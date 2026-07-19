package com.gourav.payflowx.controller;

import com.gourav.payflowx.dto.response.ApiResponse;
import com.gourav.payflowx.dto.response.TransactionResponse;
import com.gourav.payflowx.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactions(
            @PathVariable UUID userId) {

        List<TransactionResponse> transactions =
                transactionService.getTransactions(userId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Transactions fetched successfully",
                        transactions
                )
        );
    }
}