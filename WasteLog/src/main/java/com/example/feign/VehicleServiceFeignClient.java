package com.example.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.security.FeignConfig;

@FeignClient(name="VehicleAssignmentTracking", configuration = FeignConfig.class)
public interface VehicleServiceFeignClient {
	@PutMapping("/api/urbanclean/v1/admin/vehicleassignments/vehicle/updatestatus")
	String updateVehicleStatus(@RequestBody Long vehicleId);
	
	@PutMapping("/api/urbanclean/v1/admin/vehicleassignments/vehicle/{vehicleId}/wastelogged")
	void isWasteLogged(@PathVariable Long vehicleId,@RequestBody Long zoneId);
	
	@GetMapping("/api/urbanclean/v1/admin/vehicleassignments/vehicle/getstatus")
    public String getVehicleStatus(@RequestParam("vehicleId") Long vehicleId);
 
}



