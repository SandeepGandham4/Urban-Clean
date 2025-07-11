	package com.example.workermanagement.controller;
import java.util.List;
//import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.workermanagement.dto.AssignmentDTO;
import com.example.workermanagement.dto.AssignmentRequestDTO;
import com.example.workermanagement.dto.AssignmentResponseDTO;
import com.example.workermanagement.dto.PickupScheduleResponseDTO;
import com.example.workermanagement.dto.VehicleResponseDTO;
import com.example.workermanagement.dto.WorkerResponseDTO;
import com.example.workermanagement.enums.WorkerRole;
import com.example.workermanagement.service.AssignmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/urbanclean/v1/admin/workerassignments")
@RequiredArgsConstructor
@Slf4j
//@CrossOrigin(
//	    origins = "http://localhost:3000",
//	    allowedHeaders = {"Authorization", "Content-Type"},
//	    //methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
//	    allowCredentials = "true"
//	)
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping("/assign/zones/{zoneId}/routes/{routeId}/worker/{workerId}")
    public void assignWorker(@PathVariable Long zoneId,@PathVariable Long routeId,@RequestParam WorkerRole typeOfWorker,@RequestBody AssignmentDTO assignmentDTO) {
         assignmentService.assignWorker(zoneId,routeId,typeOfWorker,assignmentDTO);
         //ResponseEntity.ok("Worker added");
    }
    
    @PostMapping("/assign/zones/{zoneId}/routes/{routeId}/save")
    public ResponseEntity<List<AssignmentResponseDTO>> assignWorkersToVehicle(@PathVariable Long zoneId,@PathVariable Long routeId,@RequestBody AssignmentRequestDTO assignmentRequestDTO){
    	return ResponseEntity.ok(assignmentService.assignWorkersToVehicle(zoneId,routeId,assignmentRequestDTO));
    }
    
    @PostMapping("/assign/notify/{zoneId}")
    public void notifyPickupScheduleThatAllWorkersAreScheduled(@Valid @PathVariable Long zoneId) {
    	assignmentService.notifyPickupScheduleThatAllWorkersAreScheduled(zoneId);
    }
    
    @GetMapping("/assign/zones/{zoneId}/routes/{routeId}/worker")
    public ResponseEntity<List<WorkerResponseDTO>> getAllWorkersByRole(@PathVariable Long zoneId,@PathVariable Long routeId,@RequestParam WorkerRole typeOfWorker){
    	return ResponseEntity.ok(assignmentService.getAllWorkersByRole(zoneId,routeId,typeOfWorker));
    }

    @GetMapping("/{assignmentId}")
    public ResponseEntity<AssignmentResponseDTO> getAssignmentById(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(assignmentService.getAssignmentById(assignmentId));
    }


    @GetMapping("/worker/{workerId}")
    public ResponseEntity<List<AssignmentDTO>> getAssignmentsByWorkerId(@PathVariable Long workerId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByWorkerId(workerId));
    }
    
	

    @GetMapping
    public ResponseEntity<List<AssignmentResponseDTO>> getAllAssignments() {
        List<AssignmentResponseDTO> assignments = assignmentService.getAllAssignments();
        return ResponseEntity.ok(assignments);
    }
    @GetMapping("/schedules")
    public ResponseEntity<List<PickupScheduleResponseDTO>> getAllSchedulesWithWorkersNotAssignedForAllRoutes(){
    	return ResponseEntity.ok(assignmentService.getAllSchedulesWithWorkersNotAssignedForAllRoutes());
    }

    @PutMapping("/{assignmentId}")
    public ResponseEntity<AssignmentResponseDTO> updateAssignment(@PathVariable Long assignmentId, @RequestBody AssignmentDTO assignmentDTO) {
            return ResponseEntity.ok(assignmentService.updateAssignment(assignmentId, assignmentDTO));
    }

    @DeleteMapping("/{assignmentId}")
    public void removeAssignment(@PathVariable Long assignmentId) {
            assignmentService.removeAssignment(assignmentId);
    }
    @GetMapping("/assign/zones/{zoneId}")
    public ResponseEntity<List<VehicleResponseDTO>> getAllVehiclesOfWorkersNotAssigned(@PathVariable Long zoneId){
    	return ResponseEntity.ok(assignmentService.getAllVehiclesOfWorkersNotAssigned(zoneId));
    }
    
    @DeleteMapping("/assignments/{vehicleId}")
    public void deleteAssignmentOfWorkersByVehicleId(@PathVariable Long vehicleId) {
    	assignmentService.deleteAssignmentOfWorkersByVehicleId(vehicleId);
    }
    
    @PutMapping("/assignments/{prevVehicleId}/updateto/{newVehicleId}")
	public void updateAssignmentOfWorkersByVehicleId(@PathVariable Long prevVehicleId,@PathVariable Long newVehicleId) {
    	assignmentService.updateAssignmentOfWorkersByVehicleId(prevVehicleId,newVehicleId);
    }
    
    @DeleteMapping("/assignments/zone/{zoneId}")
    public void deleteAssignmentOfWorkersByZoneId(@PathVariable Long zoneId) {
    	assignmentService.deleteAssignmentOfWorkersByZoneId(zoneId);
    }
    
    
    
}
