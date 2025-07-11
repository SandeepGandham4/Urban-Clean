package com.example.workermanagement.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.workermanagement.dto.WorkerDTO;
import com.example.workermanagement.dto.WorkerResponseDTO;
import com.example.workermanagement.enums.WorkerRole;
//import com.example.workermanagement.exception.WorkerNotFoundException;
import com.example.workermanagement.service.WorkerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/urbanclean/v1/admin/workers")
@RequiredArgsConstructor
@Slf4j
//@CrossOrigin(
//	    origins = "http://localhost:3000",
//	    allowedHeaders = {"Authorization", "Content-Type"},
//	    //methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
//	    allowCredentials = "true"
//	)
public class WorkerController {

    private final WorkerService workerService;

    @PostMapping
    public ResponseEntity<WorkerResponseDTO> createWorker(@RequestBody WorkerDTO workerDTO) {     
        return ResponseEntity.ok(workerService.createWorker(workerDTO));
    }

    @GetMapping("/{workerId}")
    public ResponseEntity<WorkerResponseDTO> getWorkerById(@PathVariable Long workerId) {
        return ResponseEntity.ok(workerService.getWorkerById(workerId));
    }
    @GetMapping("/email")
    public ResponseEntity<WorkerResponseDTO> getWorkerByIdByEmail(@RequestParam String email) {
        return ResponseEntity.ok(workerService.getWorkerByIdByEmail(email));
    }



    @GetMapping
    public ResponseEntity<List<WorkerResponseDTO>> getAllWorkers() {
    	List<WorkerResponseDTO> workers = workerService.getAllWorkers();
        return ResponseEntity.ok(workers);
    }


    @GetMapping("/name/{name}")
    public ResponseEntity<List<WorkerResponseDTO>> getWorkerByName(@PathVariable String name) {
        return ResponseEntity.ok(workerService.getWorkerByName(name));
    }


    @GetMapping("/role/{role}")
    public ResponseEntity<List<WorkerResponseDTO>> getWorkersByRole(@PathVariable WorkerRole role) {
    	List<WorkerResponseDTO> workers = workerService.getWorkersByRole(role);
        return ResponseEntity.ok(workers);
    }

    @PutMapping("/{workerId}")
    public ResponseEntity<WorkerResponseDTO> updateWorker(@PathVariable Long workerId, @RequestBody WorkerDTO workerDTO) {
            return ResponseEntity.ok(workerService.updateWorker(workerId, workerDTO));
        
    }

    @DeleteMapping("/{workerId}")
    public ResponseEntity<String> deleteWorker(@PathVariable Long workerId) {
            workerService.deleteWorker(workerId);
            return ResponseEntity.ok("Worker deleted successfully");
    }
}
