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
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.ZoneDTO;
import com.example.dto.ZoneResponseDTO;
import com.example.service.ZoneService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Combination of controller and response body
@RestController
@RequestMapping("/api/v1/urbanclean/admin/zones")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Zone Management", description = "Operations related to zone management")
//@CrossOrigin(origins = "http://localhost:3000")
//@CrossOrigin(
//	    origins = "http://localhost:3000",
//	    allowedHeaders = {"Authorization", "Content-Type"},
//	    //methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
//	    allowCredentials = "true"
//	)
public class ZoneController {

    private final ZoneService zoneService;
    
    

    @Operation(summary = "Create a new zone", description = "Creates a new zone and returns the created entity")
    @PostMapping
    public ResponseEntity<ZoneResponseDTO> createZone(@Valid @RequestBody ZoneDTO zoneDTO) {
        log.info("Creating a new zone: {}", zoneDTO);
        ZoneResponseDTO zoneResponseDTO=zoneService.createZone(zoneDTO);
        return ResponseEntity.ok(zoneResponseDTO);
    }

    @Operation(summary = "Get all zones", description = "Retrieves all zones available in the system")
    @GetMapping("/allzones")
    public ResponseEntity<List<ZoneResponseDTO>> getAllZones() {
        log.info("Getting all zones.");
        return ResponseEntity.ok(zoneService.getAllZones());
    }

    @Operation(summary = "Get zone by ID", description = "Fetches details of a zone based on its ID")
    @GetMapping("/{zoneId}")
    public ResponseEntity getZoneById(@PathVariable Long zoneId) {
        log.info("Getting zone based on zone ID: " + zoneId);
        return ResponseEntity.ok(zoneService.getZoneById(zoneId));
    }

    @Operation(summary = "Update zone by ID", description = "Updates an existing zone with new details")
    @PutMapping("/{zoneId}")
    public ResponseEntity updateZone(@PathVariable Long zoneId, @Valid @RequestBody ZoneDTO zoneDTO) {
        log.info("Updating a zone with ID: " + zoneId);
        return ResponseEntity.ok(zoneService.updateZone(zoneId, zoneDTO));
    }

    @Operation(summary = "Delete zone by ID", description = "Deletes a zone based on its ID")
    @DeleteMapping("/{zoneId}")
    public ResponseEntity<String> deleteZone(@PathVariable Long zoneId) {
        log.info("Deleting zone with ID: " + zoneId);
        zoneService.deleteZone(zoneId);
        
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Zone deleted successfully");
    }
    
    @GetMapping("/routes")
    public Long numberOfRoutes() {
    	return zoneService.numberOfRoutes();
    }
}

