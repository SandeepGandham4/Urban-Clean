package com.urbanclean.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.urbanclean.dto.WorkerDTO;
import com.urbanclean.dto.WorkerResponseDTO;
import com.urbanclean.security.FeignConfig;

@FeignClient(name="workermanagementandassignment", configuration = FeignConfig.class)
public interface WorkerServiceFeignClient {
	@PostMapping("/api/urbanclean/v1/admin/workers")
	public ResponseEntity<WorkerResponseDTO> createWorker(@RequestBody WorkerDTO workerDTO);

}
