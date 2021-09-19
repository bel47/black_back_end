package com.black.wallet_service.repository;

import com.black.wallet_service.model.WalletTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransactions,Long> {
   // List<WalletTransactions> findFirst5ByUserIdByOrderByUpdatedAtDesc(Long driverID);
    List<WalletTransactions> findFirst5ByWalletIdOrderByUpdatedAtDesc(Long walletID);
}
