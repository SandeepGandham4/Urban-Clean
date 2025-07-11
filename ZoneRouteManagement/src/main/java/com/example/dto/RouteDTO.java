package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class RouteDTO {
	@NotBlank(message = "Path details cannot be empty")
    @Size(max = 255, message = "Path details cannot exceed 255 characters")
    @Pattern(regexp = "^[a-zA-Z]{2,15}->+[a-zA-Z]{2,15}\\s#(?:[1-9]|[1-9][0-9]|100)km$",
    message = "Path details must be in format: 'source->destination #distancekm' (e.g., 'siruseri->chennai #20km')")
    private String pathDetails;  // Valid example: "siruseri->chennai #20km"
 
    @NotBlank(message = "Estimated time cannot be empty")
    @Pattern(regexp = "^(\\d+hr|\\d+min)$",
            message = "Estimated time must be in format: 'Xhr' or 'Xmin' (e.g., '1hr' or '30min')")
    private String estimatedTime;  // Valid examples: "1hr" or "30min"
 
    
}
