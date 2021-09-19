package com.black.sales_service.repository;

import com.black.sales_service.model.Sales;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SalesRepository extends JpaRepository<Sales, Long> {
    //    List<Sales> findByPIN(String phone);
    Optional<Sales> findByPhoneNumber(String phone);

    Sales findByPIN(String PIN);
}
