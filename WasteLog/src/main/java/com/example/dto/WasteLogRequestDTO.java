package com.example.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WasteLogRequestDTO {
	@NotNull(message="zone id is required")
	private Long zoneId;
	@NotNull(message="vehicle id is required")
	private Long vehicleId;
	@NotNull(message="worker id is required")
	private Long workerId;
	@NotNull(message="weight collected is required")
	private Double weightCollected;
	
	private LocalDateTime collectionTime;
}
