package com.example.workermanagement.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.workermanagement.dto.PickupScheduleResponseDTO;
import com.example.workermanagement.security.FeignConfig;

@FeignClient(name="PickupScheduling",configuration = FeignConfig.class)
public interface PickupSchedulingFeignClient {
	@GetMapping("/api/v1/urbanclean/supervisor/schedules/workersnotassigned")
	ResponseEntity<List<PickupScheduleResponseDTO>> getAllSchedulesWithWorkersNotAssignedForAllRoutes();
	
	@PutMapping("/api/v1/urbanclean/supervisor/schedules/workersassigned")
	public void scheduleIsAssignedWithWorkers(@RequestBody Long zoneId);
}
