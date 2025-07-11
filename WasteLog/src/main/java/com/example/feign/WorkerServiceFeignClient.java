package com.example.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dto.WorkerResponseDTO;
import com.example.security.FeignConfig;

@FeignClient(name="workermanagementandassignment", configuration = FeignConfig.class)
public interface WorkerServiceFeignClient {
	@GetMapping("/api/urbanclean/v1/admin/workers/email")
    public ResponseEntity<WorkerResponseDTO> getWorkerByIdByEmail(@RequestParam String email);
}
