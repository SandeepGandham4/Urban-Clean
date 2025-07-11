package com.example.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.AssignmentDto;
import com.example.dto.PickupScheduleResponseDTO;
import com.example.dto.RouteResponseDTO;
import com.example.dto.VehicleResponseDTO;
import com.example.service.AssignmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/urbanclean/v1/admin/vehicleassignments")
@RequiredArgsConstructor

public class AssignmentController {

    
    private final AssignmentService assignmentService;

    @GetMapping("/schedules")
    public ResponseEntity<List<PickupScheduleResponseDTO>> getAllSchedulesWithRoutesNotAssigned(){
    	return ResponseEntity.ok(assignmentService.getAllSchedulesWithRoutesNotAssigned());
    } 
    
    // POST /api/urbanclean/v1/assignments
    @PostMapping("/assign/zones/{zoneId}/routes/{routeId}")
    public ResponseEntity<AssignmentDto.AssignmentResponse> createAssignment(@Valid @PathVariable Long zoneId,@Valid @PathVariable Long routeId,@Valid @RequestBody AssignmentDto.AssignmentCreateRequest request) {
        AssignmentDto.AssignmentResponse createdAssignment = assignmentService.createAssignment(zoneId,routeId,request);
        return ResponseEntity.ok(createdAssignment);
    }

    // GET /api/urbanclean/v1/assignments?date=date
    @GetMapping
    public ResponseEntity<List<AssignmentDto.AssignmentResponse>> getAssignments(
            @RequestParam(value ="date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AssignmentDto.AssignmentResponse> assignments;
        if (date != null) {
            assignments = assignmentService.getAssignmentsByDate(date);
        } else {
          
            assignments = assignmentService.getAllAssignments();
        }
        return ResponseEntity.ok(assignments);
    }
    
    // GET /api/urbanclean/v1/assignments/assignmentId 
    @GetMapping("/{assignmentId}")
    public ResponseEntity<AssignmentDto.AssignmentResponse> getAssignmentById(@PathVariable Long assignmentId) {
        AssignmentDto.AssignmentResponse assignment = assignmentService.getAssignmentById(assignmentId);
        return ResponseEntity.ok(assignment);
    }


    // PUT /api/urbanclean/v1/assignments/assignmentId
    @PutMapping("/{assignmentId}")
    public ResponseEntity<AssignmentDto.AssignmentResponse> updateAssignment(
            @PathVariable Long assignmentId,
            @Valid @RequestBody AssignmentDto.AssignmentUpdateRequest request) {
        AssignmentDto.AssignmentResponse updatedAssignment = assignmentService.updateAssignment(assignmentId, request);
        return ResponseEntity.ok(updatedAssignment);
    }

    //DELETE /api/urbanclean/v1/assignments/assignmentId 
    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<String> deleteAssignment(@PathVariable Long assignmentId) {
        assignmentService.deleteAssignment(assignmentId);
        return ResponseEntity.ok("Assignment with ID " + assignmentId + " has been successfully deleted.");
    }
    @GetMapping("/assign/zones/{zoneId}/routes")
    public ResponseEntity<List<RouteResponseDTO>> getRoutesByZoneId(@PathVariable Long zoneId){
    	return ResponseEntity.ok(assignmentService.getRoutesByZoneId(zoneId));
    }
    @PostMapping("/assign/notify")
    public void notifyPickupScheduleThatAllRoutesAreScheduled(@RequestBody Long zoneId) {
    	assignmentService.notifyPickupScheduleThatAllRoutesAreScheduled(zoneId);
    }
    @GetMapping("/workerassignment")
    public ResponseEntity<List<VehicleResponseDTO>> getVehiclesOfWorkersNotAssigned(){
    	return ResponseEntity.ok(assignmentService.getVehiclesOfWorkersNotAssigned());
    }
    @PutMapping("/worker/assigned")
    public void workerIsAssignedWithVehicles(@RequestBody Long vehicleId) {
    	assignmentService.workerIsAssignedWithVehicles(vehicleId);
    }
    @DeleteMapping("/routes/{routeId}")
    public void notifyVehicleServiceToDeleteByRoute(@Valid @PathVariable Long routeId) {
    	assignmentService.deleteAssignmentByRouteId(routeId);
    }
    @DeleteMapping("/routes/delete")
	void notifyVehicleServiceToDeleteByZone(@RequestBody List<Long> listOfRoutes) {
    	assignmentService.notifyVehicleServiceToDeleteByZone(listOfRoutes);
    }
    @PutMapping("/vehicle/updatestatus")
    public String updateVehicleStatus(@RequestBody Long vehicleId) {
        String message = assignmentService.updateVehicleStatus(vehicleId);
        return message;
    }
    @PutMapping("/vehicle/{vehicleId}/wastelogged")
	public void isWasteLogged(@PathVariable Long vehicleId,@RequestBody Long zoneId) {
    	assignmentService.isWasteLogged(vehicleId,zoneId);
    }
    
    @GetMapping("/vehicle/getstatus")
    public String getVehicleStatus(@RequestParam("vehicleId") Long vehicleId) {
        String message = assignmentService.getVehicleStatus(vehicleId);
        return message;
    }
 

    }
