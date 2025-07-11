package com.example.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WasteLogResponseDTO {
	
	private Long logId;
	
	private Long zoneId;
	
	private Long vehicleId;
	
	private Long workerId;
	
	private Double weightCollected;
	private LocalDateTime collectionTime;
}
