package com.example.dto;

import com.example.enums.VehicleStatus;
import com.example.enums.VehicleType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;



public class VehicleDto {
	
	// Private constructor to hide the implicit public one
    private VehicleDto() {
        // This constructor is intentionally left empty.
        // It prevents direct instantiation of the AssignmentDto class.
    }

    @Data
    public static class VehicleCreateRequest {
        @NotBlank(message = "Registration number cannot be blank")
        @Size(min = 3, max = 20, message = "Registration number must be between 3 and 20 characters")
        @Pattern(regexp = "^[A-Z]{2}\\d{2}[A-Z]{2}\\d{4}$", message = "Registration number must be in format: AP31PQ1234")
        private String registrationNo;
 
        private VehicleType type;
 
 
        private VehicleStatus status; // Initial status be "Available"
    }

    @Data
    public static class VehicleUpdateRequest {
        
        @Size(min = 3, max = 20, message = "Registration number must be between 3 and 20 characters")
        private String registrationNo;

        private VehicleType type;
        
        private VehicleStatus status;
    }

    @Data
    public static class VehicleStatusUpdateRequest {
        @NotBlank(message = "New status cannot be blank")
        @Size(min = 3, max = 512, message = "Status must be between 3 and 512 characters")
        private VehicleStatus newStatus; 
    }

    @Data
    public static class VehicleResponse { 
        private Long vehicleId;
        private String registrationNo;
        private VehicleType type;
        private VehicleStatus status;
    }
    
    @Data
    public static class VehicleStatusResponse { 
        private VehicleStatus status;
    }
}
