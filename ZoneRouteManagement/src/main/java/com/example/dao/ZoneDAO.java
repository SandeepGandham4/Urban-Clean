package com.example.dao;

import java.util.List;

import com.example.entity.Zone;

public interface ZoneDAO {
    Zone save(Zone zone);
    void deleteById(Long id);
    Zone findById(Long id);
    List<Zone> findAll();
    Zone findByName(String name);
}
