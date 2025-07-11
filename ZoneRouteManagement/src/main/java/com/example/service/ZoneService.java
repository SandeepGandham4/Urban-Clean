package com.example.service;

import java.util.List;

import com.example.dto.ZoneDTO;
import com.example.dto.ZoneResponseDTO;
import com.example.entity.Zone;

public interface ZoneService {
    public ZoneResponseDTO createZone(ZoneDTO zoneDTO);
    public Zone updateZone(Long zoneId, ZoneDTO zoneDTO);
    public void deleteZone(Long zoneId);
    public ZoneDTO getZoneById(Long zoneId);
    public List<ZoneResponseDTO> getAllZones();
	public Long numberOfRoutes();
}
