package com.black.wallet_service.controller;

import com.black.wallet_service.exception.ResourceNotFoundException;
import com.black.wallet_service.model.WalletUpload;
import com.black.wallet_service.repository.WalletTransactionRepository;
import com.black.wallet_service.repository.WalletUploadRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WalletUploadController {

    private final WalletTransactionRepository walletTransactionRepository;

    private final WalletUploadRepository walletUploadRepository;

    public WalletUploadController( WalletUploadRepository walletUploadRepository, WalletTransactionRepository walletTransactionRepository) {
        this.walletUploadRepository = walletUploadRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }


    @GetMapping("/wallets/{id}/attachment")
    public Page<WalletUpload> getAllCommentsByPostId(@PathVariable(value = "id") Long id, Pageable pageable) {
        return walletUploadRepository.findByWalletTransactionsId(id, pageable);
    }

    @PostMapping("/wallets/{id}/attachment")
    public WalletUpload createWalletUpload(@PathVariable (value = "id") Long id, @RequestBody WalletUpload walletUpload) {
        return walletTransactionRepository.findById(id).map(post -> {
            walletUpload.setWalletTransactions(post);
            return walletUploadRepository.save(walletUpload);
        }).orElseThrow(() -> new ResourceNotFoundException("Wallet Id: " + id + " not found"));
    }

    @PutMapping("/wallets/{id}/attachment/{uploadId}")
    public WalletUpload updateWallet(@PathVariable (value = "id") Long id,  @PathVariable (value = "uploadId") Long uploadId, @RequestBody WalletUpload walletUpload) {
        if(!walletTransactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Wallet Id: " + id + " not found");
        }

        return walletUploadRepository.findById(uploadId).map(request -> {
            request.setFileName(walletUpload.getFileName());
            request.setFileSize(walletUpload.getFileSize());
            request.setFileType(walletUpload.getFileType());
            return walletUploadRepository.save(request);
        }).orElseThrow(() -> new ResourceNotFoundException("CommentId " + uploadId + "not found"));
    }

    @DeleteMapping("/wallets/{id}/attachment/{uploadId}")
    public ResponseEntity<?> deleteComment(@PathVariable (value = "id") Long postId,
                                           @PathVariable (value = "uploadId") Long uploadId) {
        return walletUploadRepository.findByIdAndWalletTransactionsId(uploadId, postId).map(comment -> {
            walletUploadRepository.delete(comment);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + uploadId + " and postId " + postId));
    }
}
