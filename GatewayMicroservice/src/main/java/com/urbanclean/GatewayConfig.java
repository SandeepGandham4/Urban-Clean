package com.urbanclean;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
	@Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
        	.route("AuthenticationModule", r -> r.path("/api/urbanclean/v1/auth/**")
                .uri("lb://AuthenticationModule"))  // Load-balanced service discovery
            .route("ZoneRouteManagement", r -> r.path("/api/v1/urbanclean/admin/zones/**","/api/v1/urbanclean/admin/zones/{zoneId}/routes/**")
                .uri("lb://ZoneRouteManagement"))  // Load-balanced service discovery
            .route("PickupScheduling", r -> r.path("/api/v1/urbanclean/supervisor/schedules/**")
                .uri("lb://PickupScheduling"))  // Load-balanced service discovery
            .route("VehicleAssignmentTracking", r -> r.path("/api/urbanclean/v1/admin/vehicleassignments/**","/api/urbanclean/v1/admin/vehicles/**")
                    .uri("lb://VehicleAssignmentTracking"))  // Load-balanced service discovery
            .route("workermanagementandassignment", r -> r.path("/api/urbanclean/v1/admin/workerassignments/**","/api/urbanclean/v1/admin/workers/**")
                    .uri("lb://workermanagementandassignment"))  // Load-balanced service discovery
            .route("wastelog", r -> r.path("/api/v1/urbanclean/wastelog/**")
                    .uri("lb://wastelog"))  // Load-balanced service discovery
            .build();
    }
}			
