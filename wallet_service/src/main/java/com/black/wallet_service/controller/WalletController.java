package com.black.wallet_service.controller;

import com.black.wallet_service.exception.ResourceNotFoundException;
import com.black.wallet_service.model.Wallet;
import com.black.wallet_service.model.WalletTransactions;
import com.black.wallet_service.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/wallet")
@RestController
public class WalletController {
    private final WalletRepository walletRepository;

    public WalletController(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }


    @GetMapping("/driver/{id}")
    public ResponseEntity<Wallet> getWalletByDriverId(@PathVariable Long id) {
        Wallet wallet = walletRepository.findByDriverId(id);
        return ResponseEntity.ok(wallet);
    }
}
