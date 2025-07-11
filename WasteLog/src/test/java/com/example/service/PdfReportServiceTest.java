package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.dto.WasteLogResponseDTO;
import com.example.exception.InvalidRequestException;
import com.example.exception.MissingDateRangeException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.WasteLog;
import com.example.repository.WasteLogRepository;

@ExtendWith(MockitoExtension.class)
class PdfReportServiceTest {
	
    @Mock
    private WasteLogRepository wasteLogRepository;

    @InjectMocks
    private WasteLogService wasteLogService;

    @InjectMocks
    private PdfReportService pdfReportService;

    private WasteLog wasteLog1;
    private WasteLogResponseDTO wasteLogResponseDTO1;

    @BeforeEach
    void setUp() {
    	
    	 wasteLog1 = new WasteLog();
         wasteLog1.setLogId(1L);
         wasteLog1.setZoneId(101L);
         wasteLog1.setVehicleId(201L);
         wasteLog1.setWorkerId(301L);
         wasteLog1.setWeightCollected(150.5);
         wasteLog1.setCollectionTime(LocalDateTime.of(2023, 1, 15, 10, 30, 0));
    	
        wasteLogResponseDTO1 = new WasteLogResponseDTO(
            1L, 101L, 201L, 301L, 150.5, LocalDateTime.of(2023, 1, 15, 10, 30, 0)
        );
    }

    // --- PdfReportService Tests ---
    @Test
    void getWasteCollectionReportPdf_NoFilters_ReturnsAllWasteLogsAsList() {
        List<WasteLog> wasteLogList = Collections.singletonList(wasteLog1);
        when(wasteLogRepository.findAll()).thenReturn(wasteLogList);
        List<WasteLogResponseDTO> resultList = pdfReportService.getWasteCollectionReportPdf(
                null, null, null, null, null);
        assertFalse(resultList.isEmpty());
        assertEquals(1, resultList.size());
        
    }

    @Test
    void getWasteCollectionReportPdf_WithDateRangeAndZoneId_ReturnsFilteredWasteLogsAsList() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        Long zoneId = 101L;
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<WasteLog> filteredList = Collections.singletonList(wasteLog1);
        when(wasteLogRepository.findByCollectionTimeBetweenAndZoneId(
                eq(startDateTime), eq(endDateTime), eq(zoneId)))
                .thenReturn(filteredList);

        List<WasteLogResponseDTO> resultList = pdfReportService.getWasteCollectionReportPdf(
                startDate, endDate, zoneId, null, null);
        assertFalse(resultList.isEmpty());
        assertEquals(1, resultList.size());
       
    }
    
    @Test
    void getWasteCollectionReportPdf_InvalidDateRange_ThrowsException() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        assertThrows(MissingDateRangeException.class, () ->
                pdfReportService.getWasteCollectionReportPdf(startDate, null, null, null, null)); // Missing endDate
        LocalDate invalidStartDate = LocalDate.of(2023, 1, 31);
        LocalDate invalidEndDate = LocalDate.of(2023, 1, 1);
        assertThrows(InvalidRequestException.class, () ->
                pdfReportService.getWasteCollectionReportPdf(invalidStartDate, invalidEndDate, null, null, null)); // StartDate after EndDate
    }

    @Test
    void getWasteCollectionReportPdf_WithDateRange() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
         
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<WasteLog> filteredList = Collections.singletonList(wasteLog1);
       
        when(wasteLogRepository.findByCollectionTimeBetween(
                eq(startDateTime), eq(endDateTime)))
                .thenReturn(filteredList);

        List<WasteLogResponseDTO> resultList = pdfReportService.getWasteCollectionReportPdf(
                startDate, endDate, null, null, null);
        assertFalse(resultList.isEmpty());
        assertEquals(1, resultList.size());
        
    }
    
    @Test
    void getWasteCollectionReportPdf_With_ZoneId_and_VehicleId() {
    	Long vehicleId=201L;
    	Long zoneId=101L;

        List<WasteLog> filteredList = Collections.singletonList(wasteLog1);
        
        when(wasteLogRepository.findByZoneIdAndVehicleId(
                eq(zoneId), eq(vehicleId)))
                .thenReturn(filteredList);

        List<WasteLogResponseDTO> resultPage = pdfReportService.getWasteCollectionReportPdf(
                null, null, zoneId, vehicleId, null);
        assertFalse(resultPage.isEmpty());
        assertEquals(1, resultPage.size());
        assertEquals(vehicleId, resultPage.get(0).getVehicleId());
        assertEquals(zoneId, resultPage.get(0).getZoneId());
        
    }
    
    @Test
    void getWasteCollectionReportPdf_With_VehicleId_And_WorkerId() {
    	Long vehicleId=201L;
    	Long workerId=301L;

        List<WasteLog> filteredList = Collections.singletonList(wasteLog1);
       
        when(wasteLogRepository.findByVehicleIdAndWorkerId(
                eq(vehicleId), eq(workerId)))
                .thenReturn(filteredList);

        List<WasteLogResponseDTO> resultList = pdfReportService.getWasteCollectionReportPdf(
                null, null, null, vehicleId, workerId);
        assertFalse(resultList.isEmpty());
        assertEquals(1, resultList.size());
        assertEquals(vehicleId, resultList.get(0).getVehicleId());
        assertEquals(workerId, resultList.get(0).getWorkerId());
        
    }
    
    @Test
    void getWasteCollectionReportPdf_with_valid_workerId() {
    	Long workerId=301L;
        List<WasteLog> workerLogs = Collections.singletonList(wasteLog1);
        
        when(wasteLogRepository.findByWorkerId(
                eq(workerId)))
                .thenReturn(workerLogs);

        List<WasteLogResponseDTO> resultList = pdfReportService.getWasteCollectionReportPdf(
                null, null, null, null, workerId);
        assertFalse(resultList.isEmpty());
        assertEquals(1, resultList.size());
        assertEquals(301, resultList.get(0).getWorkerId());
       
    }
    
    @Test
    void getWasteCollectionReportPdf_with_vehicleId() {
    	Long vehicleId=201L;
        List<WasteLog> workerLogs = Collections.singletonList(wasteLog1);
        
        when(wasteLogRepository.findByVehicleId(
                eq(vehicleId)))
                .thenReturn(workerLogs);

        List<WasteLogResponseDTO> resultList = pdfReportService.getWasteCollectionReportPdf(
                null, null, null, vehicleId, null);
        assertFalse(resultList.isEmpty());
        assertEquals(1, resultList.size());
        assertEquals(201, resultList.get(0).getVehicleId());
       
    }
    
    @Test
    void getWasteCollectionReportPdf_with_zoneId() {
    	Long zoneId=101L;
        List<WasteLog> workerLogs = Collections.singletonList(wasteLog1);
        
        when(wasteLogRepository.findByZoneId(
                eq(zoneId)))
                .thenReturn(workerLogs);

        List<WasteLogResponseDTO> resultList = pdfReportService.getWasteCollectionReportPdf(
                null, null, zoneId, null, null);
        assertFalse(resultList.isEmpty());
        assertEquals(1, resultList.size());
        assertEquals(101, resultList.get(0).getZoneId());
       
    }
    

    
    @Test
    void getWasteCollectionReportPdf_NoRecordsFound_ThrowsResourceNotFoundException() {
        when(wasteLogRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class, () ->
                pdfReportService.getWasteCollectionReportPdf(null, null, null, null, null));
    }
    
    @Test
    void generateWasteLogReport_ValidData_ReturnsPdfBytes() {
 
        List<WasteLogResponseDTO> mockWasteLogs = Collections.singletonList(wasteLogResponseDTO1);

        byte[] pdfBytes = pdfReportService.generateWasteLogReport(mockWasteLogs);

        assertNotNull(pdfBytes);
        
        assertTrue(pdfBytes.length > 0);
    }

    @Test
    void generateWasteLogReport_EmptyList_ReturnsPdfBytes() {
        List<WasteLogResponseDTO> emptyList = Collections.emptyList();

        byte[] pdfBytes = pdfReportService.generateWasteLogReport(emptyList);

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
    }

    @Test
    void generateWasteLogReport_ThrowsRuntimeExceptionOnError() {
      
        assertThrows(RuntimeException.class, () -> {
            pdfReportService.generateWasteLogReport(null);
        });
    }
}
