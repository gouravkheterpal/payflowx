package com.gourav.payflowx.service.impl;

import com.gourav.payflowx.common.enums.Currency;
import com.gourav.payflowx.common.enums.WalletStatus;
import com.gourav.payflowx.entity.User;
import com.gourav.payflowx.entity.Wallet;
import com.gourav.payflowx.repository.WalletRepository;
import com.gourav.payflowx.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

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
}