package com.pickupscheduling.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.pickupscheduling.dto.RouteResponseDTO;
import com.pickupscheduling.dto.ZoneResponseDTO;
import com.pickupscheduling.security.FeignConfig;


@FeignClient(name="ZoneRouteManagement" , configuration = FeignConfig.class)
public interface ZoneServiceFeignClient {
	@GetMapping("/api/v1/urbanclean/admin/zones/allzones")
	public ResponseEntity<List<ZoneResponseDTO>> getAllZones();
	
	@GetMapping("/api/v1/urbanclean/admin/zones/{zoneId}/routes")
	public ResponseEntity<List<RouteResponseDTO>> getRoutesByZoneId(@PathVariable Long zoneId);
	
	@GetMapping("/api/v1/urbanclean/admin/zones/{zoneId}/routes/{routeId}")
	public ResponseEntity<RouteResponseDTO> getRoute(@PathVariable Long zoneId, @PathVariable Long routeId);
	
	@DeleteMapping("/api/v1/urbanclean/admin/zones/{zoneId}/routes/{routeId}")
    public ResponseEntity<String> deleteRoute(@PathVariable Long zoneId, @PathVariable Long routeId);
}
