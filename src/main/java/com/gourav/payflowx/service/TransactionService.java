package com.gourav.payflowx.service;

import com.gourav.payflowx.common.enums.TransactionType;
import com.gourav.payflowx.dto.response.TransactionResponse;
import com.gourav.payflowx.dto.response.TransactionResponseDto;
import com.gourav.payflowx.entity.Transaction;
import com.gourav.payflowx.entity.Wallet;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface TransactionService {

    Transaction createTransaction(
            Wallet wallet,
            BigDecimal amount,
            TransactionType type,
            String reference,
            String description
    );

    Page<TransactionResponseDto> getTransactions(
            UUID userId,
            Pageable pageable
    );

    List<TransactionResponse> getTransactions(UUID userId);
}