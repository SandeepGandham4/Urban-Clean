package com.pickupscheduling.dto;

import com.pickupscheduling.enums.Frequency;
import com.pickupscheduling.enums.Status;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PickupScheduleRequestDTO {
	
	@NotNull(message = "Zone ID must not be null")
    @Positive(message = "Zone ID must be a positive number")
    private Long zoneId;
	
	@NotNull(message = "Zone Namer must not be null")
	private String zoneName;
	
	@NotNull(message = "Frequency must not be null")
    private Frequency frequency;
	
	@NotNull(message = "Time slot must not be null")
    @Size(min = 1, max = 50, message = "Time slot must be between 1 and 50 characters")
    private String timeSlot;
	
	 @NotNull(message = "Status must not be null")
    private Status status;
}
