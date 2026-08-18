package com.gourav.payflowx.controller;

import com.gourav.payflowx.dto.request.TransferRequest;
import com.gourav.payflowx.dto.response.TransferResponse;
import com.gourav.payflowx.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Money Transfer APIs")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(

            @RequestHeader("Idempotency-Key")
            String idempotencyKey,

            @Valid
            @RequestBody
            TransferRequest request) {

        TransferResponse response =
                paymentService.transfer(idempotencyKey, request);

        return ResponseEntity.ok(response);
    }
}