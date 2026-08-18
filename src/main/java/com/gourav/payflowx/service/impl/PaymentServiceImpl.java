package com.gourav.payflowx.service.impl;

import com.gourav.payflowx.common.enums.TransactionType;
import com.gourav.payflowx.common.enums.WalletStatus;
import com.gourav.payflowx.dto.request.TransferRequest;
import com.gourav.payflowx.dto.response.TransferResponse;
import com.gourav.payflowx.entity.PaymentRequest;
import com.gourav.payflowx.entity.User;
import com.gourav.payflowx.entity.Wallet;
import com.gourav.payflowx.exception.ResourceNotFoundException;
import com.gourav.payflowx.repository.PaymentRequestRepository;
import com.gourav.payflowx.repository.UserRepository;
import com.gourav.payflowx.repository.WalletRepository;
import com.gourav.payflowx.security.CurrentUserService;
import com.gourav.payflowx.service.PaymentService;
import com.gourav.payflowx.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final WalletRepository walletRepository;
    private final CurrentUserService currentUserService;
    private final TransactionService transactionService;
    private final UserRepository userRepository;
    private final PaymentRequestRepository paymentRequestRepository;

    private String generateTransactionReference() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    @Transactional
    public TransferResponse transfer(
            String idempotencyKey,
            TransferRequest request) {

        // 1. Get authenticated sender
        User sender = currentUserService.getCurrentUser();

        // 2. Check whether this request was already processed
        Optional<PaymentRequest> existingRequest =
                paymentRequestRepository.findByIdempotencyKeyAndSenderId(
                        idempotencyKey,
                        sender.getId()
                );

        if (existingRequest.isPresent()) {

            PaymentRequest existing = existingRequest.get();

            return TransferResponse.builder()
                    .transactionId(existing.getTransactionReference())
                    .senderEmail(sender.getEmail())
                    .receiverEmail(existing.getReceiverEmail())
                    .amount(existing.getAmount())
                    .status(existing.getStatus())
                    .transferredAt(existing.getCreatedAt())
                    .build();
        }

        // 3. Find sender wallet
        Wallet senderWallet = walletRepository.findByUserId(sender.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sender wallet not found"));

        // 4. Find receiver
        User receiver = userRepository.findByEmail(request.getReceiverEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Receiver not found"));

        // 5. Prevent self transfer
        if (sender.getId().equals(receiver.getId())) {
            throw new IllegalArgumentException(
                    "You cannot transfer money to yourself"
            );
        }

        // 6. Find receiver wallet
        Wallet receiverWallet = walletRepository.findByUserId(receiver.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Receiver wallet not found"));

        // 7. Validate wallet status
        if (senderWallet.getStatus() != WalletStatus.ACTIVE) {
            throw new IllegalStateException("Sender wallet is not active");
        }

        if (receiverWallet.getStatus() != WalletStatus.ACTIVE) {
            throw new IllegalStateException("Receiver wallet is not active");
        }

        // 8. Generate ONE reference for the entire transfer
        String reference = generateTransactionReference();

        // 9. Debit sender
        senderWallet.debit(request.getAmount());

        // 10. Credit receiver
        receiverWallet.credit(request.getAmount());

        // 11. Save updated wallets
        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        // 12. Create sender DEBIT transaction
        transactionService.createTransaction(
                senderWallet,
                request.getAmount(),
                TransactionType.DEBIT,
                reference,
                request.getDescription()
        );

        // 13. Create receiver CREDIT transaction
        transactionService.createTransaction(
                receiverWallet,
                request.getAmount(),
                TransactionType.CREDIT,
                reference,
                request.getDescription()
        );

        // 14. Save idempotency record
        PaymentRequest paymentRequest =
                PaymentRequest.builder()
                        .id(UUID.randomUUID())
                        .idempotencyKey(idempotencyKey)
                        .sender(sender)
                        .transactionReference(reference)
                        .amount(request.getAmount())
                        .receiverEmail(receiver.getEmail())
                        .status("SUCCESS")
                        .build();

        paymentRequestRepository.save(paymentRequest);

        // 15. Return response
        return TransferResponse.builder()
                .transactionId(reference)
                .senderEmail(sender.getEmail())
                .receiverEmail(receiver.getEmail())
                .amount(request.getAmount())
                .status("SUCCESS")
                .transferredAt(LocalDateTime.now())
                .build();
    }}