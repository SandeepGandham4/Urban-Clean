package com.pickupscheduling.serviceimpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.pickupscheduling.dto.PickupScheduleRequestDTO;
import com.pickupscheduling.dto.PickupScheduleResponseDTO;
import com.pickupscheduling.dto.RouteResponseDTO;
import com.pickupscheduling.dto.ZoneResponseDTO;
import com.pickupscheduling.entity.PickupSchedule;
import com.pickupscheduling.enums.Status;
import com.pickupscheduling.exception.ResourceNotFoundException;
import com.pickupscheduling.exception.ZoneNotFoundException;
import com.pickupscheduling.feign.VehicleServiceFeignClient;
import com.pickupscheduling.feign.ZoneServiceFeignClient;
import com.pickupscheduling.repository.PickupScheduleRepository;
import com.pickupscheduling.service.PickupScheduleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PickupScheduleServiceImpl implements PickupScheduleService {
	
	private final PickupScheduleRepository pickupScheduleRepository;
	private final ZoneServiceFeignClient zoneServiceFeignClient;
	private final VehicleServiceFeignClient vehicleServiceFeignClient;
	
	@Override
	public List<ZoneResponseDTO> newlyAddedZones(){
		log.info("Fetching all newly created zones");
		List<ZoneResponseDTO> newZones=zoneServiceFeignClient.getAllZones().getBody();

        if (newZones == null || newZones.isEmpty()) {
        	  log.warn("No Zones Found");
        	  throw new ZoneNotFoundException("No Zones Existed");
        }
        List<PickupSchedule> scheduledZoneIds = pickupScheduleRepository.findAll();
        List<Long> l= new ArrayList<>();
        for(PickupSchedule s:scheduledZoneIds) {
        	l.add(s.getZoneId());
        }
        
        return newZones.stream()
                .filter(zone -> !l.contains(zone.getId()))
                .collect(Collectors.toList());
	}

	@Override
	public PickupScheduleResponseDTO createSchedule(PickupScheduleRequestDTO pickupScheduleDTO) {
		PickupSchedule schedule = new PickupSchedule();
		
		if(pickupScheduleRepository.findByZoneId(pickupScheduleDTO.getZoneId()).isEmpty()) {
		schedule.setZoneId(pickupScheduleDTO.getZoneId());
		schedule.setZoneName(pickupScheduleDTO.getZoneName()); 
		schedule.setFrequency(pickupScheduleDTO.getFrequency());
		schedule.setTimeSlot(pickupScheduleDTO.getTimeSlot());
		schedule.setStatus(pickupScheduleDTO.getStatus());
		pickupScheduleRepository.save(schedule);
		log.info("Successfully created a schedule with ID: {}", schedule.getScheduleId());
		return new PickupScheduleResponseDTO(schedule.getScheduleId(),schedule.getZoneId(),schedule.getZoneName(),schedule.getFrequency(),schedule.getTimeSlot(),schedule.getStatus());
		}else {
			log.error("A Schedule for this Zone already exists!");
			throw new ZoneNotFoundException("A Schedule for this Zone already exists!");
		}
	}

	@Override
	public List<PickupScheduleResponseDTO> getAllSchedules() {
		List<PickupSchedule> schedules = pickupScheduleRepository.findAll();
		List<PickupScheduleResponseDTO> listOfAllSchedules= schedules.stream()
				.map(schedule ->new PickupScheduleResponseDTO(schedule.getScheduleId(),schedule.getZoneId(),schedule.getZoneName(),schedule.getFrequency(),schedule.getTimeSlot(),schedule.getStatus()))
				.collect(Collectors.toList());
		if (listOfAllSchedules.isEmpty()) {
			log.error("No schedules found for the zone");
			throw new ResourceNotFoundException("Schedule not found for the zone");
		}
		log.info("All schedules: ", listOfAllSchedules);
		return listOfAllSchedules;
	}

	@Override
	public PickupSchedule updateSchedule(Long scheduleId, PickupScheduleRequestDTO pickupScheduleDTO) {
		Optional<PickupSchedule> optionalSchedule = pickupScheduleRepository.findById(scheduleId);
		if (optionalSchedule.isPresent()) {
			PickupSchedule schedule = optionalSchedule.get();
			schedule.setZoneId(pickupScheduleDTO.getZoneId());
			schedule.setFrequency(pickupScheduleDTO.getFrequency());
			schedule.setTimeSlot(pickupScheduleDTO.getTimeSlot());
			schedule.setStatus(pickupScheduleDTO.getStatus());
			schedule.setZoneName(pickupScheduleDTO.getZoneName()); 
			pickupScheduleRepository.save(schedule);
			log.info("Successfully updated a schedule with ID: {}", schedule.getScheduleId());
			return schedule;
		} else {
			log.error("Schedule not found");
			throw new ResourceNotFoundException("Schedule not found");
		}
	}
		//public void notifyPickupServiceToDelete(Long zoneId) {
//	    PickupSchedule schedule = pickupScheduleRepository.findByZoneId(zoneId).orElse(null);
//	    if (schedule != null && 
//	       (schedule.getStatus() == Status.COMPLETED || schedule.getStatus() == Status.SCHEDULED)) {
//	    	try {
//	        List<RouteResponseDTO> routes = zoneServiceFeignClient.getRoutesByZoneId(zoneId).getBody();
////	        System.out.println("Status: " + routes.getStatusCode());
////	        System.out.println("Headers: " + routes.getHeaders());
////	        System.out.println("Body: " + routes.getBody());
//	        if(!routes.isEmpty() && routes!=null) {
//	        List<Long> listOfRoutes = new ArrayList<>();
//	        for (RouteResponseDTO route : routes) {
//	            listOfRoutes.add(route.getId());
//	        }
//	        vehicleServiceFeignClient.notifyVehicleServiceToDeleteByZone(listOfRoutes);
//	        pickupScheduleRepository.deleteByZoneId(zoneId);
//	        }
//	    	} catch(Exception ex) {
//	    	        log.warn("No routes found for zone ID {}. Skipping vehicle cleanup.", zoneId);
//	        }
//
//	    }
//	}

	@Override
	public void deleteSchedule(Long scheduleId) {
		Optional<PickupSchedule> optionalSchedule = pickupScheduleRepository.findById(scheduleId);
		if (optionalSchedule.isPresent()) {
			log.info("Successfully deleted a schedule with ID: {}", scheduleId);
			List<RouteResponseDTO> routes = zoneServiceFeignClient.getRoutesByZoneId(optionalSchedule.get().getZoneId()).getBody();
	        List<Long> listOfRoutes = new ArrayList<>();
	        for (RouteResponseDTO route : routes) {
	            listOfRoutes.add(route.getId());
	        }
	        System.out.println("listofroutes"+listOfRoutes);
	        vehicleServiceFeignClient.notifyVehicleServiceToDeleteByZone(listOfRoutes);
	        System.out.println("Vehicles");
	        pickupScheduleRepository.deleteById(scheduleId);
		} else {
			log.error("Schedule not found with ID: {}", scheduleId + " to delete");
			throw new ResourceNotFoundException("Schedule not found");
		}

	}
	@Override
	public void deleteByZoneId(Long zoneId) {
            pickupScheduleRepository.deleteByZoneId(zoneId);
			log.info("Successfully deleted a schedule with ID: {}", zoneId);
			List<RouteResponseDTO> routes = zoneServiceFeignClient.getRoutesByZoneId(zoneId).getBody();
	        List<Long> listOfRoutes = new ArrayList<>();
	        for (RouteResponseDTO route : routes) {
	            listOfRoutes.add(route.getId());
	        }

	        vehicleServiceFeignClient.notifyVehicleServiceToDeleteByZone(listOfRoutes);
	}

	@Override
	public List<PickupSchedule> getSchedulesByZoneId(Long zoneId) {
		List<PickupSchedule> zoneSchedules = pickupScheduleRepository.findAllByZoneId(zoneId);
		if (zoneSchedules.isEmpty()) {
			log.error("Schedule not found for the zone : {}", zoneId);
			throw new ResourceNotFoundException("Schedule not found for the zone" + zoneId);
		}
		log.info("Schedules present in Zone ID: {}", zoneId, " : ", zoneSchedules);
		return zoneSchedules;
	}
	@Override
	public List<PickupScheduleResponseDTO> getAllSchedulesWithRoutesNotAssigned(){
		if(pickupScheduleRepository.findAll().isEmpty()) {
			throw new ResourceNotFoundException("No Schedules Existed");
		}
		if(pickupScheduleRepository.count()==pickupScheduleRepository.findAllByAllRoutesAssigned(true).size()) {
			throw new ResourceNotFoundException("All Schedules are assigned with routes");
		}
		List<PickupSchedule> schedules=pickupScheduleRepository.findAllByAllRoutesAssigned(false);
		return schedules.stream()
				.map(schedule -> new PickupScheduleResponseDTO(schedule.getScheduleId(),schedule.getZoneId(),schedule.getZoneName(),schedule.getFrequency(),schedule.getTimeSlot(),schedule.getStatus()))
				.collect(Collectors.toList());
	}
	
	@Override
	public List<PickupScheduleResponseDTO> getAllSchedulesWithWorkersNotAssignedForAllRoutes(){
		if(pickupScheduleRepository.findAll().isEmpty()) {
			throw new ResourceNotFoundException("No Schedules Existed");
		}
		if(pickupScheduleRepository.count()==pickupScheduleRepository.findAllByAllWorkersAreAssigned(true).size()) {
			throw new ResourceNotFoundException("All Workers are assigned with the schedules");
		}
		List<PickupSchedule> schedules=pickupScheduleRepository.findAllByAllWorkersAreAssigned(false);
		return schedules.stream()
				.map(schedule -> new PickupScheduleResponseDTO(schedule.getScheduleId(),schedule.getZoneId(),schedule.getZoneName(),schedule.getFrequency(),schedule.getTimeSlot(),schedule.getStatus()))
				.collect(Collectors.toList());
	}
	
	@Override
	public void scheduleIsAssignedWithVehicles(Long zoneId) {
		PickupSchedule schedule=pickupScheduleRepository.findByZoneId(zoneId).orElse(null);
		if(schedule!=null) {
			schedule.setAllRoutesAssigned(true);
			schedule.setAllWorkersAreAssigned(false);
			pickupScheduleRepository.save(schedule);
		}
	}
	
	@Override
	public void newRouteIsNotAssignedWithVehicleAndWorker(Long zoneId) {
		PickupSchedule schedule=pickupScheduleRepository.findByZoneId(zoneId).orElse(null);
		if(schedule!=null) {
			schedule.setAllRoutesAssigned(false);
			schedule.setAllWorkersAreAssigned(false);
			pickupScheduleRepository.save(schedule);
		}
	}
	
	@Override
	public void scheduleIsAssignedWithWorkers(Long zoneId) {
		PickupSchedule schedule=pickupScheduleRepository.findByZoneId(zoneId).orElse(null);
		if(schedule!=null) {
			schedule.setAllWorkersAreAssigned(true);
			pickupScheduleRepository.save(schedule);
		}
	}
	@Override
	@Transactional
	public void notifyPickupServiceToDelete(Long zoneId) {
	    PickupSchedule schedule = pickupScheduleRepository.findByZoneId(zoneId).orElse(null);
	    if (schedule != null && 
	       (schedule.getStatus() == Status.COMPLETED || schedule.getStatus() == Status.SCHEDULED)) {
	    	try {
	        List<RouteResponseDTO> routes = zoneServiceFeignClient.getRoutesByZoneId(zoneId).getBody();
//	        System.out.println("Status: " + routes.getStatusCode());
//	        System.out.println("Headers: " + routes.getHeaders());
//	        System.out.println("Body: " + routes.getBody());
	        if(!routes.isEmpty() && routes!=null) {
	        List<Long> listOfRoutes = new ArrayList<>();
	        for (RouteResponseDTO route : routes) {
	            listOfRoutes.add(route.getId());
	        }
	        vehicleServiceFeignClient.notifyVehicleServiceToDeleteByZone(listOfRoutes);
	        pickupScheduleRepository.deleteByZoneId(zoneId);
	        }
	    	} catch(Exception ex) {
	    		System.out.println("Hloo ");
	    	        log.warn("No routes found for zone ID {}. Skipping vehicle cleanup.", zoneId);
	        }

	    }
	}

	public Long numberOfSchedules() {
		List<PickupSchedule> count=pickupScheduleRepository.findAll();
		return (long) count.size();
	}
	
	
	@Scheduled(cron = "0 0 0 * * *")
	public void assignTodaySchedules() {
		String timeSlot = LocalDate.now().toString();
		List<PickupSchedule> todaySchedules = pickupScheduleRepository.findByTimeSlot(timeSlot);

	    for (PickupSchedule schedule : todaySchedules) {
	        if (schedule.getStatus() == Status.COMPLETED) {
	            schedule.setStatus(Status.SCHEDULED);
	        }
	    }

	    pickupScheduleRepository.saveAll(todaySchedules);
	}
	
	public void updateScheduleStatus(Long zoneId) {
		PickupSchedule schedule=pickupScheduleRepository.findByZoneId(zoneId).orElse(null);
		if(schedule!=null) {
			System.out.println("HLOOOOOO");
			schedule.setStatus(Status.COMPLETED);
			pickupScheduleRepository.save(schedule);
		}
	}
	

}
