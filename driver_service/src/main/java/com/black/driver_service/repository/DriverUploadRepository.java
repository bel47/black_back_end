package com.black.driver_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.black.driver_service.model.DriverUpload;

import java.util.List;

public interface DriverUploadRepository extends JpaRepository <DriverUpload, Long> {
	List<DriverUpload> findAllByDriverId(long driver_id);
}
