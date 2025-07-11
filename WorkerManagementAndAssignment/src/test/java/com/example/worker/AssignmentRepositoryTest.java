package com.example.worker;

import com.example.workermanagement.entity.Assignment;
import com.example.workermanagement.entity.Worker;
import com.example.workermanagement.repo.AssignmentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssignmentRepositoryTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @InjectMocks
    private AssignmentRepositoryTest assignmentRepositoryTest;

    private Assignment assignment;
    private Worker worker;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        worker = new Worker();
        worker.setWorkerId(1L);
        worker.setName("John Doe");
        worker.setContactInfo("john.doe@example.com");
        //worker.setRole("Garbage Collector");

        assignment = new Assignment();
        assignment.setAssignmentId(1L);
        assignment.setZoneId(101L);
        assignment.setShiftTime("Morning");
        assignment.setWorker(worker);
    }

    @Test
    void givenWorkerId_whenFindByWorkerId_thenReturnAssignmentList() {
        when(assignmentRepository.findByWorker_WorkerId(1L)).thenReturn(List.of(assignment));

        List<Assignment> assignments = assignmentRepository.findByWorker_WorkerId(1L);

        assertNotNull(assignments);
        assertEquals(1, assignments.size());
        assertEquals(101L, assignments.get(0).getZoneId());
        assertEquals("Morning", assignments.get(0).getShiftTime());

        verify(assignmentRepository, times(1)).findByWorker_WorkerId(1L);
    }

    @Test
    void givenZoneId_whenFindByZoneId_thenReturnAssignment() {
        when(assignmentRepository.findByZoneId(101L)).thenReturn(Optional.of(assignment));

        Optional<Assignment> result = assignmentRepository.findByZoneId(101L);

        assertTrue(result.isPresent());
        assertEquals(101L, result.get().getZoneId());
        assertEquals("Morning", result.get().getShiftTime());

        verify(assignmentRepository, times(1)).findByZoneId(101L);
    }

    @Test
    void givenAssignment_whenSave_thenReturnSavedAssignment() {
        when(assignmentRepository.save(assignment)).thenReturn(assignment);

        Assignment savedAssignment = assignmentRepository.save(assignment);

        assertNotNull(savedAssignment);
        assertEquals(1L, savedAssignment.getAssignmentId());
        assertEquals(101L, savedAssignment.getZoneId());
        assertEquals("Morning", savedAssignment.getShiftTime());

        verify(assignmentRepository, times(1)).save(assignment);
    }

    @Test
    void givenAssignmentId_whenDeleteById_thenVerifyDeletion() {
        doNothing().when(assignmentRepository).deleteById(1L);

        assignmentRepository.deleteById(1L);

        verify(assignmentRepository, times(1)).deleteById(1L);
    }
}