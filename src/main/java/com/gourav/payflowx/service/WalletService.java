package com.gourav.payflowx.service;

import com.gourav.payflowx.dto.response.WalletResponse;
import com.gourav.payflowx.entity.User;
import com.gourav.payflowx.entity.Wallet;

import java.math.BigDecimal;
import java.util.UUID;

public interface WalletService {

    Wallet createWallet(User user);

    WalletResponse getWallet(UUID userId);

    WalletResponse credit(UUID userId, BigDecimal amount);

    WalletResponse debit(UUID userId, BigDecimal amount);

    WalletResponse getMyWallet();

    WalletResponse creditMyWallet(BigDecimal amount);

    WalletResponse debitMyWallet(BigDecimal amount);

    WalletResponse freezeWallet(UUID userId);

    WalletResponse activateWallet(UUID userId);
}