package com.example.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


public class AssignmentDto {
	
	// Private constructor to hide the implicit public one
    private AssignmentDto() {
        // This constructor is intentionally left empty.
        // It prevents direct instantiation of the AssignmentDto class.
    }
	
    @Data
    public static class AssignmentCreateRequest {
        @NotNull(message = "Vehicle ID cannot be null")
        private Long vehicleId;

//        @NotNull(message = "Route ID cannot be null")
//        private Long routeId;

        @NotNull(message = "Date assigned cannot be null")
        private LocalDate dateAssigned;
    }

    @Data
    public static class AssignmentUpdateRequest {
    	private Long vehicleId;
        private Long routeId;
        private LocalDate dateAssigned;
    }

    @Data
    public static class AssignmentResponse {
        private Long assignmentId;
        private Long vehicleId;
        private String vehicleRegistrationNo; 
        private Long routeId;
        private LocalDate dateAssigned;
        private String vehicleStatus;
    }
}
