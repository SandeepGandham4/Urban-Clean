package com.example.worker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.workermanagement.dto.AssignmentDTO;
import com.example.workermanagement.dto.AssignmentResponseDTO;
import com.example.workermanagement.entity.Assignment;
import com.example.workermanagement.entity.Worker;
import com.example.workermanagement.exception.AssignmentNotFoundException;
import com.example.workermanagement.exception.WorkerNotFoundException;
import com.example.workermanagement.repo.AssignmentRepository;
import com.example.workermanagement.repo.WorkerRepository;
import com.example.workermanagement.serviceImpl.AssignmentServiceImpl;

class AssignmentServiceImplTests {

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private WorkerRepository workerRepository;

    @InjectMocks
    private AssignmentServiceImpl assignmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAssignWorker_Success() {
        Worker worker = new Worker();
        worker.setWorkerId(1L);

        Assignment assignment = new Assignment();
        assignment.setAssignmentId(1L);
        assignment.setZoneId(101L);
        assignment.setShiftTime("Morning");
        assignment.setWorker(worker);

        //AssignmentDTO assignmentDTO = new AssignmentDTO(1L, 101L, "Morning");

        when(workerRepository.findById(1L)).thenReturn(Optional.of(worker));
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);

        //AssignmentResponseDTO response = assignmentService.assignWorker(assignmentDTO);

//        assertNotNull(response);
//        assertEquals(1L, response.getWorkerId());
//        assertEquals(101L, response.getZoneId());
//        assertEquals("Morning", response.getShiftTime());

        verify(workerRepository, times(1)).findById(1L);
        verify(assignmentRepository, times(1)).save(any(Assignment.class));
    }

    @Test
    void testAssignWorker_WorkerNotFound() {
        //AssignmentDTO assignmentDTO = new AssignmentDTO(1L, 101L, "Morning");

        when(workerRepository.findById(1L)).thenReturn(Optional.empty());

        //assertThrows(WorkerNotFoundException.class, () -> assignmentService.assignWorker(assignmentDTO));

        verify(workerRepository, times(1)).findById(1L);
        verify(assignmentRepository, never()).save(any(Assignment.class));
    }

    @Test
    void testGetAssignmentById_Success() {
        Worker worker = new Worker();
        worker.setWorkerId(1L);

        Assignment assignment = new Assignment();
        assignment.setAssignmentId(1L);
        assignment.setZoneId(101L);
        assignment.setShiftTime("Morning");
        assignment.setWorker(worker);

        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));

        AssignmentResponseDTO response = assignmentService.getAssignmentById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getAssignmentId());
        assertEquals(1L, response.getWorkerId());
        assertEquals(101L, response.getZoneId());
        assertEquals("Morning", response.getShiftTime());

        verify(assignmentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAssignmentById_NotFound() {
        when(assignmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AssignmentNotFoundException.class, () -> assignmentService.getAssignmentById(1L));

        verify(assignmentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAssignmentsByWorkerId() {
        Worker worker = new Worker();
        worker.setWorkerId(1L);

        Assignment assignment = new Assignment();
        assignment.setWorker(worker);
        assignment.setZoneId(101L);
        assignment.setShiftTime("Morning");

        when(assignmentRepository.findByWorker_WorkerId(1L)).thenReturn(List.of(assignment));

        List<AssignmentDTO> assignments = assignmentService.getAssignmentsByWorkerId(1L);

        assertNotNull(assignments);
        assertEquals(1, assignments.size());
        //assertEquals(101L, assignments.get(0).getZoneId());
        assertEquals("Morning", assignments.get(0).getShiftTime());

        verify(assignmentRepository, times(1)).findByWorker_WorkerId(1L);
    }

    @Test
    void testUpdateAssignment_Success() {
        Worker worker = new Worker();
        worker.setWorkerId(1L);

        Assignment assignment = new Assignment();
        assignment.setAssignmentId(1L);
        assignment.setWorker(worker);
        assignment.setZoneId(101L);
        assignment.setShiftTime("Morning");

        //AssignmentDTO assignmentDTO = new AssignmentDTO(1L, 102L, "Evening");

        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);

        //AssignmentResponseDTO response = assignmentService.updateAssignment(1L, assignmentDTO);

//        assertNotNull(response);
//        assertEquals(1L, response.getAssignmentId());
//        assertEquals(102L, response.getZoneId());
//        assertEquals("Evening", response.getShiftTime());

        verify(assignmentRepository, times(1)).findById(1L);
        verify(assignmentRepository, times(1)).save(any(Assignment.class));
    }

    @Test
    void testRemoveAssignment_Success() {
        when(assignmentRepository.existsById(1L)).thenReturn(true);

        assignmentService.removeAssignment(1L);

        verify(assignmentRepository, times(1)).existsById(1L);
        verify(assignmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRemoveAssignment_NotFound() {
        when(assignmentRepository.existsById(1L)).thenReturn(false);

        assertThrows(AssignmentNotFoundException.class, () -> assignmentService.removeAssignment(1L));

        verify(assignmentRepository, times(1)).existsById(1L);
        verify(assignmentRepository, never()).deleteById(1L);
    }
}