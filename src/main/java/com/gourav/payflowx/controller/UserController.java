package com.gourav.payflowx.controller;

import com.gourav.payflowx.dto.request.CreateUserRequest;
import com.gourav.payflowx.dto.response.ApiResponse;
import com.gourav.payflowx.dto.response.TransactionResponseDto;
import com.gourav.payflowx.dto.response.UserResponse;
import com.gourav.payflowx.service.TransactionService;
import com.gourav.payflowx.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.data.domain.Pageable;
import java.util.UUID;

import org.springframework.data.domain.Page;

import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        UserResponse response = userService.createUser(request);

        ApiResponse<UserResponse> apiResponse =
                ApiResponse.<UserResponse>builder()
                        .success(true)
                        .message("User created successfully")
                        .data(response)
                        .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiResponse);
    }

    @GetMapping("/{userId}/transactions")
    public ResponseEntity<ApiResponse<Page<TransactionResponseDto>>> getTransactions(

            @PathVariable UUID userId,

            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable) {

        Page<TransactionResponseDto> response =
                transactionService.getTransactions(userId, pageable);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Transactions fetched successfully",
                        response
                )
        );
    }
}