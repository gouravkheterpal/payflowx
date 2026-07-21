package com.gourav.payflowx.repository;

import com.gourav.payflowx.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByWalletIdOrderByCreatedAtDesc(UUID walletId);

    Optional<Transaction> findByReference(String reference);

    Page<Transaction> findByWallet_User_Id(
            UUID userId,
            Pageable pageable
    );

}