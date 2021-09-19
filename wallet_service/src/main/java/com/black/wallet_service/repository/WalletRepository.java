package com.black.wallet_service.repository;

import com.black.wallet_service.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findByDriverId(Long id);
}
