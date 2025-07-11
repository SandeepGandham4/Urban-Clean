package com.example.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {
	
	List<Route> findByZoneId(Long zoneId);

}
