package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteResponseDTO {
	private Long id;
	private String pathDetails;
	private String estimatedTime;
}
