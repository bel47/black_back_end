package com.black.branche_service.model;

import javax.persistence.*;

@Entity
public class Branch{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "branch_name")
    private String branchName;
    @Column(name = "branch_id")
    private String branchID;

    public Branch() {
    }

    public Branch(Long id, String branchName, String branchID) {
        this.id = id;
        this.branchName = branchName;
        this.branchID = branchID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }
}
