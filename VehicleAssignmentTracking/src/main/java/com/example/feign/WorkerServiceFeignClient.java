package com.example.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.example.security.FeignConfig;

@FeignClient(name="workermanagementandassignment", configuration = FeignConfig.class)
public interface WorkerServiceFeignClient {
	@DeleteMapping("/api/urbanclean/v1/admin/workerassignments/assignments/{vehicleId}")
	public void deleteAssignmentOfWorkersByVehicleId(@PathVariable Long vehicleId);
	
	@PutMapping("/api/urbanclean/v1/admin/workerassignments/assignments/{prevVehicleId}/updateto/{newVehicleId}")
	public void updateAssignmentOfWorkersByVehicleId(@PathVariable Long prevVehicleId,@PathVariable Long newVehicleId);
}