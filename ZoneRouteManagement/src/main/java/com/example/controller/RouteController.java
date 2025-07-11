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

import com.example.dto.RouteDTO;
import com.example.dto.RouteResponseDTO;
import com.example.service.RouteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/urbanclean/admin/zones/{zoneId}/routes")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Route Management", description = "Operations related to managing routes in urban clean zones")
//@CrossOrigin(origins = "http://localhost:3000")
//@CrossOrigin(
//	    origins = "http://localhost:3000",
//	    allowedHeaders = {"Authorization", "Content-Type"},
//	    //methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
//	    allowCredentials = "true"
//	)
public class RouteController {

    
    private final RouteService routeService;

    @Operation(summary = "Create a new route", description = "Creates a new route under a zone and returns the created route")
    @PostMapping
    public ResponseEntity<RouteDTO> createRoute(@PathVariable Long zoneId, @Valid @RequestBody RouteDTO routeDTO) {
        RouteDTO createdRoute = routeService.createRoute(zoneId, routeDTO);
        return ResponseEntity.ok(createdRoute);
    }
    
    @Operation(summary = "Get all routes", description = "Retrieves all routes available in a specific zone")
    @GetMapping
    public ResponseEntity<List<RouteResponseDTO>> getRoutesByZoneId(@PathVariable Long zoneId) {
        log.info("Getting routes under zone: " + zoneId + " with route details.");
        return ResponseEntity.ok(routeService.getRoutesByZoneId(zoneId));
    }

    @Operation(summary = "Get route by ID", description = "Fetches details of a specific route under a zone")
    @GetMapping("/{routeId}")
    public ResponseEntity<RouteResponseDTO> getRoute(@PathVariable Long zoneId, @PathVariable Long routeId) {
        log.info("Fetching data of route with ID: " + routeId + " under the zone ID: " + zoneId);
        RouteResponseDTO routeResponseDTO=routeService.getRouteById(zoneId, routeId);
        return ResponseEntity.ok(routeResponseDTO);
    }

    @Operation(summary = "Update route by ID", description = "Updates a route with new details")
    @PutMapping("/{routeId}")
    public ResponseEntity<?> updateRoute(@PathVariable Long zoneId, @PathVariable Long routeId, @Valid @RequestBody RouteDTO routeDTO) {
        log.info("Updating data of route with ID: " + routeId + " under the zone ID: " + zoneId + " route details {}", routeDTO);
        return ResponseEntity.ok(routeService.updateRoute(zoneId, routeId, routeDTO));
    }

    @Operation(summary = "Delete route by ID", description = "Deletes a route from a zone")
    @DeleteMapping("/{routeId}")
    public ResponseEntity<String> deleteRoute(@PathVariable Long zoneId, @PathVariable Long routeId) {
        log.info("Deleting route with ID: " + routeId + " under zone ID: " + zoneId);
        String result=routeService.deleteRoute(zoneId, routeId);
        return ResponseEntity.ok(result);
    }
}

