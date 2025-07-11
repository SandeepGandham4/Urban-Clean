package com.example.workermanagement.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleAndWorkerResponseDTO {
	private List<AssignmentResponseDTO> listOfWorkersAssigned;
	private Long vehicleId;
}
