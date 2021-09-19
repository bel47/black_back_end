package com.black.driver_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import com.black.driver_service.model.Driver;

public interface DriverRepository extends JpaRepository <Driver, Long> {
	Optional<Driver> findByPhoneNumber(String phone);

}
