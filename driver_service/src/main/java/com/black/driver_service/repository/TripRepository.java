package com.black.driver_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.black.driver_service.model.Trip;

public interface TripRepository extends JpaRepository <Trip, Long> {

}
