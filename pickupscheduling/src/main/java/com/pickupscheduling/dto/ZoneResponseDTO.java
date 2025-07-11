package com.pickupscheduling.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZoneResponseDTO {
	@NotNull(message = "ID is required for updates")
    private Long id;
	private String name;
	private String areaCoverage;
}
