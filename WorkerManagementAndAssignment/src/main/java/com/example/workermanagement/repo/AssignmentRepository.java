package com.example.workermanagement.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.workermanagement.entity.Assignment;


@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByWorker_WorkerId(Long workerId); // Find assignments by worker ID
    Optional<Assignment> findByZoneId(Long zoneId); // Find assignments by zone ID
    void deleteById(Long assignmentId);
    @Modifying
    @Query("delete from Assignment a where a.worker.workerId=?1")
    void deleteByWorkerId(Long workerId);
	void deleteAllByZoneId(Long zoneId);
	void deleteByWorker_WorkerId(Long workerId);
}
