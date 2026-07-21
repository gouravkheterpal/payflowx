package com.gourav.payflowx.service.impl;

import com.gourav.payflowx.common.enums.TransactionStatus;
import com.gourav.payflowx.common.enums.TransactionType;
import com.gourav.payflowx.dto.response.TransactionResponse;
import com.gourav.payflowx.dto.response.TransactionResponseDto;
import com.gourav.payflowx.entity.Transaction;
import com.gourav.payflowx.entity.Wallet;
import com.gourav.payflowx.mapper.TransactionMapper;
import com.gourav.payflowx.repository.TransactionRepository;
import com.gourav.payflowx.repository.WalletRepository;
import com.gourav.payflowx.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.gourav.payflowx.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final TransactionMapper transactionMapper;

    @Override
    @Transactional
    public Transaction createTransaction(
            Wallet wallet,
            BigDecimal amount,
            TransactionType type,
            String description) {

        log.info("Creating {} transaction for walletId={}, amount={}",
                type, wallet.getId(), amount);

        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .wallet(wallet)
                .amount(amount)
                .type(type)
                .status(TransactionStatus.SUCCESS)
                .reference(generateTransactionReference())
                .description(description)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        log.info("Transaction created successfully. reference={}",
                savedTransaction.getReference());

        return savedTransaction;
    }

    private String generateTransactionReference() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactions(UUID userId) {

        log.info("Fetching transactions for userId={}", userId);

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wallet not found"));

        List<Transaction> transactions =
                transactionRepository.findByWalletIdOrderByCreatedAtDesc(wallet.getId());

        return transactions.stream()
                .map(transactionMapper::toResponse)
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getTransactions(
            UUID userId,
            Pageable pageable) {

        return transactionRepository
                .findByWallet_User_Id(userId, pageable)
                .map(transactionMapper::toDto);
    }

    @Override
    public Page<TransactionResponseDto> getTransactions(UUID userId, java.awt.print.Pageable pageable) {
        return null;
    }
}