//package com.example.worker;
//
//import com.example.workermanagement.dto.WorkerDTO;
//import com.example.workermanagement.dto.WorkerResponseDTO;
//import com.example.workermanagement.entity.Worker;
//import com.example.workermanagement.exception.WorkerNotFoundException;
//import com.example.workermanagement.repo.WorkerRepository;
//import com.example.workermanagement.serviceImpl.WorkerServiceImpl;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.*;
//
//@ExtendWith(MockitoExtension.class)
//class WorkerRepositoryTest{
//
//    @Mock
//    private WorkerRepository workerRepository;
//
//    @InjectMocks
//    private WorkerServiceImpl workerService;
//
//    private Worker worker;
//    private WorkerDTO workerDTO;
//
//    @BeforeEach
//    void setUp() {
//        worker = new Worker(1L, "John", "john@example.com", "Driver", null);
//        workerDTO = new WorkerDTO("John", "john@example.com", "Driver");
//    }
//
//    @Test
//    void createWorker_shouldReturnValidResponse() {
//        when(workerRepository.save(any())).thenReturn(worker);
//        WorkerResponseDTO response = workerService.createWorker(workerDTO);
//        assertEquals("John", response.getName());
//    }
//
//    @Test
//    void getWorkerById_shouldReturnWorker() {
//        when(workerRepository.findById(1L)).thenReturn(Optional.of(worker));
//        WorkerResponseDTO response = workerService.getWorkerById(1L);
//        assertEquals("john@example.com", response.getContactInfo());
//    }
//
//    @Test
//    void getWorkerById_shouldThrowIfNotFound() {
//        when(workerRepository.findById(1L)).thenReturn(Optional.empty());
//        assertThrows(WorkerNotFoundException.class, () -> workerService.getWorkerById(1L));
//    }
//
//    @Test
//    void getAllWorkers_shouldReturnList() {
//        when(workerRepository.findAll()).thenReturn(List.of(worker));
//        List<WorkerResponseDTO> list = workerService.getAllWorkers();
//        assertFalse(list.isEmpty());
//        assertEquals("Driver", list.get(0).getRole());
//    }
//
//    @Test
//    void getAllWorkers_shouldThrowIfEmpty() {
//        when(workerRepository.findAll()).thenReturn(List.of());
//        assertThrows(WorkerNotFoundException.class, () -> workerService.getAllWorkers());
//    }
//
//    @Test
//    void getWorkerByName_shouldReturnList() {
//        when(workerRepository.findByName("John")).thenReturn(List.of(worker));
//        List<WorkerResponseDTO> list = workerService.getWorkerByName("John");
//        assertEquals("John", list.get(0).getName());
//    }
//
//    @Test
//    void getWorkersByRole_shouldReturnList() {
//        when(workerRepository.findByRole("Driver")).thenReturn(List.of(worker));
//        List<WorkerResponseDTO> list = workerService.getWorkersByRole("Driver");
//        assertEquals(1, list.size());
//    }
//
//    @Test
//    void updateWorker_shouldModifyFields() {
//        when(workerRepository.findById(1L)).thenReturn(Optional.of(worker));
//        when(workerRepository.save(any())).thenReturn(worker);
//        WorkerDTO updatedDTO = new WorkerDTO("Updated", "new@example.com", "Guard");
//        WorkerResponseDTO result = workerService.updateWorker(1L, updatedDTO);
//        assertNotNull(result);
//    }
//
//    @Test
//    void updateWorker_shouldThrowIfNotFound() {
//        when(workerRepository.findById(1L)).thenReturn(Optional.empty());
//        assertThrows(WorkerNotFoundException.class, () -> workerService.updateWorker(1L, workerDTO));
//    }
//
//    @Test
//    void deleteWorker_shouldDelete() {
//        when(workerRepository.existsById(1L)).thenReturn(true);
//        doNothing().when(workerRepository).deleteById(1L);
//        assertDoesNotThrow(() -> workerService.deleteWorker(1L));
//    }
//
//    @Test
//    void deleteWorker_shouldThrowIfNotExists() {
//        when(workerRepository.existsById(1L)).thenReturn(false);
//        assertThrows(WorkerNotFoundException.class, () -> workerService.deleteWorker(1L));
//    }
//}
