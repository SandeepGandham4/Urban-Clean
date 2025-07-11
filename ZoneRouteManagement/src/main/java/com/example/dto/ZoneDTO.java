package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class ZoneDTO {
	
	@NotBlank(message = "Zone name cannot be empty")
    @Size(max = 50, message = "Zone name cannot exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Zone name must contain only letters and spaces")
    @Schema(
            type = "string"
//            example = "North Zone",
//            description = "Zone name (letters and spaces only)",
//            maxLength = 50
        )
    private String name;
 
    @NotBlank(message = "Area coverage cannot be empty")
    @Pattern(regexp = "^\\d+km$", message = "Area coverage must be in format: number followed by 'km' (e.g., '10km')")
    private String areaCoverage;
   
}