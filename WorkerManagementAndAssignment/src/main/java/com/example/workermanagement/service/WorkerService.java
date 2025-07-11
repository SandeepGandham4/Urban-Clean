package com.example.workermanagement.service;
import java.util.List;


import com.example.workermanagement.dto.WorkerDTO;
import com.example.workermanagement.dto.WorkerResponseDTO;
import com.example.workermanagement.enums.WorkerRole;

public interface WorkerService {
    WorkerResponseDTO createWorker(WorkerDTO workerDTO);
    WorkerResponseDTO getWorkerById(Long workerId);
    List<WorkerResponseDTO> getAllWorkers();
    List<WorkerResponseDTO> getWorkerByName(String name); // Find worker by name
    List<WorkerResponseDTO> getWorkersByRole(WorkerRole role); // Find workers by role
    WorkerResponseDTO updateWorker(Long workerId, WorkerDTO workerDTO);
    void deleteWorker(Long workerId);
	WorkerResponseDTO getWorkerByIdByEmail(String emailId);
}
