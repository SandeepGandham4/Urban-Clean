package com.example.worker;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.workermanagement.controller.AssignmentController;
import com.example.workermanagement.dto.AssignmentDTO;
import com.example.workermanagement.dto.AssignmentResponseDTO;
import com.example.workermanagement.service.AssignmentService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class AssignmentControllerTest {

    @Mock
    private AssignmentService assignmentService;

    @InjectMocks
    private AssignmentController assignmentController;

    @Autowired
    private MockMvc mockMvc;

    private AssignmentDTO assignmentDTO;
    private AssignmentResponseDTO assignmentResponseDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(assignmentController).build();
        //assignmentDTO = new AssignmentDTO( 10L, 1L, "Morning Shift");
        assignmentResponseDTO = new AssignmentResponseDTO( 1L, 10L,10L, "Morning Shift");
    }

    @Test
    void testAssignWorker() throws Exception {
        //when(assignmentService.assignWorker(any())).thenReturn(assignmentResponseDTO);

        mockMvc.perform(post("/api/v1/assignments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(assignmentDTO)))
                .andExpect(status().isOk());

        //verify(assignmentService).assignWorker(any());
    }

    @Test
    void testGetAssignmentById() throws Exception {
        when(assignmentService.getAssignmentById(10L)).thenReturn(assignmentResponseDTO);

        mockMvc.perform(get("/api/v1/assignments/10"))
                .andExpect(status().isOk());

        verify(assignmentService).getAssignmentById(10L);
    }

    @Test
    void testGetAssignmentsByWorkerId() throws Exception {
        when(assignmentService.getAssignmentsByWorkerId(1L)).thenReturn(List.of(assignmentDTO));

        mockMvc.perform(get("/api/v1/assignments/worker/1"))
                .andExpect(status().isOk());

        verify(assignmentService).getAssignmentsByWorkerId(1L);
    }

    @Test
    void testGetAllAssignments() throws Exception {
        //when(assignmentService.getAllAssignments()).thenReturn(List.of(assignmentDTO));

        mockMvc.perform(get("/api/v1/assignments"))
                .andExpect(status().isOk());

        verify(assignmentService).getAllAssignments();
    }

    @Test
    void testUpdateAssignment() throws Exception {
        when(assignmentService.updateAssignment(eq(10L), any())).thenReturn(assignmentResponseDTO);

        mockMvc.perform(put("/api/v1/assignments/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(assignmentDTO)))
                .andExpect(status().isOk());

        verify(assignmentService).updateAssignment(eq(10L), any());
    }

    @Test
    void testRemoveAssignment() throws Exception {
        doNothing().when(assignmentService).removeAssignment(10L);

        mockMvc.perform(delete("/api/v1/assignments/10"))
                .andExpect(status().isOk());

        verify(assignmentService).removeAssignment(10L);
    }

    private static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

