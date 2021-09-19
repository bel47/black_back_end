package com.black.passenger_service.repository;

import com.black.passenger_service.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Passenger findByPhoneNumber(String phone);

    boolean existsByPhoneNumber(String phone);

}
