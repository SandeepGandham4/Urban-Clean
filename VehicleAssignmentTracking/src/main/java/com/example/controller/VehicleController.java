package com.example.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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

import com.example.dto.VehicleDto;
import com.example.exception.InvalidRequestException;
//import com.example.service.AssignmentService;
import com.example.service.VehicleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/urbanclean/v1/admin/vehicles")
@RequiredArgsConstructor

public class VehicleController {

    
    private final VehicleService vehicleService;
    @PostMapping
    public ResponseEntity<VehicleDto.VehicleResponse> createVehicle(@Valid @RequestBody VehicleDto.VehicleCreateRequest request) {
        VehicleDto.VehicleResponse createdVehicle = vehicleService.createVehicle(request);
        return new ResponseEntity<>(createdVehicle, HttpStatus.CREATED);
    }
    
    @GetMapping("/activestatus")
    public ResponseEntity<Long> getCountOfActiveVehicles() {
        Long vehicles = vehicleService.getCountOfActiveVehicles();
        return ResponseEntity.ok(vehicles);
    } 

    @GetMapping
    public ResponseEntity<List<VehicleDto.VehicleResponse>> getAllVehicles() {
        List<VehicleDto.VehicleResponse> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }
    
    @GetMapping("/unassignedvehicles")
    public ResponseEntity<List<VehicleDto.VehicleResponse>> getUnassignedVehicles() {
        List<VehicleDto.VehicleResponse> vehicles = vehicleService.unassignedVehicles();
        return ResponseEntity.ok(vehicles);
    }
     
    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleDto.VehicleResponse> getVehicleById(@PathVariable Long vehicleId) {
        VehicleDto.VehicleResponse vehicle = vehicleService.getVehicleById(vehicleId);
        return ResponseEntity.ok(vehicle);
    }

    @GetMapping("/{vehicleId}/status")
    public ResponseEntity<VehicleDto.VehicleStatusResponse> getVehicleStatus(@PathVariable Long vehicleId) {
        VehicleDto.VehicleStatusResponse statusResponse = vehicleService.getVehicleStatus(vehicleId);
        return ResponseEntity.ok(statusResponse);
    }
    
 // DELETE /api/urbanclean/v1/vehicles/vehicleId
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<String> deleteVehicle(@PathVariable Long vehicleId) {
        try {
            vehicleService.deleteVehicle(vehicleId);
            return ResponseEntity.ok("Vehicle with ID " + vehicleId + " has been successfully deleted.");
        } catch (InvalidRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 

    @PutMapping("/{vehicleId}")
    public ResponseEntity<VehicleDto.VehicleResponse> updateVehicle(
            @PathVariable Long vehicleId,
            @Valid @RequestBody VehicleDto.VehicleUpdateRequest request) {
        VehicleDto.VehicleResponse updatedVehicle = vehicleService.updateVehicle(vehicleId, request);
        return ResponseEntity.ok(updatedVehicle);
    }

    @PutMapping("/{vehicleId}/status")
    public ResponseEntity<VehicleDto.VehicleResponse> updateVehicleLocationStatus(
            @PathVariable Long vehicleId,
            @Valid @RequestBody VehicleDto.VehicleStatusUpdateRequest request) {
        VehicleDto.VehicleResponse updatedVehicle = vehicleService.updateVehicleStatus(vehicleId, request);
        return ResponseEntity.ok(updatedVehicle);
    }
}
