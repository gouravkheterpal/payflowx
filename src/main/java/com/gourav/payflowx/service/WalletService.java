package com.gourav.payflowx.service;

import com.gourav.payflowx.entity.User;
import com.gourav.payflowx.entity.Wallet;

public interface WalletService {

    Wallet createWallet(User user);

}