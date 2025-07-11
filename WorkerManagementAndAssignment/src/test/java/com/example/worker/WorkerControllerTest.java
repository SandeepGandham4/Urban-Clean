//package com.example.worker;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import com.example.workermanagement.controller.WorkerController;
//import com.example.workermanagement.dto.WorkerDTO;
//import com.example.workermanagement.dto.WorkerResponseDTO;
//import com.example.workermanagement.service.WorkerService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@ExtendWith(MockitoExtension.class)
//class WorkerControllerTest {
//
//	@Mock
//	private WorkerService workerService;
//
//	@InjectMocks
//	private WorkerController workerController;
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	private WorkerDTO workerDTO;
//	private WorkerResponseDTO workerResponseDTO;
//
//	@BeforeEach
//	void setUp() {
//		mockMvc = MockMvcBuilders.standaloneSetup(workerController).build();
//		workerDTO = new WorkerDTO("Alice", "alice@example.com", "Driver");
//		workerResponseDTO = new WorkerResponseDTO(1L, "Alice", "alice@example.com", "Driver");
//	}
//
//	@Test
//	void testCreateWorker() throws Exception {
//		when(workerService.createWorker(any())).thenReturn(workerResponseDTO);
//
//		mockMvc.perform(
//				post("/api/v1/workers").contentType(MediaType.APPLICATION_JSON).content(asJsonString(workerDTO)))
//				.andExpect(status().isOk());
//
//		verify(workerService).createWorker(any());
//	}
//
//	@Test
//	void testGetWorkerById() throws Exception {
//		when(workerService.getWorkerById(1L)).thenReturn(workerResponseDTO);
//
//		mockMvc.perform(get("/api/v1/workers/1")).andExpect(status().isOk());
//
//		verify(workerService).getWorkerById(1L);
//	}
//
//	@Test
//	void testGetAllWorkers() throws Exception {
//		when(workerService.getAllWorkers()).thenReturn(List.of(workerResponseDTO));
//
//		mockMvc.perform(get("/api/v1/workers")).andExpect(status().isOk());
//
//		verify(workerService).getAllWorkers();
//	}
//
//	@Test
//	void testGetWorkersByName() throws Exception {
//		when(workerService.getWorkerByName("Alice")).thenReturn(List.of(workerResponseDTO));
//
//		mockMvc.perform(get("/api/v1/workers/name/Alice")).andExpect(status().isOk());
//
//		verify(workerService).getWorkerByName("Alice");
//	}
//
//	@Test
//	void testGetWorkersByRole() throws Exception {
//		when(workerService.getWorkersByRole("Driver")).thenReturn(List.of(workerResponseDTO));
//
//		mockMvc.perform(get("/api/v1/workers/role/Driver")).andExpect(status().isOk());
//
//		verify(workerService).getWorkersByRole("Driver");
//	}
//
//	@Test
//	void testUpdateWorker() throws Exception {
//		when(workerService.updateWorker(eq(1L), any())).thenReturn(workerResponseDTO);
//
//		mockMvc.perform(
//				put("/api/v1/workers/1").contentType(MediaType.APPLICATION_JSON).content(asJsonString(workerDTO)))
//				.andExpect(status().isOk());
//
//		verify(workerService).updateWorker(eq(1L), any());
//	}
//
//	@Test
//	void testDeleteWorker() throws Exception {
//		doNothing().when(workerService).deleteWorker(1L);
//
//		mockMvc.perform(delete("/api/v1/workers/1")).andExpect(status().isOk());
//
//		verify(workerService).deleteWorker(1L);
//	}
//
//	// Helper to convert object to JSON string
//	private static String asJsonString(final Object obj) {
//		try {
//			return new ObjectMapper().writeValueAsString(obj);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//}
