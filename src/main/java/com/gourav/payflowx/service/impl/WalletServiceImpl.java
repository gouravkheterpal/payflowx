package com.gourav.payflowx.service.impl;

import com.gourav.payflowx.common.enums.Currency;
import com.gourav.payflowx.common.enums.WalletStatus;
import com.gourav.payflowx.dto.response.WalletResponse;
import com.gourav.payflowx.entity.User;
import com.gourav.payflowx.entity.Wallet;
import com.gourav.payflowx.exception.ResourceNotFoundException;
import com.gourav.payflowx.repository.WalletRepository;
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

        wallet.credit(amount);

        Wallet updatedWallet = walletRepository.save(wallet);

        return walletMapper.toResponse(updatedWallet);

    }

    @Override
    @Transactional
    public WalletResponse debit(UUID userId, BigDecimal amount) {

        Wallet wallet = getWalletEntity(userId);

        wallet.debit(amount);

        Wallet updatedWallet = walletRepository.save(wallet);

        return walletMapper.toResponse(updatedWallet);

    }

}