package com.gourav.payflowx.controller;

import com.gourav.payflowx.dto.request.CreditDebitRequest;
import com.gourav.payflowx.dto.response.ApiResponse;
import com.gourav.payflowx.dto.response.WalletResponse;
import com.gourav.payflowx.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<WalletResponse>> getWallet(
            @PathVariable UUID userId) {

        WalletResponse response = walletService.getWallet(userId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Wallet fetched successfully",
                        response
                )
        );
    }

    @PostMapping("/{userId}/credit")
    public ResponseEntity<ApiResponse<WalletResponse>> credit(

            @PathVariable UUID userId,

            @Valid
            @RequestBody CreditDebitRequest request) {

        WalletResponse response =
                walletService.credit(userId, request.getAmount());

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Amount credited successfully",
                        response
                )
        );

    }

    @PostMapping("/{userId}/debit")
    public ResponseEntity<ApiResponse<WalletResponse>> debit(

            @PathVariable UUID userId,

            @Valid
            @RequestBody CreditDebitRequest request) {

        WalletResponse response =
                walletService.debit(userId, request.getAmount());

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Amount debited successfully",
                        response
                )        );

    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<WalletResponse>> getMyWallet() {

        WalletResponse response = walletService.getMyWallet();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Wallet fetched successfully",
                        response
                )
        );
    }

    @PostMapping("/me/credit")
    public ResponseEntity<ApiResponse<WalletResponse>> creditMyWallet(
            @Valid @RequestBody CreditDebitRequest request) {

        WalletResponse response =
                walletService.creditMyWallet(request.getAmount());

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Amount credited successfully",
                        response
                )
        );
    }

    @PostMapping("/me/debit")
    public ResponseEntity<ApiResponse<WalletResponse>> debitMyWallet(
            @Valid @RequestBody CreditDebitRequest request) {

        WalletResponse response =
                walletService.debitMyWallet(request.getAmount());

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Amount debited successfully",
                        response
                )
        );
    }
}