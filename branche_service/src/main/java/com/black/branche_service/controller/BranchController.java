package com.black.branche_service.controller;

import com.black.branche_service.exception.BranchNotFoundException;
import com.black.branche_service.model.Branch;
import com.black.branche_service.repository.BranchRepository;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class BranchController {
    private final BranchRepository branchRepository;

    public BranchController(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @GetMapping("/branches")
    List<Branch> all() {
        return branchRepository.findAll();
    }

    @PostMapping("/branches")
    Branch newBranch(@RequestBody Branch newEmployee) {
        return branchRepository.save(newEmployee);
    }

    @GetMapping("/branches/{id}")
    Branch one(@PathVariable Long id) {

        return branchRepository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException("Branch Not Found for Id: "+id));
    }

    @PutMapping("/branches/{id}")
    Branch replaceBranch(@RequestBody Branch newBranch, @PathVariable Long id) {

        return branchRepository.findById(id)
                .map(branch -> {
                    branch.setBranchID(newBranch.getBranchID());
                    branch.setBranchName(newBranch.getBranchName());
                    return branchRepository.save(branch);
                })
                .orElseGet(() -> {
                    newBranch.setId(id);
                    return branchRepository.save(newBranch);
                });
    }

    @DeleteMapping("/branches/{id}")
    void deleteBranch(@PathVariable Long id) {
        branchRepository.deleteById(id);
    }
}
