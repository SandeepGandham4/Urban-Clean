/*package com.example.workermanagement.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.workermanagement.dto.AssignmentDTO;
import com.example.workermanagement.dto.AssignmentResponseDTO;
import com.example.workermanagement.entity.Assignment;
import com.example.workermanagement.exception.AssignmentNotFoundException;
import com.example.workermanagement.repo.AssignmentRepository;
import com.example.workermanagement.service.AssignmentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
    private final AssignmentRepository assignmentRepository;

   @Override
    public AssignmentResponseDTO assignWorker(AssignmentDTO assignmentDTO) {
        Assignment assignment = new Assignment();
        assignment.setWorkerId(assignmentDTO.getWorkerId());
        assignment.setZoneId(assignmentDTO.getZoneId());
        assignment.setShiftTime(assignmentDTO.getShiftTime());

        // Save and retrieve the generated ID
        Assignment savedAssignment = assignmentRepository.save(assignment);
        log.info("Assignment created: {}", savedAssignment);

        // Return response DTO including the generated assignmentId
        return new AssignmentResponseDTO(
                savedAssignment.getAssignmentId(),
                savedAssignment.getWorkerId(),
                savedAssignment.getZoneId(),
                savedAssignment.getShiftTime()
        );
    }


    @Override
    public AssignmentResponseDTO getAssignmentById(Long assignmentId) {
        log.info("Fetching assignment with ID: {}", assignmentId);
        Assignment assignment = assignmentRepository.findById(assignmentId).orElse(null);
        if(assignment==null) {
        	log.info("No Assignmnet exists with assignment ID{}",assignmentId);
        	throw new AssignmentNotFoundException("Assignment with ID " + assignmentId + " not found"); 
        }
        return new AssignmentResponseDTO(assignmentId,assignment.getWorkerId(),assignment.getZoneId(),assignment.getShiftTime());
    }

    @Override
    public List<AssignmentDTO> getAssignmentsByWorkerId(Long workerId) {
        log.info("Fetching assignments for worker ID: {}", workerId);
//        if (assignments.isEmpty()) {
//            return ResponseEntity.status(404).body(assignments);
//        }
        return assignmentRepository.findByWorkerId(workerId).stream()
                .map(assignment -> new AssignmentDTO(
                        assignment.getWorkerId(),
                        assignment.getZoneId(),
                        assignment.getShiftTime()))
                .toList();
    }

    @Override
    public Optional<AssignmentDTO> getAssignmentByZoneId(Long zoneId) {
        log.info("Fetching assignment for zone ID: {}", zoneId);
        return assignmentRepository.findByZoneId(zoneId)
                .map(assignment -> new AssignmentDTO(
                        assignment.getWorkerId(),
                        assignment.getZoneId(),
                        assignment.getShiftTime()));
    }

    @Override
    public List<AssignmentDTO> getAllAssignments() {
        log.info("Fetching all assignments");
        return assignmentRepository.findAll().stream()
                .map(assignment -> new AssignmentDTO(
                        assignment.getWorkerId(),
                        assignment.getZoneId(),
                        assignment.getShiftTime()))
                .toList();
    }

    @Override
    @Transactional
    public AssignmentResponseDTO updateAssignment(Long assignmentId, AssignmentDTO assignmentDTO) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment with ID " + assignmentId + " not found"));

        assignment.setZoneId(assignmentDTO.getZoneId());
        assignment.setShiftTime(assignmentDTO.getShiftTime());
        assignmentRepository.save(assignment);
        log.info("Assignment updated: {}", assignmentDTO);
        return new AssignmentResponseDTO(assignmentId,assignmentDTO.getWorkerId(),assignmentDTO.getZoneId(),assignmentDTO.getShiftTime());
    }

    @Override
    @Transactional
    public void removeAssignment(Long assignmentId) {
        if (!assignmentRepository.existsById(assignmentId)) {
            throw new AssignmentNotFoundException("Assignment with ID " + assignmentId + " not found");
        }
        assignmentRepository.deleteById(assignmentId);
        log.info("Assignment removed with ID: {}", assignmentId);
    }
}*/
package com.example.workermanagement.serviceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.workermanagement.dto.AssignmentDTO;
import com.example.workermanagement.dto.AssignmentRequestDTO;
import com.example.workermanagement.dto.AssignmentResponseDTO;
import com.example.workermanagement.dto.PickupScheduleResponseDTO;
import com.example.workermanagement.dto.RouteResponseDTO;
import com.example.workermanagement.dto.VehicleAndWorkerResponseDTO;
import com.example.workermanagement.dto.VehicleResponseDTO;
import com.example.workermanagement.dto.WorkerResponseDTO;
import com.example.workermanagement.entity.Assignment;
import com.example.workermanagement.entity.Worker;
import com.example.workermanagement.enums.WorkerRole;
import com.example.workermanagement.exception.AssignmentNotFoundException;
import com.example.workermanagement.exception.WorkerNotFoundException;
import com.example.workermanagement.feign.PickupSchedulingFeignClient;
import com.example.workermanagement.feign.VehicleServiceFeignClient;
import com.example.workermanagement.feign.WasteLogServiceFeignClient;
import com.example.workermanagement.feign.ZoneServiceFeignClient;
import com.example.workermanagement.repo.AssignmentRepository;
import com.example.workermanagement.repo.WorkerRepository;
import com.example.workermanagement.service.AssignmentService;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {


    private final AssignmentRepository assignmentRepository;
    private final WorkerRepository workerRepository;
    private final PickupSchedulingFeignClient pickupSchedulingFeignClient;
    private final ZoneServiceFeignClient zoneServiceFeignClient;
    private final VehicleServiceFeignClient vehicleServiceFeignClient;
    private final WasteLogServiceFeignClient wasteLogServiceFeignClient;
    
    private final List<Assignment> assignments= new ArrayList<>();

    @Override
    public void assignWorker(Long zoneId,Long routeId,WorkerRole type,AssignmentDTO assignmentDTO) {
        Worker worker = workerRepository.findById(assignmentDTO.getWorkerId())
                .orElseThrow(() -> new WorkerNotFoundException("Worker with ID " + assignmentDTO.getWorkerId() + " not found"));

        Assignment assignment = new Assignment();
        assignment.setZoneId(zoneId);
        assignment.setShiftTime(assignmentDTO.getShiftTime());
        assignment.setWorker(worker);
        worker.setAssignment(assignment);
        assignments.add(assignment);
        //Assignment savedAssignment = assignmentRepository.save(assignment);
        log.info("Assignment created for worker ID: {}", worker.getWorkerId());

//        return new AssignmentResponseDTO(
//                savedAssignment.getAssignmentId(),
//                worker.getWorkerId(),
//                savedAssignment.getZoneId(),
//                savedAssignment.getShiftTime()
//        );
    }
     
    @Override
    public List<AssignmentResponseDTO> assignWorkersToVehicle(Long zoneId,Long routeId,AssignmentRequestDTO assignmentRequestDTO){
    	AssignmentDTO assignmentDTO = new AssignmentDTO();
    	assignmentDTO.setWorkerId(assignmentRequestDTO.getDriverId());
    	String shift=assignmentRequestDTO.getShiftTimeByWorker().get((assignmentRequestDTO.getDriverId()));
    	assignmentDTO.setShiftTime(shift);
    	assignWorker(zoneId, routeId, WorkerRole.DRIVER, assignmentDTO);
    	for(Long ids:assignmentRequestDTO.getWorkerIds()) {
        	assignmentDTO.setWorkerId(ids);
        	assignmentDTO.setShiftTime(assignmentRequestDTO.getShiftTimeByWorker().get(ids));
        	assignWorker(zoneId, routeId, WorkerRole.GARBAGECOLLECTOR, assignmentDTO);
    	}
    	List<Assignment> savedassignments=assignmentRepository.saveAll(assignments);
    	
    	vehicleServiceFeignClient.workerIsAssignedWithVehicles(assignmentRequestDTO.getVehicleId());
    	
    	
    	List<AssignmentResponseDTO> listOfWorkersAssigned= savedassignments.stream()
    			.map(savedassignment -> new AssignmentResponseDTO(savedassignment.getAssignmentId(),savedassignment.getWorker().getWorkerId(),savedassignment.getZoneId(),savedassignment.getShiftTime()))
    			.collect(Collectors.toList());
    	
     	VehicleAndWorkerResponseDTO vehicleAndWorkerResponseDTO=new VehicleAndWorkerResponseDTO();
     	vehicleAndWorkerResponseDTO.setListOfWorkersAssigned(listOfWorkersAssigned);
     	vehicleAndWorkerResponseDTO.setVehicleId(assignmentRequestDTO.getVehicleId());
    	wasteLogServiceFeignClient.storeVehicleIdAndWorkers(vehicleAndWorkerResponseDTO);
    	System.out.println(assignments);
    	assignments.clear();
     	return listOfWorkersAssigned;
    	
    	
    }
    @Override
    public void notifyPickupScheduleThatAllWorkersAreScheduled(Long zoneId) {
    	pickupSchedulingFeignClient.scheduleIsAssignedWithWorkers(zoneId);
    }
    
    @Override
    public List<WorkerResponseDTO> getAllWorkersByRole(Long zoneId,Long routeId,WorkerRole typeOfWorker){
    	List<Worker> workers = workerRepository.findAllByRole(typeOfWorker);
    	List<Assignment> assignedWorkers=assignmentRepository.findAll();
    	List<Long> assignedWorkers_ids=new ArrayList<>();
    	for(Assignment assignment: assignedWorkers) {
    		assignedWorkers_ids.add(assignment.getWorker().getWorkerId());
    	}
    	List<Worker> listOfWorkersNotAssigned=workers.stream()
    			.filter(worker -> !assignedWorkers_ids.contains(worker.getWorkerId()))
    			.collect(Collectors.toList());
//    	System.out.println(listOfWorkersNotAssigned);
    	return listOfWorkersNotAssigned.stream()
    			.map(listOfWorkerNotAssigned -> new WorkerResponseDTO(listOfWorkerNotAssigned.getWorkerId(),listOfWorkerNotAssigned.getName(),listOfWorkerNotAssigned.getContactInfo(),listOfWorkerNotAssigned.getRole()))
    			.collect(Collectors.toList());
    }

    @Override
    public AssignmentResponseDTO getAssignmentById(Long assignmentId) {
        log.info("Fetching assignment with ID: {}", assignmentId);
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment with ID " + assignmentId + " not found"));

        return new AssignmentResponseDTO(
                assignment.getAssignmentId(),
                assignment.getWorker().getWorkerId(),
                assignment.getZoneId(),
                assignment.getShiftTime()
        );
    }

    @Override
    public List<AssignmentDTO> getAssignmentsByWorkerId(Long workerId) {
        log.info("Fetching assignments for worker ID: {}", workerId);
        List<AssignmentDTO> assignmentList = new ArrayList<>();

        List<Assignment> assignments = assignmentRepository.findByWorker_WorkerId(workerId);
        for (Assignment assignment : assignments) {
            AssignmentDTO dto = new AssignmentDTO(
                    assignment.getWorker().getWorkerId(),
                    assignment.getShiftTime()
            );
            assignmentList.add(dto);
        }

        return assignmentList;
    }

    @Override
    public Optional<AssignmentDTO> getAssignmentByZoneId(Long zoneId) {
        log.info("Fetching assignment for zone ID: {}", zoneId);
        return assignmentRepository.findByZoneId(zoneId)
                .map(assignment -> new AssignmentDTO(
                        assignment.getWorker().getWorkerId(),
                        assignment.getShiftTime()
                ));
    }

    @Override
    public List<AssignmentResponseDTO> getAllAssignments() {
        log.info("Fetching all assignments");
        List<Assignment> assignments = assignmentRepository.findAll();

        if (assignments.isEmpty()) {
            log.info("No assignments found");
            return new ArrayList<>();
        }
        List<AssignmentResponseDTO> dtoList = new ArrayList<>();

        for (Assignment assignment : assignments) {
            dtoList.add(new AssignmentResponseDTO(
            		assignment.getAssignmentId(),
                    assignment.getWorker().getWorkerId(),
                    assignment.getZoneId(),
                    assignment.getShiftTime()                    
                    ));
        }

        return dtoList;
    }

    @Override
    @Transactional
    public AssignmentResponseDTO updateAssignment(Long assignmentId, AssignmentDTO assignmentDTO) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment with ID " + assignmentId + " not found"));

        //assignment.setZoneId(assignmentDTO.getZoneId());
        assignment.setShiftTime(assignmentDTO.getShiftTime());

        assignmentRepository.save(assignment);
        log.info("Assignment updated: {}", assignmentDTO);

        return new AssignmentResponseDTO(
                assignmentId,
                assignment.getWorker().getWorkerId(),
                assignment.getZoneId(),
                assignment.getShiftTime()
        );
    }

    @Override
    @Transactional
    public void removeAssignment(Long assignmentId) {
    	System.out.println("hloooo");
    	Assignment assignment=assignmentRepository.findById(assignmentId).orElse(null);
    	if(assignment!=null) {
        if (!assignmentRepository.existsById(assignmentId)) {
            throw new AssignmentNotFoundException("Assignment with ID " + assignmentId + " not found");
        }
        System.out.println("NOERROR");
        assignmentRepository.deleteByWorkerId(assignment.getWorker().getWorkerId());
        System.out.println("DELETED");
    	}
        log.info("Assignment removed with ID: {}", assignmentId);
    }
    
//    @Override
//    public List<PickupScheduleResponseDTO> getAllSchedulesWithWorkersNotAssignedForAllRoutes(){
//    	return pickupSchedulingFeignClient.getAllSchedulesWithWorkersNotAssignedForAllRoutes().getBody();
//    }
    
    
    @Override
    public List<PickupScheduleResponseDTO> getAllSchedulesWithWorkersNotAssignedForAllRoutes() {
        try {
            ResponseEntity<List<PickupScheduleResponseDTO>> response =
                pickupSchedulingFeignClient.getAllSchedulesWithWorkersNotAssignedForAllRoutes();

            return response.getBody() != null ? response.getBody() : Collections.emptyList();

        } catch (FeignException.NotFound ex) {
            log.info("No schedules found with workers unassigned: {}", ex.getMessage());
            return Collections.emptyList();
        }
    }

//    @Override
//    public List<VehicleResponseDTO> getAllVehiclesOfWorkersNotAssigned(Long zoneId){
//    	List<RouteResponseDTO> routes=zoneServiceFeignClient.getRoutesByZoneId(zoneId).getBody();
//    	
//    	List<Long> routeIds=new ArrayList<>();
//    	for(RouteResponseDTO route:routes) {
//    		routeIds.add(route.getId());
//    	}
//    	List<VehicleResponseDTO> vehicles=vehicleServiceFeignClient.getVehiclesOfWorkersNotAssigned().getBody();
//    	return vehicles.stream()
//    			.filter(vehicle ->routeIds.contains(vehicle.getRouteId()))
//    			.collect(Collectors.toList());
//    }
    
    @Override
    public List<VehicleResponseDTO> getAllVehiclesOfWorkersNotAssigned(Long zoneId) {
        try {
            List<RouteResponseDTO> routes = zoneServiceFeignClient.getRoutesByZoneId(zoneId).getBody();

            if (routes == null || routes.isEmpty()) {
                return Collections.emptyList();
            }

            List<Long> routeIds = routes.stream()
                                        .map(RouteResponseDTO::getId)
                                        .collect(Collectors.toList());

            ResponseEntity<List<VehicleResponseDTO>> response = vehicleServiceFeignClient.getVehiclesOfWorkersNotAssigned();

            if (response.getBody() == null) {
                return Collections.emptyList(); 
            }

            return response.getBody().stream()
                           .filter(vehicle -> routeIds.contains(vehicle.getRouteId()))
                           .collect(Collectors.toList());

        } catch (FeignException.NotFound ex) {
            log.info("All workers are already assigned to vehicles: {}", ex.getMessage());
            return Collections.emptyList();
        }
    }

    
    
//    @Transactional
//    public void deleteAssignmentOfWorkersByVehicleId(Long vehicleId) {
//    	System.out.println(vehicleId);
//            List<Long> listOfWorkers = wasteLogServiceFeignClient.deleteLogDataOfWorkerAndVehicleId(vehicleId)	;
//            System.out.println(listOfWorkers);
//            if (listOfWorkers.size()!=0) {
//                for (Long workerId : listOfWorkers) {
//                    assignmentRepository.deleteByWorker_WorkerId(workerId);
//                }
//            }
//        }
        
    @Transactional
    public void deleteAssignmentOfWorkersByVehicleId(Long vehicleId) {
        ResponseEntity<List<Long>> responseEntity = wasteLogServiceFeignClient.deleteLogDataOfWorkerAndVehicleId(vehicleId);
        List<Long> listOfWorkers = null;
        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            listOfWorkers = responseEntity.getBody();
        } else {
            System.err.println("Feign call failed or returned null body. Status: " + responseEntity.getStatusCode());
        }
        if (listOfWorkers != null && !listOfWorkers.isEmpty()) { 
            for (Long workerId : listOfWorkers) {
            	assignmentRepository.deleteByWorkerId(workerId);
            }
        } else {
            System.out.println("No worker IDs to delete assignments for.");
        }
    }
    
    @Override
    public void updateAssignmentOfWorkersByVehicleId(@PathVariable Long prevVehicleId,@PathVariable Long newVehicleId) {
    	ResponseEntity<List<Long>> responseEntity = wasteLogServiceFeignClient.updateLogDataOfWorkerAndVehicleId(prevVehicleId,newVehicleId);
    	System.out.println("responseEntity"+responseEntity);
        List<Long> listOfWorkers = null;
        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            listOfWorkers = responseEntity.getBody();
        } else {
            System.err.println("Feign call failed or returned null body. Status: " + responseEntity.getStatusCode());
        }
    }
    
      
    public void deleteAssignmentOfWorkersByZoneId(Long zoneId) {
    	if(zoneId!=0) {
    		assignmentRepository.deleteAllByZoneId(zoneId);
    	}
    	wasteLogServiceFeignClient.deleteLogDataOfWorkerByZoneId(zoneId);
    }
    
    
}

