package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dto.VehicleDto;
import com.example.dto.VehicleResponseDTO;
import com.example.entity.Assignment;
import com.example.entity.Vehicle;
import com.example.enums.VehicleStatus;
import com.example.exception.DuplicateResourceException;
import com.example.exception.InvalidRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.AssignmentRepository;
import com.example.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleService {

    
    private final VehicleRepository vehicleRepository;
    private final AssignmentRepository assignmentRepository; 
    
    private static final String VEHICLE_NOT_FOUND_MESSAGE = "Vehicle not found with ID: ";

    @Transactional
    public VehicleDto.VehicleResponse createVehicle(VehicleDto.VehicleCreateRequest request) {
        if (vehicleRepository.existsByRegistrationNo(request.getRegistrationNo())) {
            throw new DuplicateResourceException("Vehicle with registration number '" + request.getRegistrationNo() + "' already exists.");
        }
        Vehicle vehicle = new Vehicle();
        vehicle.setRegistrationNo(request.getRegistrationNo());
        vehicle.setType(request.getType());
        vehicle.setStatus(request.getStatus()); 
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        log.info("Created new vehicle: {}", savedVehicle);
        return mapToVehicleResponse(savedVehicle);
    }
    
    @Transactional
    public void deleteVehicle(Long vehicleId) {
        if (vehicleId == null) {
            throw new InvalidRequestException("Vehicle ID must not be null");
        }
 
        log.info("Fetching vehicle with ID: {}", vehicleId);
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + vehicleId));
 
        List<Assignment> assignments = assignmentRepository.findByVehicle(vehicle);
 
        if (!assignments.isEmpty()) {
            throw new InvalidRequestException("Cannot delete vehicle with ID: " + vehicleId + " because it has existing assignments");
        }
 
        vehicleRepository.delete(vehicle);
        log.info("Vehicle with ID: {} has been deleted successfully", vehicleId);
    }
 
    
    public List<VehicleDto.VehicleResponse> unassignedVehicles(){
    	List<Assignment> vehicles=assignmentRepository.findAll();
    	List<Vehicle> totalVehicles=vehicleRepository.findAll();
    	List<Long> assignedvehicleIds=new ArrayList<>();
    	for(Assignment vehicle:vehicles) {
    		assignedvehicleIds.add(vehicle.getVehicle().getVehicleId());
    	}
    	List<Vehicle>unassignedvehicles= totalVehicles.stream()
    			.filter(vehicle ->!assignedvehicleIds.contains(vehicle.getVehicleId()))
    			.collect(Collectors.toList());
    	return unassignedvehicles.stream()
    			.map(this::mapToVehicleResponse)
    			.toList();
    }

    public List<VehicleDto.VehicleResponse> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(this::mapToVehicleResponse)
                .toList();
    }

    public VehicleDto.VehicleResponse getVehicleById(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException(VEHICLE_NOT_FOUND_MESSAGE + vehicleId));
        return mapToVehicleResponse(vehicle);
    }
    
    public VehicleDto.VehicleStatusResponse getVehicleStatus(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException(VEHICLE_NOT_FOUND_MESSAGE + vehicleId));
        VehicleDto.VehicleStatusResponse statusResponse = new VehicleDto.VehicleStatusResponse();
        statusResponse.setStatus(vehicle.getStatus());
        return statusResponse;
    }

    @Transactional
    public VehicleDto.VehicleResponse updateVehicle(Long vehicleId, VehicleDto.VehicleUpdateRequest request) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException(VEHICLE_NOT_FOUND_MESSAGE + vehicleId));

        if (request.getRegistrationNo() != null && !request.getRegistrationNo().equals(vehicle.getRegistrationNo())) {
            if (vehicleRepository.existsByRegistrationNo(request.getRegistrationNo())) {
                throw new DuplicateResourceException("Vehicle with registration number '" + request.getRegistrationNo() + "' already exists.");
            }
            vehicle.setRegistrationNo(request.getRegistrationNo());
        }
        if (request.getType() != null) {
            vehicle.setType(request.getType());
        }
        if(request.getStatus().equals(request.getStatus().ACTIVE) || request.getStatus().equals(request.getStatus().INACTIVE)) {
        	vehicle.setStatus(request.getStatus());
        }
        
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        log.info("Updated vehicle {}: {}", vehicleId, updatedVehicle);
        return mapToVehicleResponse(updatedVehicle);
    }

    @Transactional
    public VehicleDto.VehicleResponse updateVehicleStatus(Long vehicleId, VehicleDto.VehicleStatusUpdateRequest request) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException(VEHICLE_NOT_FOUND_MESSAGE + vehicleId));
        
        vehicle.setStatus(request.getNewStatus());
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        log.info("Updated status for vehicle {}: New status - '{}'. Admin notified (logged).", vehicleId, request.getNewStatus());
                return mapToVehicleResponse(updatedVehicle);
    }

    private VehicleDto.VehicleResponse mapToVehicleResponse(Vehicle vehicle) {
        VehicleDto.VehicleResponse response = new VehicleDto.VehicleResponse();
        response.setVehicleId(vehicle.getVehicleId());
        response.setRegistrationNo(vehicle.getRegistrationNo());
        response.setType(vehicle.getType());
        response.setStatus(vehicle.getStatus());
        return response;
    }

	public Long getCountOfActiveVehicles() {
		List<Vehicle> count=vehicleRepository.findAllByStatus(VehicleStatus.ACTIVE);
		return (long) count.size();
	}
}
