package com.example.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.entity.Zone;
import com.example.repository.ZoneRepository;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Repository
public class ZoneDAOImpl implements ZoneDAO {

   
    private final ZoneRepository zoneRepository;

    @Override
    public Zone save(Zone zone) {
        return zoneRepository.save(zone);
    }

    @Override
    public void deleteById(Long id) {
        zoneRepository.deleteById(id);
    }

    @Override
    public Zone findById(Long id) {
        return zoneRepository.findById(id).orElse(null);
    }

    @Override
    public List<Zone> findAll() {
        return zoneRepository.findAll();
    }

    @Override
    public Zone findByName(String name) {
        return zoneRepository.findByName(name);
    }
}
