package com.example.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.security.FeignConfig;

@FeignClient(name = "workermanagementandassignment",configuration = FeignConfig.class)
public interface WorkerServiceFeignClient {
	@DeleteMapping("/assignments/zone/{zoneId}")
    public void deleteAssignmentOfWorkersByZoneId(@PathVariable Long zoneId);
}
