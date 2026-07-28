package com.gourav.payflowx.controller;

import com.gourav.payflowx.dto.response.ApiResponse;
import com.gourav.payflowx.dto.response.WalletResponse;
import com.gourav.payflowx.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final WalletService walletService;

    @PostMapping("/wallets/{userId}/freeze")
    public ResponseEntity<ApiResponse<WalletResponse>> freezeWallet(
            @PathVariable UUID userId) {

        WalletResponse response = walletService.freezeWallet(userId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Wallet frozen successfully",
                        response
                )
        );
    }

    @PostMapping("/wallets/{userId}/activate")
    public ResponseEntity<ApiResponse<WalletResponse>> activateWallet(
            @PathVariable UUID userId) {

        WalletResponse response = walletService.activateWallet(userId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Wallet activated successfully",
                        response
                )
        );
    }
}