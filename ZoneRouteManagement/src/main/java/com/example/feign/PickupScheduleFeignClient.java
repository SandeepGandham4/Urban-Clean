package com.example.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.security.FeignConfig;

@FeignClient(name = "PickupScheduling",configuration = FeignConfig.class)
public interface PickupScheduleFeignClient {
	@DeleteMapping("/api/v1/urbanclean/supervisor/schedules/zone/{zoneId}")
    public void notifyPickupServiceToDelete(@PathVariable Long zoneId);

	@PostMapping("/api/v1/urbanclean/supervisor/schedules/newrouteassigned")
	public void newRouteIsNotAssignedWithVehicleAndWorker(@RequestBody Long zoneId);
}
