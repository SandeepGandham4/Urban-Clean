package com.example.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString; // Import for containsString matcher


import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.Collections;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders; // Used for standalone setup

import com.example.dto.WasteLogRequestDTO;
import com.example.dto.WasteLogResponseDTO;
import com.example.exception.InvalidRequestException;
import com.example.exception.MissingDateRangeException;
import com.example.exception.ResourceNotFoundException;
import com.example.exception.GlobalExceptionHandler; // Import your GlobalExceptionHandler
import com.example.service.PdfReportService;
import com.example.service.WasteLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ExtendWith(MockitoExtension.class) // Use MockitoExtension for @Mock and @InjectMocks
class WasteLogControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WasteLogService wasteLogService;

    @Mock
    private PdfReportService pdfReportService;

    @InjectMocks
    private WasteLogController wasteLogController;

    private ObjectMapper objectMapper;

    
    private GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders.standaloneSetup(wasteLogController)
                .setControllerAdvice(globalExceptionHandler) 
                .build();
    }

    // --- POST /waste-logs Tests ---
    @Test
    void logWasteCollection_ValidRequest_ReturnsCreated() throws Exception {
        WasteLogRequestDTO requestDTO = new WasteLogRequestDTO(1L, 10L, 100L, 250.0, LocalDateTime.now());
        WasteLogResponseDTO mockResponseDTO = new WasteLogResponseDTO(1L, 1L, 10L, 100L, 250.0, requestDTO.getCollectionTime());
        when(wasteLogService.createWasteLog(any(WasteLogRequestDTO.class))).thenReturn(mockResponseDTO);

        mockMvc.perform(post("/api/urbanclean/v1/waste-logs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.logId").value(1L));
    }

    @Test
    void logWasteCollection_InvalidRequest_ReturnsBadRequest() throws Exception {
        WasteLogRequestDTO invalidRequestDTO = new WasteLogRequestDTO(); // Missing @NotNull fields
        // We don't mock the service here because Spring's validation layer (before the service) handles this.
        mockMvc.perform(post("/api/urbanclean/v1/waste-logs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    // --- GET /reports/waste-collection Tests (Paged) ---
    @Test
    void getWasteCollectionReport_WithFilters_ReturnsOkAndPagedData() throws Exception {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        Long zoneId = 1L;
        WasteLogResponseDTO log = new WasteLogResponseDTO(1L, zoneId, 10L, 100L, 200.0, LocalDateTime.of(2023, 1, 15, 10, 0));
        PageImpl<WasteLogResponseDTO> page = new PageImpl<>(Collections.singletonList(log), PageRequest.of(0, 10, Sort.by("zoneId")), 1);

        when(wasteLogService.getWasteCollectionReport(
                eq(startDate), eq(endDate), eq(zoneId), eq(null), eq(null), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/urbanclean/v1/reports/waste-collection")
                .param("startDate", "2023-01-01")
                .param("endDate", "2023-01-31")
                .param("zoneId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void getWasteCollectionReport_NoRecordsFound_ReturnsNotFound() throws Exception {
        // Service throws the exception, controller re-throws it, and the handler catches it.
        String expectedMessage = "No waste collection records found for the given filters.";
        when(wasteLogService.getWasteCollectionReport(any(), any(), any(), any(), any(), any(Pageable.class)))
                .thenThrow(new ResourceNotFoundException(expectedMessage));
        mockMvc.perform(get("/api/urbanclean/v1/reports/waste-collection")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Resource Not Found: " + expectedMessage)));
    }

    @Test
    void getWasteCollectionReport_MissingDateRange_ReturnsBadRequest() throws Exception {
        // Service throws the exception, controller re-throws it, and the handler catches it.
        String expectedMessage = "Both startDate and endDate must be provided together.";
        when(wasteLogService.getWasteCollectionReport(any(), eq(null), any(), any(), any(), any(Pageable.class)))
                .thenThrow(new MissingDateRangeException(expectedMessage));
        mockMvc.perform(get("/api/urbanclean/v1/reports/waste-collection")
                .param("startDate", "2023-01-01") // Only one date param
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Missing Date Range: " + expectedMessage)));
    }

    @Test
    void getWasteCollectionReport_StartDateAfterEndDate_ReturnsBadRequest() throws Exception {
        // Service throws the exception, controller re-throws it, and the handler catches it.
        String expectedMessage = "Start date must be before end date.";
        when(wasteLogService.getWasteCollectionReport(any(), any(), any(), any(), any(), any(Pageable.class)))
                .thenThrow(new InvalidRequestException(expectedMessage));
        mockMvc.perform(get("/api/urbanclean/v1/reports/waste-collection")
                .param("startDate", "2023-01-31")
                .param("endDate", "2023-01-01")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid Request: " + expectedMessage)));
    }

    // --- GET /reports/worker/{id} Tests (Worker-Specific Paged Report) ---
    @Test
    void getWasteCollectionByWorkerId_ValidWorkerId_ReturnsOkAndPagedData() throws Exception {
        Long workerId = 301L;
        WasteLogResponseDTO workerLog = new WasteLogResponseDTO(5L, 10L, 100L, workerId, 600.0, LocalDateTime.now());
        PageImpl<WasteLogResponseDTO> page = new PageImpl<>(Collections.singletonList(workerLog), PageRequest.of(0, 10, Sort.by("zoneId")), 1);
        when(wasteLogService.getWasteCollectionReportByWorker(
                eq(workerId), eq(null), eq(null), eq(null), eq(null), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/urbanclean/v1/reports/worker/{id}", workerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].workerId").value(workerId));
    }

    @Test
    void getWasteCollectionByWorkerId_NoRecordsFound_ReturnsNotFound() throws Exception {
        Long workerId = 9999L;
        String expectedMessage = "No waste collection records found for the given filters.";
        when(wasteLogService.getWasteCollectionReportByWorker(
                eq(workerId), any(), any(), any(), any(), any(Pageable.class)))
                .thenThrow(new ResourceNotFoundException(expectedMessage));
        mockMvc.perform(get("/api/urbanclean/v1/reports/worker/{id}", workerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Resource Not Found: " + expectedMessage)));
    }

    // --- GET /reports/waste-collection/pdf Tests (PDF Report) ---
	
	 

    @Test
    void downloadWasteCollectionReportPdf_NoRecordsFound_ReturnsNotFound() throws Exception {
        String expectedMessage = "No waste collection records found for the given filters.";
        when(pdfReportService.getWasteCollectionReportPdf(any(), any(), any(), any(), any()))
                .thenThrow(new ResourceNotFoundException(expectedMessage));
        mockMvc.perform(get("/api/urbanclean/v1/reports/waste-collection/pdf")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Resource Not Found: " + expectedMessage)));
    }

    @Test
    void downloadWasteCollectionReportPdf_MissingDateRange_ReturnsBadRequest() throws Exception {
        String expectedMessage = "Both startDate and endDate must be provided together.";
        when(pdfReportService.getWasteCollectionReportPdf(any(), eq(null), any(), any(), any()))
                .thenThrow(new MissingDateRangeException(expectedMessage));
        mockMvc.perform(get("/api/urbanclean/v1/reports/waste-collection/pdf")
                .param("startDate", "2023-01-01")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Missing Date Range: " + expectedMessage)));
    }
}
