//package com.example.worker;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentMatchers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import com.example.workermanagement.dto.WorkerDTO;
//import com.example.workermanagement.dto.WorkerResponseDTO;
//import com.example.workermanagement.entity.Worker;
//import com.example.workermanagement.exception.WorkerNotFoundException;
//import com.example.workermanagement.repo.WorkerRepository;
//import com.example.workermanagement.serviceImpl.WorkerServiceImpl;
//
//@ExtendWith(MockitoExtension.class)
//class WorkerServiceImplTests{
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
//        workerDTO = new WorkerDTO("Alice", "alice@example.com", "Sweeper");
//        worker = new Worker(1L, "Alice", "alice@example.com", "Sweeper", null);
//    }
//
//    @Test
//    void createWorker_shouldReturnResponseDTO() {
//        when(workerRepository.save(ArgumentMatchers.any())).thenReturn(worker);
//
//        WorkerResponseDTO result = workerService.createWorker(workerDTO);
//
//        assertEquals("Alice", result.getName());
//        verify(workerRepository, times(1)).save(any());
//    }
//    @Test
//    void getWorkerById_shouldReturnWorker() {
//        // Arrange
//        Worker worker = new Worker();
//        worker.setWorkerId(1L);
//        worker.setName("Alice");
//        worker.setContactInfo("alice@example.com");
//        worker.setRole("Engineer");
//
//        when(workerRepository.findById(1L)).thenReturn(Optional.of(worker));
//
//        // Act
//        WorkerResponseDTO result = workerService.getWorkerById(1L);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("Alice", result.getName());
//        assertEquals("alice@example.com", result.getContactInfo());
//        assertEquals("Engineer", result.getRole());
//        verify(workerRepository).findById(1L);
//    }
//
//    @Test
//    void getWorkerById_shouldThrowIfNotFound() {
//        when(workerRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(WorkerNotFoundException.class, () -> workerService.getWorkerById(1L));
//    }
//
//    @Test
//    void getAllWorkers_shouldReturnList() {
//        when(workerRepository.findAll()).thenReturn(List.of(worker));
//
//        List<WorkerResponseDTO> result = workerService.getAllWorkers();
//
//        assertEquals(1, result.size());
//        assertEquals("Alice", result.get(0).getName());
//    }
//
//    @Test
//    void getAllWorkers_shouldThrowIfEmpty() {
//        when(workerRepository.findAll()).thenReturn(List.of());
//
//        assertThrows(WorkerNotFoundException.class, () -> workerService.getAllWorkers());
//    }
//
//    @Test
//    void getWorkerByName_shouldReturnList() {
//        when(workerRepository.findByName("Alice")).thenReturn(List.of(worker));
//
//        List<WorkerResponseDTO> result = workerService.getWorkerByName("Alice");
//
//        assertEquals(1, result.size());
//        assertEquals("Alice", result.get(0).getName());
//    }
//
//    @Test
//    void getWorkersByRole_shouldReturnList() {
//        when(workerRepository.findByRole("Sweeper")).thenReturn(List.of(worker));
//
//        List<WorkerResponseDTO> result = workerService.getWorkersByRole("Sweeper");
//
//        assertEquals(1, result.size());
//        assertEquals("Sweeper", result.get(0).getRole());
//    }
//
//    @Test
//    void updateWorker_shouldUpdateFields() {
//        when(workerRepository.findById(1L)).thenReturn(Optional.of(worker));
//        when(workerRepository.save(any())).thenReturn(worker);
//
//        WorkerDTO updateDTO = new WorkerDTO("UpdatedName", "updated@example.com", "Guard");
//
//        WorkerResponseDTO result = workerService.updateWorker(1L, updateDTO);
//
//        assertNotNull(result);
//        verify(workerRepository).save(any());
//    }
//
//    @Test
//    void updateWorker_shouldThrowIfNotFound() {
//        when(workerRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(WorkerNotFoundException.class, () -> workerService.updateWorker(1L, workerDTO));
//    }
//
//    @Test
//    void deleteWorker_shouldDeleteIfExists() {
//        when(workerRepository.existsById(1L)).thenReturn(true);
//
//        workerService.deleteWorker(1L);
//
//        verify(workerRepository).deleteById(1L);
//    }
//
//    @Test
//    void deleteWorker_shouldThrowIfNotFound() {
//        when(workerRepository.existsById(1L)).thenReturn(false);
//
//        assertThrows(WorkerNotFoundException.class, () -> workerService.deleteWorker(1L));
//    }
//}
