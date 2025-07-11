package com.example.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.dto.PickupScheduleResponseDTO;
import com.example.security.FeignConfig;

@FeignClient(name="PickupScheduling", configuration = FeignConfig.class)
public interface PickupSchedulingFeignClient {
	@GetMapping("/api/v1/urbanclean/supervisor/schedules/notassigned")
	public ResponseEntity<List<PickupScheduleResponseDTO>> getAllSchedulesWithRoutesNotAssigned();
	
	@PostMapping("/api/v1/urbanclean/supervisor/schedules/vechiclesassigned")
	public void scheduleIsAssignedWithVehicles(@RequestBody Long zoneId);
	
	@PutMapping("/api/v1/urbanclean/supervisor/schedules/schedulestatus")
	public void updateScheduleStatus(@RequestBody Long zoneId);
	
	@PostMapping("/api/v1/urbanclean/supervisor/schedules/newrouteassigned")
	public void newRouteIsNotAssignedWithVehicleAndWorker(@RequestBody Long zoneId);
}
