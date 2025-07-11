package com.example.dto;

import lombok.AllArgsConstructor;

import lombok.Data;

import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerAssignmentResponseDTO {

	private Long workerId;

	private Long zoneId;

	private Long vehicleId;

	private Double weightCollected;

}
