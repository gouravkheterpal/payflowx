package com.gourav.payflowx.mapper;

import com.gourav.payflowx.dto.response.TransactionResponse;
import com.gourav.payflowx.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "transactionId", source = "id")
    TransactionResponse toResponse(Transaction transaction);
}