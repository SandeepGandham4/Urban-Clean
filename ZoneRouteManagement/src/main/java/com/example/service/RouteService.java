package com.example.service;

import java.util.List;

import com.example.dto.RouteDTO;
import com.example.dto.RouteResponseDTO;
import com.example.entity.Route;

public interface RouteService {
    RouteDTO createRoute(Long zoneId, RouteDTO routeDTO);
    Route updateRoute(Long zoneId, Long routeId, RouteDTO routeDTO);
    String deleteRoute(Long zoneId, Long routeId);
    RouteResponseDTO getRouteById(Long zoneId, Long routeId);
    List<RouteResponseDTO> getRoutesByZoneId(Long zoneId);
    List<RouteResponseDTO> getRoutes();
}
	