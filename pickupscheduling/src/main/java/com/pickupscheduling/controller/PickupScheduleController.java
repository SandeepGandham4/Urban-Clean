package com.pickupscheduling.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pickupscheduling.dto.PickupScheduleRequestDTO;
import com.pickupscheduling.dto.PickupScheduleResponseDTO;
import com.pickupscheduling.dto.ZoneResponseDTO;
import com.pickupscheduling.entity.PickupSchedule;
import com.pickupscheduling.service.PickupScheduleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/urbanclean/supervisor/schedules")
@RequiredArgsConstructor
@Tag(name = "Pickup Schedule Controller", description = "Controller for managing pickup scheduling operations")
//@CrossOrigin(origins = "http://localhost:3000")
//@CrossOrigin(
//	    origins = "http://localhost:3000",
//	    allowedHeaders = {"Authorization", "Content-Type"},
//	    //methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
//	    allowCredentials = "true"
//	)
public class PickupScheduleController {
    
        private final PickupScheduleService service;
        
        @GetMapping("/notify")
        @Operation(summary = "Notifies that a new zones created", description = "Displays a set of newly created zones")
        public ResponseEntity<List<ZoneResponseDTO>> newlyAddedZones(){
        	return ResponseEntity.ok(service.newlyAddedZones());
        }
        
        @PostMapping("/create")
        @Operation(summary = "Create a pickup schedule", description = "Registers a new pickup schedule with provided details.")
        public ResponseEntity<PickupScheduleResponseDTO> createSchedule(@Valid @RequestBody PickupScheduleRequestDTO dto) {
                return ResponseEntity.ok(service.createSchedule(dto));
        }

       
        @GetMapping
        @Operation(summary = "Get all pickup schedules", description = "Fetches a list of all available pickup schedules.")
        public ResponseEntity<List<PickupScheduleResponseDTO>> getAllSchedules() {
                return ResponseEntity.ok(service.getAllSchedules());
        }

        
        @PutMapping("/{scheduleId}")    
        @Operation(summary = "Update a pickup schedule", description = "Modifies an existing schedule based on the provided ID.")
        public ResponseEntity<PickupSchedule> updateSchedule(@Valid @PathVariable Long scheduleId, @Valid @RequestBody PickupScheduleRequestDTO dto) {
                return ResponseEntity.ok(service.updateSchedule(scheduleId, dto));
        }

        
        @DeleteMapping("/{scheduleId}")
        @Operation(summary = "Delete a pickup schedule", description = "Removes a pickup schedule identified by the given ID.")
        public ResponseEntity<Void> deleteSchedule(@Valid @PathVariable Long scheduleId) {
            service.deleteSchedule(scheduleId);
                return ResponseEntity.noContent().build();
        }

        
        @GetMapping("/{zoneId}")
        @Operation(summary = "Get pickup schedules by zone", description = "Fetches pickup schedules specific to a given zone ID.")
        public ResponseEntity<List<PickupSchedule>> getSchedulesByZoneId(@Valid @PathVariable Long zoneId) {
                return ResponseEntity.ok(service.getSchedulesByZoneId(zoneId));
        }
        @DeleteMapping("/zone/{zoneId}")
        public ResponseEntity<Void> deleteSchedulesByZone(@Valid @PathVariable Long zoneId) {
            service.deleteByZoneId(zoneId);
            return ResponseEntity.noContent().build();
        }
        @GetMapping("/notassigned")
        public ResponseEntity<List<PickupScheduleResponseDTO>> getAllSchedulesWithRoutesNotAssigned(){
        	return ResponseEntity.ok(service.getAllSchedulesWithRoutesNotAssigned());
        }
        @PostMapping("/vechiclesassigned")
        public void scheduleIsAssignedWithVehicles(@RequestBody Long zoneId) {
        	service.scheduleIsAssignedWithVehicles(zoneId);
        }
        
        @PostMapping("/newrouteassigned")
    	public void newRouteIsNotAssignedWithVehicleAndWorker(@RequestBody Long zoneId) {
        	service.newRouteIsNotAssignedWithVehicleAndWorker(zoneId);
        }
        
        @GetMapping("/workersnotassigned")
        public ResponseEntity<List<PickupScheduleResponseDTO>> getAllSchedulesWithWorkersNotAssignedForAllRoutes(){
        	return ResponseEntity.ok(service.getAllSchedulesWithWorkersNotAssignedForAllRoutes());
        }
        @PutMapping("/workersassigned")
        public void scheduleIsAssignedWithWorkers(@RequestBody Long zoneId) {
        	service.scheduleIsAssignedWithWorkers(zoneId);
        }
        
        //Modify this api
        @DeleteMapping("/api/v1/urbanclean/supervisor/schedules/zone/{zoneId}")
        public void notifyPickupServiceToDelete(@PathVariable Long zoneId) {
        	service.notifyPickupServiceToDelete(zoneId);
        }
        
        @GetMapping("/schedulescount")
        public Long numberOfSchedules() {
        	return service.numberOfSchedules();
        }
        
        @PutMapping("/schedulestatus")
    	public void updateScheduleStatus(@RequestBody Long zoneId) {
        	service.updateScheduleStatus(zoneId);
        }

}
