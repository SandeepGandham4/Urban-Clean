package com.pickupscheduling.dto;

import lombok.Data;

@Data
public class RouteResponseDTO {
	private Long id;
	private String pathDetails;
	private String estimatedTime;
}
