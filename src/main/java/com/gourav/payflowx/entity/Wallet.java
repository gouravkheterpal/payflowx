package com.gourav.payflowx.entity;

import com.gourav.payflowx.common.enums.Currency;
import com.gourav.payflowx.common.enums.WalletStatus;
import com.gourav.payflowx.exception.InsufficientBalanceException;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet extends BaseEntity {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WalletStatus status;

    public void credit(BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        this.balance = this.balance.add(amount);
    }

    public void debit(BigDecimal amount) {

        if (balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient wallet balance");        }

        this.balance = this.balance.subtract(amount);
    }

    @OneToMany(
            mappedBy = "wallet",
            fetch = FetchType.LAZY
    )
    private List<Transaction> transactions = new ArrayList<>();
}