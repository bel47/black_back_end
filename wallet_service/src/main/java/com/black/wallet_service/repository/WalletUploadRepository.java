package com.black.wallet_service.repository;

import com.black.wallet_service.model.WalletUpload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletUploadRepository extends JpaRepository<WalletUpload, Long> {
    Page<WalletUpload> findByWalletTransactionsId(Long WalletTransactionsId, Pageable pageable);
    Optional<WalletUpload> findByIdAndWalletTransactionsId(Long id, Long WalletTransactionsId);
}
