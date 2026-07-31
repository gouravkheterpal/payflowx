package com.gourav.payflowx.service.impl;

import com.gourav.payflowx.common.enums.TransactionType;
import com.gourav.payflowx.dto.request.TransferRequest;
import com.gourav.payflowx.dto.response.TransferResponse;
import com.gourav.payflowx.entity.User;
import com.gourav.payflowx.entity.Wallet;
import com.gourav.payflowx.exception.ResourceNotFoundException;
import com.gourav.payflowx.repository.UserRepository;
import com.gourav.payflowx.repository.WalletRepository;
import com.gourav.payflowx.security.CurrentUserService;
import com.gourav.payflowx.service.PaymentService;
import com.gourav.payflowx.service.TransactionService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.gourav.payflowx.common.enums.WalletStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final WalletRepository walletRepository;
    private final CurrentUserService currentUserService;
    private final TransactionService transactionService;
    private final UserRepository userRepository;

    private String generateTransactionReference() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    @Transactional
    public TransferResponse transfer(TransferRequest request) {

        User sender = currentUserService.getCurrentUser();

        User receiver = walletRepository.findByUserEmail(request.getReceiverEmail())
                .map(Wallet::getUser)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Receiver not found"));

        Wallet receiverWallet = walletRepository.findByUserId(receiver.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Receiver wallet not found"));

        if (sender.getId().equals(receiver.getId())) {
            throw new IllegalArgumentException("You cannot transfer money to yourself");
        }

        Wallet senderWallet = walletRepository.findByUserId(sender.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sender wallet not found"));

        if (senderWallet.getStatus() != WalletStatus.ACTIVE) {
            throw new IllegalStateException("Sender wallet is not active");
        }

        if (receiverWallet.getStatus() != WalletStatus.ACTIVE) {
            throw new IllegalStateException("Receiver wallet is not active");
        }

        String reference = generateTransactionReference();

        senderWallet.debit(request.getAmount());

        receiverWallet.credit(request.getAmount());

        walletRepository.save(senderWallet);

        walletRepository.save(receiverWallet);

        transactionService.createTransaction(
                senderWallet,
                request.getAmount(),
                TransactionType.DEBIT,
                reference,
                request.getDescription()
        );

        transactionService.createTransaction(
                receiverWallet,
                request.getAmount(),
                TransactionType.CREDIT,
                reference,
                request.getDescription()
        );


        return TransferResponse.builder()
                .transactionId(reference)
                .senderEmail(sender.getEmail())
                .receiverEmail(receiver.getEmail())
                .amount(request.getAmount())
                .status("SUCCESS")
                .transferredAt( LocalDateTime.now())
                .build();
    }
}