package com.black.wallet_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@Entity
public class WalletUpload extends AuditModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;
    @Column(name = "type")
    private String fileType;
    @Column(name = "size")
    private byte fileSize;

   // @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "walletTransactions_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private WalletTransactions walletTransactions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte getFileSize() {
        return fileSize;
    }

    public void setFileSize(byte fileSize) {
        this.fileSize = fileSize;
    }

    public WalletTransactions getWalletTransactions() {
        return walletTransactions;
    }

    public void setWalletTransactions(WalletTransactions walletTransactions) {
        this.walletTransactions = walletTransactions;
    }

    
}
