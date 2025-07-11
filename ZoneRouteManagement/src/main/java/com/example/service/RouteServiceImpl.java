
package com.example.service;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dao.RouteDAO;
import com.example.dao.ZoneDAO;
import com.example.dto.RouteDTO;
import com.example.dto.RouteResponseDTO;
import com.example.entity.Route;
import com.example.entity.Zone;
import com.example.exception.RouteNotFoundException;
import com.example.feign.PickupScheduleFeignClient;
import com.example.feign.VehicleServiceFeignClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Service
@Slf4j
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
	
	 
	private final RouteDAO routeDAO;

	private final ZoneDAO zoneDAO;
	
	private final VehicleServiceFeignClient vehicleServiceFeignClient;
	
	private final PickupScheduleFeignClient pickupScheduleFeignClient;

	@Override
	public RouteDTO createRoute(Long zoneId, RouteDTO dto) {
		Zone zone = zoneDAO.findById(zoneId);
		log.info("Creating a new Route under zone: "+zoneId +"with route details {}"+ dto);
		if (zone == null) {
			log.error("Zone not found with ID: {}", zoneId);
			throw new RouteNotFoundException("Zone not found with ID: " + zoneId);
		}

		Route route = new Route();
		route.setZone(zone);
		route.setPathDetails(dto.getPathDetails());
		route.setEstimatedTime(dto.getEstimatedTime());
		route = routeDAO.save(route);
		//dto.setId(route.getId());
		pickupScheduleFeignClient.newRouteIsNotAssignedWithVehicleAndWorker(zoneId);
		
		return dto;
	}

	@Override
	public Route updateRoute(Long zoneId, Long routeId, RouteDTO dto) {
		Route route = routeDAO.findById(routeId);
		log.info("Updating route ID: {} in zone ID: {}", routeId, zoneId);
		if (route == null || !route.getZone().getId().equals(zoneId)) {
			log.error("Route not found with ID: {} in zone ID: {}", routeId, zoneId);
			throw new RouteNotFoundException("Route not found with id: " + routeId + " in zone: " + zoneId);
		}

		route.setPathDetails(dto.getPathDetails());
		route.setEstimatedTime(dto.getEstimatedTime());
		routeDAO.save(route);
		log.debug("Route updated: {}", route);
		return route;
	}

	@Override
	@Transactional
	public String deleteRoute(Long zoneId, Long routeId) {        //Add a string return type to display the error message
		log.warn("Attempting to delete route ID: {} in zone ID: {}", routeId, zoneId);
		Route route = routeDAO.findById(routeId);
		if (route == null || !route.getZone().getId().equals(zoneId)) {
			log.error("Route not found with ID: {} in zone ID: {}", routeId, zoneId);
			throw new RouteNotFoundException("Route not found with id: " + routeId + " in zone: " + zoneId);
		}
		vehicleServiceFeignClient.notifyVehicleServiceToDeleteByRoute(routeId);
		routeDAO.deleteById(routeId);
		log.info("Route deleted with ID: {}", routeId);
		return "Route Successfully Deleted!";
		
	}

	@Override
	public RouteResponseDTO getRouteById(Long zoneId, Long routeId) {
		log.info("Fetching route ID: {} in zone ID: {}", routeId, zoneId);
		Route route = routeDAO.findById(routeId);
		if (route == null || !route.getZone().getId().equals(zoneId)) {
			log.error("Route not found with ID: {} in zone ID: {}", routeId, zoneId);
			throw new RouteNotFoundException("Zone doesnot exist" + zoneId);
		}

		RouteResponseDTO dto = new RouteResponseDTO();
		//dto.setId(route.getId());
		dto.setId(route.getId());
		dto.setPathDetails(route.getPathDetails());
		dto.setEstimatedTime(route.getEstimatedTime());
		log.debug("Route fetched: {}", dto);
		return dto;
	}

	@Override
	public List<RouteResponseDTO> getRoutesByZoneId(Long zoneId) {
		log.info("Fetching all routes in zone ID: {}", zoneId);
		List<Route> routes = routeDAO.findByZoneId(zoneId);
		
		if (routes.isEmpty()) {
			log.warn("No routes found in zone ID: {}", zoneId);
			//throw new RouteNotFoundException("Routes not found with id in zone: " + zoneId);
			return Collections.emptyList();
		}

		return routes.stream()
				.map(route -> new RouteResponseDTO(route.getId(),route.getPathDetails(),route.getEstimatedTime()))
				.collect(Collectors.toList());

	}
	@Override
	public List<RouteResponseDTO> getRoutes(){
		List<Route> routes=routeDAO.findAll();
		List<RouteResponseDTO> listOfRoutes=new ArrayList<>();
		for(Route r:routes) {
			RouteResponseDTO responsedto=new RouteResponseDTO();
			responsedto.setId(r.getId());
			responsedto.setPathDetails(r.getPathDetails());
			responsedto.setEstimatedTime(r.getEstimatedTime());
			listOfRoutes.add(responsedto);
		}
		return listOfRoutes;
	}
}
