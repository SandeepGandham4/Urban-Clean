package com.example.workermanagement.serviceImpl;

import java.util.ArrayList;

//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.workermanagement.dto.WorkerDTO;
import com.example.workermanagement.dto.WorkerResponseDTO;
import com.example.workermanagement.entity.Worker;
import com.example.workermanagement.enums.WorkerRole;
import com.example.workermanagement.exception.WorkerNotFoundException;
import com.example.workermanagement.repo.AssignmentRepository;
import com.example.workermanagement.repo.WorkerRepository;
import com.example.workermanagement.service.WorkerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkerServiceImpl implements WorkerService {
    private final WorkerRepository workerRepository;
    

    @Override
    @Transactional
    public WorkerResponseDTO createWorker(WorkerDTO workerDTO) {
    	
    	Worker worker = new Worker();
        worker.setName(workerDTO.getName());
        worker.setContactInfo(workerDTO.getContactInfo());
        worker.setRole(workerDTO.getRole());

        // Save the worker entity
        Worker savedWorker = workerRepository.save(worker);
        log.info("Worker created: {}", savedWorker);
        WorkerResponseDTO responsedto = new WorkerResponseDTO();
        responsedto.setWorkerId(worker.getWorkerId());
        responsedto.setName(workerDTO.getName());
        responsedto.setContactInfo(workerDTO.getContactInfo());
        responsedto.setRole(workerDTO.getRole());
        
        // Return DTO with generated workerId
        return responsedto;
    }


    @Override
    public WorkerResponseDTO getWorkerById(Long workerId) {
        log.info("Fetching worker with ID: {}", workerId);

        // Fetch the worker from the repository
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> {
                    log.error("Worker Not Found with ID: {}", workerId);
                    return new WorkerNotFoundException("Worker with ID " + workerId + " not found");
                });

        // Map Worker entity to WorkerResponseDTO
        WorkerResponseDTO responseDto = new WorkerResponseDTO();
        responseDto.setWorkerId(worker.getWorkerId());
        responseDto.setName(worker.getName());
        responseDto.setContactInfo(worker.getContactInfo());
        responseDto.setRole(worker.getRole());

        return responseDto;
    }
    public WorkerResponseDTO getWorkerByIdByEmail(String emailId) {
    	log.info("Fetching worker with EmailId: {}", emailId);
    	System.out.println("hloooo");
        // Fetch the worker from the repository
        Worker worker = workerRepository.findByContactInfo(emailId);
        WorkerResponseDTO responseDto = new WorkerResponseDTO();
        // Map Worker entity to WorkerResponseDTO
        if(worker!=null) {
        //WorkerResponseDTO responseDto = new WorkerResponseDTO();
        responseDto.setWorkerId(worker.getWorkerId());
        responseDto.setName(worker.getName());
        responseDto.setContactInfo(worker.getContactInfo());
        responseDto.setRole(worker.getRole());
    }else {
    	throw new WorkerNotFoundException("No worker record found");
    }
        return responseDto;
    }

    public List<WorkerResponseDTO> getAllWorkers() {
        log.info("Fetching all workers");

        List<Worker> workers = workerRepository.findAll();
        
        if (workers.isEmpty()) {
            log.warn("No workers found in the database.");
            throw new WorkerNotFoundException("No workers available");
        }

        List<WorkerResponseDTO> workerResponseList = new ArrayList<>();
        
        for (Worker worker : workers) {
            WorkerResponseDTO responseDto = new WorkerResponseDTO();
            responseDto.setWorkerId(worker.getWorkerId());
            responseDto.setName(worker.getName());
            responseDto.setContactInfo(worker.getContactInfo());
            responseDto.setRole(worker.getRole());
            
            workerResponseList.add(responseDto);
        }

        return workerResponseList;
    }


    @Override
    public List<WorkerResponseDTO> getWorkerByName(String name) {
        log.info("Fetching worker by name: {}", name);
        List<Worker> workers=workerRepository.findByName(name);
        List<WorkerResponseDTO> workerResponseList = new ArrayList<>();

        for (Worker worker : workers) {
            WorkerResponseDTO responseDto = new WorkerResponseDTO();
            responseDto.setWorkerId(worker.getWorkerId());
            responseDto.setName(worker.getName());
            responseDto.setContactInfo(worker.getContactInfo());
            responseDto.setRole(worker.getRole());

            workerResponseList.add(responseDto);
        }
        	return workerResponseList;
        }
        

    @Override
    public List<WorkerResponseDTO> getWorkersByRole(WorkerRole role) {
        log.info("Fetching workers by role: {}", role);
        List<Worker> workers=workerRepository.findAllByRole(role);
        List<WorkerResponseDTO> workerResponseList = new ArrayList<>();
        //List<Assignment> assignedWorkers=
        for (Worker worker : workers) {
            WorkerResponseDTO responseDto = new WorkerResponseDTO();
            responseDto.setWorkerId(worker.getWorkerId());
            responseDto.setName(worker.getName());
            responseDto.setContactInfo(worker.getContactInfo());
            responseDto.setRole(worker.getRole());

            workerResponseList.add(responseDto);
        }
        	return workerResponseList;
    }

    @Override
    @Transactional
    public WorkerResponseDTO updateWorker(Long workerId, WorkerDTO workerDTO) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new WorkerNotFoundException("Worker with ID " + workerId + " not found"));

        worker.setName(workerDTO.getName());
        worker.setContactInfo(workerDTO.getContactInfo());
        worker.setRole(workerDTO.getRole());
        workerRepository.save(worker);
        log.info("Worker updated: {}", workerDTO);
        WorkerResponseDTO responseDto = new WorkerResponseDTO();
        responseDto.getWorkerId();
        responseDto.getName();
        responseDto.getContactInfo();
        responseDto.getRole();
      
        return responseDto;
    }

    @Override
    @Transactional
    public void deleteWorker(Long workerId) {
        if (!workerRepository.existsById(workerId)) {
            throw new WorkerNotFoundException("Worker with ID " + workerId + " not found");
        }
        workerRepository.deleteById(workerId);
        log.info("Worker deleted with ID: {}", workerId);
    }
}
