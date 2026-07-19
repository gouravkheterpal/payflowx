package com.gourav.payflowx.service;

import com.gourav.payflowx.common.enums.TransactionType;
import com.gourav.payflowx.dto.response.TransactionResponse;
import com.gourav.payflowx.entity.Transaction;
import com.gourav.payflowx.entity.Wallet;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface TransactionService {

    Transaction createTransaction(
            Wallet wallet,
            BigDecimal amount,
            TransactionType type,
            String description
    );

    List<TransactionResponse> getTransactions(UUID userId);
}