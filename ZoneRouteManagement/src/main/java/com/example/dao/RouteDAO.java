package com.example.dao;

import java.util.List;

import com.example.entity.Route;

public interface RouteDAO {
    Route save(Route route);
    void deleteById(Long id);
    Route findById(Long id);
    List<Route> findByZoneId(Long zoneId);
    List<Route> findAll();
}
