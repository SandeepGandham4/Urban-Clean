package com.example.workermanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.workermanagement.dto.AssignWorkerRequestDTO;
import com.example.workermanagement.dto.AssignmentDTO;
import com.example.workermanagement.dto.AssignmentRequestDTO;
import com.example.workermanagement.dto.AssignmentResponseDTO;
import com.example.workermanagement.dto.PickupScheduleResponseDTO;
import com.example.workermanagement.dto.VehicleResponseDTO;
import com.example.workermanagement.dto.WorkerResponseDTO;
import com.example.workermanagement.enums.WorkerRole;

import jakarta.validation.Valid;

@Service
public interface AssignmentService {
    void assignWorker(Long zoneId,Long routeId,WorkerRole type,AssignmentDTO assignmentDTO);
    AssignmentResponseDTO getAssignmentById(Long assignmentId);
    List<AssignmentDTO> getAssignmentsByWorkerId(Long workerId); // Find assignments by worker ID
    Optional<AssignmentDTO> getAssignmentByZoneId(Long zoneId); // Find assignments by zone ID
    List<AssignmentResponseDTO> getAllAssignments(); // Retrieve all assignments
    AssignmentResponseDTO updateAssignment(Long assignmentId, AssignmentDTO assignmentDTO);
    void removeAssignment(Long assignmentId);
    
    List<PickupScheduleResponseDTO> getAllSchedulesWithWorkersNotAssignedForAllRoutes();
    
    List<VehicleResponseDTO> getAllVehiclesOfWorkersNotAssigned(Long zoneId);
    
    List<WorkerResponseDTO> getAllWorkersByRole(Long zoneId,Long routeId,WorkerRole typeOfWorker);
    
    List<AssignmentResponseDTO> assignWorkersToVehicle(@PathVariable Long zoneId,@PathVariable Long routeId, @RequestBody AssignmentRequestDTO assignmentRequestDTO);
    
    void notifyPickupScheduleThatAllWorkersAreScheduled(@Valid @RequestBody Long zoneId);
	void deleteAssignmentOfWorkersByVehicleId(Long vehicleId);
	void deleteAssignmentOfWorkersByZoneId(Long zoneId);
	void updateAssignmentOfWorkersByVehicleId(Long prevVehicleId, Long newVehicleId);

}

