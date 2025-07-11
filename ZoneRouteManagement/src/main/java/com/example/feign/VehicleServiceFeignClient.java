package com.example.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.security.FeignConfig;

@FeignClient(name = "VehicleAssignmentTracking",configuration = FeignConfig.class)
public interface VehicleServiceFeignClient {
	@DeleteMapping("/api/urbanclean/v1/admin/vehicleassignments/routes/{routeId}")
	public String notifyVehicleServiceToDeleteByRoute(@PathVariable Long routeId);
	
	@DeleteMapping("/api/urbanclean/v1/admin/vehicleassignments/routes/delete")
	void notifyVehicleServiceToDeleteByZone(@RequestBody List<Long> listOfRoutes);
	
}
