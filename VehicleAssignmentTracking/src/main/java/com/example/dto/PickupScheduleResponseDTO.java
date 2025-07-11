package com.example.dto;

import com.example.enums.Frequency;
import com.example.enums.Status;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PickupScheduleResponseDTO {
	
	@NotNull(message = "Schedule ID must not be null")
    @Positive(message = "Schedule ID must be a positive number")
	private Long scheduleId;
	
	@NotNull(message = "Zone ID must not be null")
    @Positive(message = "Zone ID must be a positive number")
    private Long zoneId;
	
	private String zoneName;
	
	@NotNull(message = "Frequency must not be null")
    private Frequency frequency;
	
	@NotNull(message = "Time slot must not be null")
    @Size(min = 1, max = 50, message = "Time slot must be between 1 and 50 characters")
    private String timeSlot;
	
	 @NotNull(message = "Status must not be null")
    private Status status;
}
