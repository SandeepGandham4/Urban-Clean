package com.example.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.dto.RouteResponseDTO;
import com.example.security.FeignConfig;

@FeignClient(name="ZoneRouteManagement", configuration = FeignConfig.class)
public interface RouteServiceFeignClient {
	@GetMapping("/api/v1/urbanclean/admin/zones/{zoneId}/routes")
	public ResponseEntity<List<RouteResponseDTO>> getRoutesByZoneId(@PathVariable Long zoneId);
	
	@GetMapping("/api/v1/urbanclean/admin/zones/{zoneId}/routes/{routeId}")
	public ResponseEntity<RouteResponseDTO> getRoute(@PathVariable Long zoneId, @PathVariable Long routeId);
}
