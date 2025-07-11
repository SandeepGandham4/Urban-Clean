package com.example.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dto.AssignmentDto;
import com.example.dto.PickupScheduleResponseDTO;
import com.example.dto.RouteResponseDTO;
import com.example.dto.VehicleResponseDTO;
import com.example.entity.Assignment;
import com.example.entity.Vehicle;
import com.example.exception.DuplicateResourceException;
import com.example.exception.InvalidRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.feign.PickupSchedulingFeignClient;
import com.example.feign.RouteServiceFeignClient;
import com.example.feign.WorkerServiceFeignClient;
import com.example.repository.AssignmentRepository;
import com.example.repository.VehicleRepository;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssignmentService {
	
	private final RouteServiceFeignClient routeServiceFeignClient;
	
	private final PickupSchedulingFeignClient pickupSchedulingFeignClient;

    private final AssignmentRepository assignmentRepository;

    private final VehicleRepository vehicleRepository; 
    
    private final WorkerServiceFeignClient workerServiceFeignClient;
    
    private static final String ASSIGNMENT_NOT_FOUND_MESSAGE = "Assignment not found with ID: ";
    
    public List<PickupScheduleResponseDTO> getAllSchedulesWithRoutesNotAssigned(){
    	try {
    		
    	return pickupSchedulingFeignClient.getAllSchedulesWithRoutesNotAssigned().getBody();
     	}
    	catch (FeignException.NotFound ex) {
        log.info("All Schedules are assigned with routes: {}", ex.getMessage());
        return Collections.emptyList();
    }
    }

    @Transactional
    public AssignmentDto.AssignmentResponse createAssignment(Long zoneId,Long routeId,AssignmentDto.AssignmentCreateRequest request) {
    	if (routeId == null || routeId <= 0) { 
            throw new InvalidRequestException("Invalid Route ID: " + routeId);
       }
       boolean exists = assignmentRepository.existsByRouteIdAndDateAssigned(routeId, request.getDateAssigned());
       if (exists) {
           throw new DuplicateResourceException("An assignment with the same routeId "+routeId+" and date assigned "+request.getDateAssigned()+" already exists.");
       }
    	Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + request.getVehicleId() + ". Cannot create assignment."));
        
        Assignment assignment = new Assignment();
        assignment.setVehicle(vehicle);
        assignment.setRouteId(routeId);
        assignment.setVehicleStatus("0");
        RouteResponseDTO routeResponseDTO=routeServiceFeignClient.getRoute(zoneId, routeId).getBody();
        assignment.setPathDetails(routeResponseDTO.getPathDetails());
        assignment.setDateAssigned(request.getDateAssigned());
        Assignment savedAssignment = assignmentRepository.save(assignment);
        log.info("Created new assignment: {}", savedAssignment);
        return mapToAssignmentResponse(savedAssignment);
    }
    
    List<Vehicle> unassignedVehicles(List<Assignment> vehicles){
    	List<Vehicle> unassignedvehicles=new ArrayList<>();
    	List<Vehicle> totalVehicles=vehicleRepository.findAll();
    	List<Long> assignedvehicleIds=new ArrayList<>();
    	for(Assignment vehicle:vehicles) {
    		assignedvehicleIds.add(vehicle.getVehicle().getVehicleId());
    	}
    	return totalVehicles.stream()
    			.filter(vehicle ->!assignedvehicleIds.contains(vehicle.getVehicleId()))
    			.collect(Collectors.toList());
    }
    

    public List<AssignmentDto.AssignmentResponse> getAssignmentsByDate(LocalDate date) {
        List<Assignment> assignments= assignmentRepository.findByDateAssigned(date);
        	if(assignments.isEmpty()) {
        		throw new ResourceNotFoundException("No assignments found for the date: "+date);
        	}
        	return assignments.stream()
                .map(this::mapToAssignmentResponse)
                .toList();
    }
    
    public List<AssignmentDto.AssignmentResponse> getAllAssignments() {
        return assignmentRepository.findAll().stream()
                .map(this::mapToAssignmentResponse)
                .toList();
    }


    public AssignmentDto.AssignmentResponse getAssignmentById(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException(ASSIGNMENT_NOT_FOUND_MESSAGE + assignmentId));
        return mapToAssignmentResponse(assignment);
    }

    @Transactional
    public AssignmentDto.AssignmentResponse updateAssignment(Long assignmentId, AssignmentDto.AssignmentUpdateRequest request) {
    	System.out.println("assignmentId"+assignmentId);
    	System.out.println("request"+request);
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException(ASSIGNMENT_NOT_FOUND_MESSAGE + assignmentId));
        Long prevVehicleId=assignment.getVehicle().getVehicleId();
        if (request.getRouteId() != null) {
            
            if (request.getRouteId() <= 0) {
                 throw new InvalidRequestException("Invalid new Route ID: " + request.getRouteId());
            }
            System.out.println("routenotnull"+assignment);
            assignment.setRouteId(request.getRouteId());
        }   System.out.println(assignment);
        if (request.getDateAssigned() != null) {
            assignment.setDateAssigned(request.getDateAssigned());
            System.out.println("datenotnull"+assignment);
        }
        Vehicle vehicle=vehicleRepository.findById(request.getVehicleId()).orElse(null);
        if(vehicle!=null) {
        if(request.getVehicleId()!=null) {
        	assignment.setVehicle(vehicle);
        }
        }
        
        Assignment updatedAssignment = assignmentRepository.save(assignment);
        System.out.println(updatedAssignment);
        log.info("Updated assignment {}: {}", assignmentId, updatedAssignment);
        workerServiceFeignClient.updateAssignmentOfWorkersByVehicleId(prevVehicleId,request.getVehicleId());
        return mapToAssignmentResponse(updatedAssignment);
    }

    @Transactional
    public String deleteAssignment(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException(ASSIGNMENT_NOT_FOUND_MESSAGE + assignmentId));
        if(assignment.isWorkerAssigned()) {
        
        workerServiceFeignClient.deleteAssignmentOfWorkersByVehicleId(assignment.getVehicle().getVehicleId());
        }
        log.info("Deleted assignment with ID: {}", assignmentId);
        assignmentRepository.delete(assignment);
        return "Assignment Successfully deleted!";
    }

    private AssignmentDto.AssignmentResponse mapToAssignmentResponse(Assignment assignment) {
        AssignmentDto.AssignmentResponse response = new AssignmentDto.AssignmentResponse();
        response.setAssignmentId(assignment.getAssignmentId());
        if (assignment.getVehicle() != null) {
            response.setVehicleId(assignment.getVehicle().getVehicleId());
            response.setVehicleRegistrationNo(assignment.getVehicle().getRegistrationNo());
            response.setVehicleStatus(assignment.getVehicleStatus());
        }
        response.setRouteId(assignment.getRouteId());
        response.setDateAssigned(assignment.getDateAssigned());
        return response;
    }
    public List<RouteResponseDTO> getRoutesByZoneId(Long zoneId){
    	log.info("Fetching all newly created zones");
    	List<RouteResponseDTO> routes=routeServiceFeignClient.getRoutesByZoneId(zoneId).getBody();
    	
        if (routes == null || routes.isEmpty()) {
        	  log.warn("No Routes Found");
        	  throw new ResourceNotFoundException("No Routes Existed");
        }
        List<Assignment> assignedRoutes = assignmentRepository.findAll();
        List<Long> l= new ArrayList<>();
        for(Assignment s:assignedRoutes) {
        	l.add(s.getRouteId());
        }
        List<RouteResponseDTO> unAssignedRoutes=routes.stream()
        .filter(route -> !l.contains(route.getId()))
        .collect(Collectors.toList());
        
        return unAssignedRoutes;
    }
    
    
    public void notifyPickupScheduleThatAllRoutesAreScheduled(Long zoneId) {
    	pickupSchedulingFeignClient.scheduleIsAssignedWithVehicles(zoneId);
    }
    
    
    
    public List<VehicleResponseDTO> getVehiclesOfWorkersNotAssigned(){
    	if(assignmentRepository.findAll().isEmpty()) {
			throw new ResourceNotFoundException("No Vehicles Existed");
		}
		if(assignmentRepository.count()==assignmentRepository.findAllByIsWorkerAssigned(true).size()) {
			throw new ResourceNotFoundException("All Workers are assigned with the vehicles");
		}
		List<Assignment> vehicles=assignmentRepository.findAllByIsWorkerAssigned(false);
		return vehicles.stream()
				.map(vehicle -> new VehicleResponseDTO(vehicle.getAssignmentId(),vehicle.getVehicle().getVehicleId(),vehicle.getVehicle().getRegistrationNo(),vehicle.getRouteId(),vehicle.getDateAssigned()))
				.collect(Collectors.toList());
    }
    public void workerIsAssignedWithVehicles(Long vehicleId) {
    	Assignment assignment= assignmentRepository.findByVehicle_VehicleId(vehicleId);
    	if(assignment!=null) {
    		assignment.setWorkerAssigned(true);
    		assignmentRepository.save(assignment);
    	}
    }
    @Transactional
    public void deleteAssignmentByRouteId(Long routeId) {
    	Assignment assignment=assignmentRepository.findByRouteId(routeId);
    	//return "ASSIGNMENTDELETE"+assignment.getAssignmentId();
    	if(assignment!=null) {
    		System.out.println("ASSIGNMENTDELETEBYID"+assignment.getAssignmentId());
    		if(assignment.isWorkerAssigned()) {
    		workerServiceFeignClient.deleteAssignmentOfWorkersByVehicleId(assignment.getVehicle().getVehicleId());
    		assignmentRepository.deleteByRouteId(routeId);
    		}else {
    			assignmentRepository.deleteByRouteId(routeId);
    		}
    	}
    }
    @Transactional
    public void notifyVehicleServiceToDeleteByZone(List<Long> listOfRoutes) {
    	List<Assignment> assignments=assignmentRepository.findAll();
    	List<Assignment> routes=assignments.stream().filter(assignment -> listOfRoutes.contains(assignment.getRouteId()))
    			.collect(Collectors.toList());
    	for(Assignment route:routes) {
    		deleteAssignmentByRouteId(route.getRouteId());
    	}
    }

    public String updateVehicleStatus(Long vehicleId) {
	    Assignment assignment = assignmentRepository.findByVehicle_VehicleId(vehicleId);
	    System.out.println(assignment);
	    if (assignment != null) {
	        System.out.println(assignment.getPathDetails());
	        String route = assignment.getPathDetails();
	        
	        String[] parts = route.split("#");
	        System.out.println(Arrays.toString(parts));
	        
	        String distanceWithKm = parts[1].trim();
	        System.out.println(distanceWithKm);
	        String numberOnly = distanceWithKm.replace("km", "").trim();
	        System.out.println(numberOnly);
	        int distance = Integer.parseInt(numberOnly);
	        if(!assignment.getVehicleStatus().equals("Completed")) {
	        int status = Integer.parseInt(assignment.getVehicleStatus());
	        System.out.println(status+" "+distance);
	        
	        status = status + (distance / 4);
	        if(status<distance) {
	        assignment.setVehicleStatus(""+status);
	        assignmentRepository.save(assignment);
	        
	        System.out.println(status+" "+distance);
	        return "Distance Covered: "+status+"km of "+distance+"km";
	        }
	        else {
	        	assignment.setVehicleStatus("Completed");
	        	assignmentRepository.save(assignment);
	        	return "Waste Collection Completed";
	        }
	        }else {
	        	return "Waste Collection Completed";
	        }
	    }
	    return "Completed";
	}
    
    public void countNumberOfWastesLogged(Long zoneId) {
    	List<RouteResponseDTO> listOfRoutes=routeServiceFeignClient.getRoutesByZoneId(zoneId).getBody();
    	int sum=0;
    	int count=listOfRoutes.size();
    	System.out.println("COUNT"+count);
    	List<Assignment> assignedRoutes=assignmentRepository.findAll();
    	List<Long> l=new ArrayList<>();
    	for(Assignment a:assignedRoutes) {
    		l.add(a.getRouteId());
    	}
    	System.out.println(assignedRoutes);
    	for(RouteResponseDTO route:listOfRoutes) {
    		if(l.contains(route.getId())) {
    			Assignment assignment=assignmentRepository.findByRouteId(route.getId());
    			if(assignment.isWasteLogged()) {
    				sum=sum+1;
    				System.out.println("sum "+sum);
    			}else {
    				break;
    			}
    		}
    	}
    	System.out.println("Count sum"+count+" "+sum);
    	if(count==sum) {
    		pickupSchedulingFeignClient.updateScheduleStatus(zoneId);
    	}
    }

	public void isWasteLogged(Long vehicleId,Long zoneId) {
		Assignment assignment=assignmentRepository.findByVehicle_VehicleId(vehicleId);
		if(assignment!=null && assignment.isWasteLogged()==false){
			assignment.setWasteLogged(true);
			assignmentRepository.save(assignment);
			countNumberOfWastesLogged(zoneId);
		}
		
	}
	
	public String getVehicleStatus(Long vehicleId) {
		Assignment assignment=assignmentRepository.findByVehicle_VehicleId(vehicleId);
		String msg=" ";
		if(assignment!=null) {
	        String route = assignment.getPathDetails();
	        
	        String[] parts = route.split("#");	        
	        String distanceWithKm = parts[1].trim();
	        String numberOnly = distanceWithKm.replace("km", "").trim();
	        int distance = Integer.parseInt(numberOnly);
	        if(!assignment.getVehicleStatus().equals("Completed")) {
		        int status = Integer.parseInt(assignment.getVehicleStatus());
		        msg="current_status "+status+" total_distance "+distance;
		        System.out.println("msg"+msg);
		        return msg;
	        }else {
	        	return "Completed";
	        }
	        
	        
	        
		}
		return msg;
	}
 
 
    
    
}
