package com.pickupscheduling.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.pickupscheduling.dto.PickupScheduleRequestDTO;
import com.pickupscheduling.dto.PickupScheduleResponseDTO;
import com.pickupscheduling.dto.ZoneResponseDTO;
import com.pickupscheduling.entity.PickupSchedule;

public interface PickupScheduleService {
	PickupScheduleResponseDTO createSchedule(PickupScheduleRequestDTO pickupScheduleDTO);

	List<PickupScheduleResponseDTO> getAllSchedules();

	PickupSchedule updateSchedule(Long scheduleId,PickupScheduleRequestDTO pickupScheduleDTO);

	void deleteSchedule(Long scheduleId);

	List<PickupSchedule> getSchedulesByZoneId(Long zoneId);
	
	List<ZoneResponseDTO> newlyAddedZones();
	
	void deleteByZoneId(Long zoneId);
	
	List<PickupScheduleResponseDTO> getAllSchedulesWithRoutesNotAssigned();
	
	void scheduleIsAssignedWithVehicles(Long scheduleId);
	
    List<PickupScheduleResponseDTO> getAllSchedulesWithWorkersNotAssignedForAllRoutes();
    
    void scheduleIsAssignedWithWorkers(@RequestBody Long scheduleId);
    
    void notifyPickupServiceToDelete(@PathVariable Long zoneId);
    
    void newRouteIsNotAssignedWithVehicleAndWorker(@RequestBody Long zoneId);

	Long numberOfSchedules();

	void updateScheduleStatus(@RequestBody Long zoneId);
}
