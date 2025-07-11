package com.example.workermanagement.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.workermanagement.dto.RouteResponseDTO;

@FeignClient(name="ZoneRouteManagement")
public interface ZoneServiceFeignClient {
	@GetMapping("/api/v1/urbanclean/admin/zones/{zoneId}/routes")
	public ResponseEntity<List<RouteResponseDTO>> getRoutesByZoneId(@PathVariable Long zoneId);
	
}
