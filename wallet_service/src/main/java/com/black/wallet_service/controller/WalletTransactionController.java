package com.black.wallet_service.controller;

import com.black.wallet_service.exception.ResourceNotFoundException;
import com.black.wallet_service.model.WalletTransactions;
import com.black.wallet_service.repository.WalletTransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/wallet-transactions")
@RestController
public class WalletTransactionController {
   private  WalletTransactionRepository walletTransactionRepository;

    public WalletTransactionController(WalletTransactionRepository walletTransactionRepository) {
        this.walletTransactionRepository = walletTransactionRepository;
    }

    // get all transaction
    @GetMapping("/")
    public List<WalletTransactions> getAllWalletTransaction(){
        return walletTransactionRepository.findAll();
    }

    @GetMapping("/list")
    public Page<WalletTransactions> getAllWalletPageable(Pageable pageable) {
        return walletTransactionRepository.findAll(pageable);
    }

    @PostMapping("/create")
    public WalletTransactions createWallet(@RequestBody WalletTransactions walletTransactions) {
        return walletTransactionRepository.save(walletTransactions);
    }

    // get wallet by id rest api
    @GetMapping("/wallets/{id}")
    public ResponseEntity<WalletTransactions> getWalletById(@PathVariable Long id) {
        WalletTransactions walletTransactions = walletTransactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not exist with id :" + id));
        return ResponseEntity.ok(walletTransactions);
    }

   // update wallet rest api
 @PutMapping("/update/{id}")
    public ResponseEntity<WalletTransactions> updateWallet(@PathVariable Long id, @RequestBody WalletTransactions walletTransactions){
        WalletTransactions walletTransactions1 = walletTransactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :" + id));

        walletTransactions.setDebitCredit(walletTransactions.getDebitCredit());
        walletTransactions.setDebitCredit(walletTransactions.getDebitCredit());
        walletTransactions.setOpeningBalance(walletTransactions.getOpeningBalance());
        walletTransactions.setTransAmount(walletTransactions.getTransAmount());
        walletTransactions.setUserId(walletTransactions.getUserId());

        WalletTransactions updatedWallet = walletTransactionRepository.save(walletTransactions);
        return ResponseEntity.ok(updatedWallet);

    }
 /*
      // delete wallet rest api
    @DeleteMapping("/wallets/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteWallet(@PathVariable Long id){
        WalletTransactions employee = walletTransactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :" + id));

        walletTransactionRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }*/

    // update wallet rest api
/*    @PutMapping("/update/{id}")
    public WalletTransactions updateWallet(@PathVariable Long id, @RequestBody WalletTransactions walletTransactions){
        return walletTransactionRepository.findById(id).map(request -> {
            walletTransactions.setDebitCredit(walletTransactions.getDebitCredit());
            walletTransactions.setDebitCredit(walletTransactions.getDebitCredit());
            walletTransactions.setOpeningBalance(walletTransactions.getOpeningBalance());
            walletTransactions.setTransAmount(walletTransactions.getTransAmount());
            walletTransactions.setUserId(walletTransactions.getUserId());
            return walletTransactionRepository.save(request);
        }).orElseThrow(() -> new ResourceNotFoundException("Wallet Id: " + id + " not found"));
    }*/

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteWallet(@PathVariable Long id) {
        return walletTransactionRepository.findById(id).map(request -> {
            walletTransactionRepository.delete(request);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Wallet Id: " + id + " not found"));
    }

    @GetMapping("/last-five/{id}")
    public List<WalletTransactions> getFiveWalletTransaction(@PathVariable Long id){
        return walletTransactionRepository.findFirst5ByWalletIdOrderByUpdatedAtDesc(id);
    }
}
