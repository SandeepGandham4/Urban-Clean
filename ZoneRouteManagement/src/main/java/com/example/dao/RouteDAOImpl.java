package com.example.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.entity.Route;
import com.example.repository.RouteRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class RouteDAOImpl implements RouteDAO {

   
    private final RouteRepository routeRepository;

    @Override
    public Route save(Route route) {
        return routeRepository.save(route);
    }

    @Override
    public void deleteById(Long id) {
        routeRepository.deleteById(id);
    }

    @Override
    public Route findById(Long id) {
        return routeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Route> findByZoneId(Long zoneId) {
        return routeRepository.findByZoneId(zoneId);
    }
    @Override
    public List<Route>findAll(){
    	return routeRepository.findAll();
    }
}
