package com.gourav.payflowx.mapper;

import com.gourav.payflowx.dto.response.WalletResponse;
import com.gourav.payflowx.entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    @Mapping(target = "walletId", source = "id")
    @Mapping(target = "userId", source = "user.id")
    WalletResponse toResponse(Wallet wallet);

}
