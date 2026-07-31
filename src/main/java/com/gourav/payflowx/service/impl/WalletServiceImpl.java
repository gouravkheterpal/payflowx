package com.gourav.payflowx.service.impl;

import com.gourav.payflowx.common.enums.Currency;
import com.gourav.payflowx.common.enums.TransactionType;
import com.gourav.payflowx.common.enums.WalletStatus;
import com.gourav.payflowx.dto.response.WalletResponse;
import com.gourav.payflowx.entity.User;
import com.gourav.payflowx.entity.Wallet;
import com.gourav.payflowx.exception.ResourceNotFoundException;
import com.gourav.payflowx.repository.WalletRepository;
import com.gourav.payflowx.security.CurrentUserService;
import com.gourav.payflowx.service.TransactionService;
import com.gourav.payflowx.service.WalletService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import com.gourav.payflowx.mapper.WalletMapper;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    private final TransactionService transactionService;
    private final CurrentUserService currentUserService;
    private String generateTransactionReference() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public Wallet createWallet(User user) {

        log.info("Creating wallet for user {}", user.getId());

        Wallet wallet = Wallet.builder()
                .id(UUID.randomUUID())
                .user(user)
                .balance(BigDecimal.ZERO)
                .currency(Currency.INR)
                .status(WalletStatus.ACTIVE)
                .build();

        Wallet savedWallet = walletRepository.save(wallet);

        log.info("Wallet created successfully. WalletId={}", savedWallet.getId());

        return savedWallet;
    }

    private Wallet getWalletEntity(UUID userId) {

        return walletRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Wallet not found"));

    }

    @Override
    public WalletResponse getWallet(UUID userId) {

        Wallet wallet = getWalletEntity(userId);

        return walletMapper.toResponse(wallet);

    }

    @Override
    @Transactional
    public WalletResponse credit(UUID userId, BigDecimal amount) {

        Wallet wallet = getWalletEntity(userId);

        if (wallet.getStatus() == WalletStatus.FROZEN) {
            throw new IllegalStateException("Wallet is frozen");
        }

        wallet.credit(amount);

        Wallet updatedWallet = walletRepository.save(wallet);

        transactionService.createTransaction(
                wallet,
                amount,
                TransactionType.CREDIT,
                generateTransactionReference(),
                "Wallet credited"
        );

        return walletMapper.toResponse(updatedWallet);

    }

    @Override
    @Transactional
    public WalletResponse debit(UUID userId, BigDecimal amount) {

        Wallet wallet = getWalletEntity(userId);

        if (wallet.getStatus() == WalletStatus.FROZEN) {
            throw new IllegalStateException("Wallet is frozen");
        }

        wallet.debit(amount);

        Wallet updatedWallet = walletRepository.save(wallet);

        transactionService.createTransaction(
                wallet,
                amount,
                TransactionType.DEBIT,
                generateTransactionReference(),
                "Wallet debited"
        );

        return walletMapper.toResponse(updatedWallet);

    }

    @Override
    public WalletResponse getMyWallet() {

        User currentUser = currentUserService.getCurrentUser();

        return getWallet(currentUser.getId());
    }

    @Override
    public WalletResponse creditMyWallet(BigDecimal amount) {

        User currentUser = currentUserService.getCurrentUser();

        return credit(currentUser.getId(), amount);
    }

    @Override
    public WalletResponse debitMyWallet(BigDecimal amount) {

        User currentUser = currentUserService.getCurrentUser();

        return debit(currentUser.getId(), amount);
    }

    @Override
    @Transactional
    public WalletResponse freezeWallet(UUID userId) {

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wallet not found"));

        wallet.setStatus(WalletStatus.FROZEN);

        walletRepository.save(wallet);

        return walletMapper.toResponse(wallet);
    }

    @Override
    @Transactional
    public WalletResponse activateWallet(UUID userId) {

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wallet not found"));

        wallet.setStatus(WalletStatus.ACTIVE);

        walletRepository.save(wallet);

        return walletMapper.toResponse(wallet);
    }

}