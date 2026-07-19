package com.gourav.payflowx.repository;

import com.gourav.payflowx.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByWalletIdOrderByCreatedAtDesc(UUID walletId);

    Optional<Transaction> findByReference(String reference);

}