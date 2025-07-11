package com.example.workermanagement.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.workermanagement.dto.VehicleAndWorkerResponseDTO;
import com.example.workermanagement.security.FeignConfig;

@FeignClient(name="wastelog",configuration = FeignConfig.class)
public interface WasteLogServiceFeignClient {
	@PostMapping("/api/v1/urbanclean/wastelog/admin/store")
	public void storeVehicleIdAndWorkers(@RequestBody VehicleAndWorkerResponseDTO vehicleAndWorkerResponseDTO);
	
	@DeleteMapping("/api/v1/urbanclean/wastelog/admin/vehicle/{vehicleId}")
	ResponseEntity<List<Long>> deleteLogDataOfWorkerAndVehicleId(@PathVariable("vehicleId") Long vehicleId);

	@PutMapping("/api/v1/urbanclean/wastelog/admin/vehicle/{prevVehicleId}/updateto/{newVehicleId}")
	ResponseEntity<List<Long>> updateLogDataOfWorkerAndVehicleId(@PathVariable("prevVehicleId") Long prevvehicleId,@PathVariable("newVehicleId") Long newvehicleId);
	
	@DeleteMapping("/api/v1/urbanclean/wastelog/worker/zones/{zoneId}")
	public void deleteLogDataOfWorkerByZoneId(@PathVariable Long zoneId);
}
