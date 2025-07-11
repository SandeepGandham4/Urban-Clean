package com.example.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Assignment;
import com.example.entity.Vehicle;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByDateAssigned(LocalDate dateAssigned);
    List<Assignment> findByVehicle(Vehicle vehicle);
    boolean existsByRouteIdAndDateAssigned(Long routeId, LocalDate dateAssigned);
    List<Assignment> findAllByIsWorkerAssigned(boolean value);
    Assignment findByRouteId(Long routeId);
    void deleteByRouteId(Long routeId);
	Assignment findByVehicle_VehicleId(Long vehicleId);
}
