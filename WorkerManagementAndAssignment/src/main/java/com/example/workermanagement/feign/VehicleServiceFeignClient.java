package com.example.workermanagement.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.workermanagement.dto.VehicleResponseDTO;
import com.example.workermanagement.security.FeignConfig;

@FeignClient(name="VehicleAssignmentTracking",configuration = FeignConfig.class)
public interface VehicleServiceFeignClient {
	@GetMapping("/api/urbanclean/v1/admin/vehicleassignments/workerassignment")
	ResponseEntity<List<VehicleResponseDTO>> getVehiclesOfWorkersNotAssigned();
	
	@PutMapping("/api/urbanclean/v1/admin/vehicleassignments/worker/assigned")
	public void workerIsAssignedWithVehicles(@RequestBody Long vehicleId);
}
