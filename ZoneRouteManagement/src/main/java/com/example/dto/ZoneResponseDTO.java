package com.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
public class ZoneResponseDTO {
	@NotNull(message = "ID is required for updates")
    private Long id;
	private String name;
	private String areaCoverage;
	
}
