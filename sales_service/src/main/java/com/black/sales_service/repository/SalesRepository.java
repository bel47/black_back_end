package com.black.sales_service.repository;

import com.black.sales_service.model.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SalesRepository extends JpaRepository<Sales, Long> {
    Optional<Sales> findByPhoneNumber(String phone);

    Sales findByPIN(String PIN);
}
