package com.example.workermanagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.workermanagement.entity.Worker;
import com.example.workermanagement.enums.WorkerRole;

import java.util.Optional;
import java.util.List;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
    List<Worker> findByName(String name); // Allow finding workers by name
    List<Worker> findAllByRole(WorkerRole role);     // Allow filtering workers by role (Driver/Garbage Collector)
    Worker findByContactInfo(String contactInfo);

}
