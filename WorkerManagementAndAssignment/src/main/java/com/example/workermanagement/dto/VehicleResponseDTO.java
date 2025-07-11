package com.example.workermanagement.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleResponseDTO {
	private Long assignmentId;
    private Long vehicleId;
    private String vehicleRegistrationNo; 
    private Long routeId;
    private LocalDate dateAssigned;
}
