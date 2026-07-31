package com.gourav.payflowx.service;

import com.gourav.payflowx.dto.request.TransferRequest;
import com.gourav.payflowx.dto.response.TransferResponse;

public interface PaymentService {

    TransferResponse transfer(TransferRequest request);

}