package com.example.service;



import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dao.ZoneDAO;
import com.example.dto.ZoneDTO;
import com.example.dto.ZoneResponseDTO;
import com.example.entity.Route;
import com.example.entity.Zone;
import com.example.exception.ZoneNotFoundException;
import com.example.feign.PickupScheduleFeignClient;
import com.example.feign.VehicleServiceFeignClient;
import com.example.feign.WorkerServiceFeignClient;
import com.example.repository.RouteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ZoneServiceImpl implements ZoneService {

	private final PickupScheduleFeignClient pickupScheduleFeignClient;
	private final VehicleServiceFeignClient vehicleServiceFeignClient;
	private final WorkerServiceFeignClient workerServiceFeignClient;
	private final RouteRepository routeRepository;
    private final ZoneDAO zoneDAO;
    @Override
    public ZoneResponseDTO createZone(ZoneDTO dto) 
    {
    	log.info("Creating zone: {}", dto);
    	Zone zone = new Zone();
    	
 	if(Objects.nonNull(zoneDAO.findByName(dto.getName()))) {
   		log.warn("Zone already exists with name: {}", dto.getName());
   		throw new ZoneNotFoundException("Zone already found ");
   	}
        zone.setName(dto.getName());
        zone.setAreaCoverage(dto.getAreaCoverage());
        zone = zoneDAO.save(zone);

        log.debug("Zone created with ID: {}", zone.getId());
        ZoneResponseDTO responsedto= new ZoneResponseDTO();
        responsedto.setId(zone.getId());
        responsedto.setName(dto.getName());
        responsedto.setAreaCoverage(dto.getAreaCoverage());
        return responsedto;
    }

    @Override
    public Zone updateZone(Long id, ZoneDTO dto) {
    	log.info("Updating zone with ID: {}", id);
        Zone zone = zoneDAO.findById(id);
        if (zone == null) {
        	log.error("Zone not found with ID: {}", id);
            throw new ZoneNotFoundException("Zone not found with id: " + id);
        }
        if(Objects.nonNull(zoneDAO.findByName(dto.getName()))) {
        	log.warn("Zone already exists with name: {}", dto.getName());
    		throw new ZoneNotFoundException("Zone already found with name: " + dto.getName());
    	}
        zone.setName(dto.getName());
        zone.setAreaCoverage(dto.getAreaCoverage());
        zoneDAO.save(zone);
        log.debug("Zone updated: {}",zone);
        return zone;
    }

    @Override
    @Transactional
    public void deleteZone(Long id) {
    	log.warn("Attempting to delete zone with ID: {}", id);
        if (zoneDAO.findById(id)==null) {
        	log.error("Zone not found with ID: {}", id);
            throw new ZoneNotFoundException("Zone not found with id: " + id);
        }
        if(!routeRepository.findByZoneId(id).isEmpty()) {
        pickupScheduleFeignClient.notifyPickupServiceToDelete(id);      
        }
        zoneDAO.deleteById(id);
        log.info("Zone deleted with ID: {}", id);
    }

    @Override
    public ZoneDTO getZoneById(Long id) {
    	log.info("Fetching zone with ID: {}", id);
        Zone zone = zoneDAO.findById(id);
         
        if (zone == null) {
        	log.error("Zone not found with ID: {}", id);
            throw new ZoneNotFoundException("Zone not found with id: " + id);
        }
        ZoneDTO dto = new ZoneDTO();
        dto.setName(zone.getName());
        dto.setAreaCoverage(zone.getAreaCoverage());
        log.debug("Zone fetched: {}", dto);
        return dto;
    }

    @Override
    public List<ZoneResponseDTO> getAllZones() {
    	log.info("Fetching all zones");
    	List<Zone> zones=zoneDAO.findAll();
    	
    	if(zones.isEmpty()) {
    		log.warn("No zones found");

    		throw new ZoneNotFoundException("No zones existed");
    	}

    	List<ZoneResponseDTO> responsedto=new ArrayList<>();
    	for(Zone zone:zones) {
    		ZoneResponseDTO z=new ZoneResponseDTO();
    		z.setId(zone.getId());
    		z.setName(zone.getName());
    		z.setAreaCoverage(zone.getAreaCoverage());
    		responsedto.add(z);
    	}		 
    	log.debug("Total zones found: {}", zones.size());
        return responsedto;
    }
    
    public Long numberOfRoutes() {
    	List<Route> count=routeRepository.findAll();
    	return (long) count.size();
    }
}
