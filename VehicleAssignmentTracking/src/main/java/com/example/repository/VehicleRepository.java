package com.example.repository;

import com.example.entity.Vehicle;
import com.example.enums.VehicleStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    boolean existsByRegistrationNo(String registrationNo);

	List<Vehicle> findAllByStatus(VehicleStatus active);
}
