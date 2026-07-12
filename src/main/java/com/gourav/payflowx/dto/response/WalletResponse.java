package com.gourav.payflowx.dto.response;

import com.gourav.payflowx.common.enums.Currency;
import com.gourav.payflowx.common.enums.WalletStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter

public class WalletResponse {

    private UUID walletId;

    private UUID userId;

    private BigDecimal balance;

    private Currency currency;

    private WalletStatus status;
}
