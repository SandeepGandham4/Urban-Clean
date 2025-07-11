
package com.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import com.example.dto.WasteLogRequestDTO;
import com.example.dto.WasteLogResponseDTO;
import com.example.exception.InvalidRequestException;
import com.example.exception.MissingDateRangeException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.WasteLog;
import com.example.repository.WasteLogRepository;

@ExtendWith(MockitoExtension.class)
class WasteLogServiceTest {

    @Mock
    private WasteLogRepository wasteLogRepository;

    @InjectMocks
    private WasteLogService wasteLogService;

    private WasteLog wasteLog1;
    private WasteLogRequestDTO wasteLogRequestDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        wasteLog1 = new WasteLog();
        wasteLog1.setLogId(1L);
        wasteLog1.setZoneId(101L);
        wasteLog1.setVehicleId(201L);
        wasteLog1.setWorkerId(301L);
        wasteLog1.setWeightCollected(150.5);
        wasteLog1.setCollectionTime(LocalDateTime.of(2023, 1, 15, 10, 30, 0));

        wasteLogRequestDTO = new WasteLogRequestDTO();
        wasteLogRequestDTO.setZoneId(101L);
        wasteLogRequestDTO.setVehicleId(201L);
        wasteLogRequestDTO.setWorkerId(301L);
        wasteLogRequestDTO.setWeightCollected(150.5);
        wasteLogRequestDTO.setCollectionTime(LocalDateTime.of(2023, 1, 15, 10, 30, 0));

        pageable = PageRequest.of(0, 10, Sort.by("logId"));
    }

    // --- createWasteLog Tests ---
    @Test
    void createWasteLog_ValidRequest_ReturnsResponseDTO() {
        when(wasteLogRepository.save(any(WasteLog.class))).thenReturn(wasteLog1);
        WasteLogResponseDTO responseDTO = wasteLogService.createWasteLog(wasteLogRequestDTO);
        assertNotNull(responseDTO);
        assertEquals(wasteLog1.getLogId(), responseDTO.getLogId());
        
    }

    @Test
    void createWasteLog_NullCollectionTime_SetsCurrentTime() {
        wasteLogRequestDTO.setCollectionTime(null);
        when(wasteLogRepository.save(any(WasteLog.class))).thenAnswer(invocation -> {
            WasteLog savedArg = invocation.getArgument(0);
            assertNotNull(savedArg.getCollectionTime());
            savedArg.setLogId(1L);
            return savedArg;
        });
        WasteLogResponseDTO responseDTO = wasteLogService.createWasteLog(wasteLogRequestDTO);
        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getCollectionTime());
    }

    // --- getWasteCollectionReport Tests (Paged) ---
    
    
    
    @Test
    void getWasteCollectionReport_NoFiltersOrDateRange_ReturnsAllPagedWasteLogs() {
        List<WasteLog> wasteLogList = Collections.singletonList(wasteLog1);
        Page<WasteLog> pageOfWasteLogs = new PageImpl<>(wasteLogList, pageable, wasteLogList.size());
        when(wasteLogRepository.findAll(any(Pageable.class))).thenReturn(pageOfWasteLogs);
        Page<WasteLogResponseDTO> resultPage = wasteLogService.getWasteCollectionReport(
                null, null, null, null, null, pageable);
        assertFalse(resultPage.isEmpty());
        assertEquals(1, resultPage.getTotalElements());
        
    }
    
    @Test
    void getWasteCollectionReportBy_ZoneId() {
    	Long zoneId=101L;
        List<WasteLog> workerLogs = Collections.singletonList(wasteLog1);
        Page<WasteLog> pageOfWorkerLogs = new PageImpl<>(workerLogs, pageable, workerLogs.size());
        when(wasteLogRepository.findByZoneId(
                eq(zoneId), any(Pageable.class)))
                .thenReturn(pageOfWorkerLogs);

        Page<WasteLogResponseDTO> resultPage = wasteLogService.getWasteCollectionReport(
                null, null, zoneId, null, null, pageable);
        assertFalse(resultPage.isEmpty());
        assertEquals(1, resultPage.getTotalElements());
        assertEquals(zoneId, resultPage.getContent().get(0).getZoneId());
    }
    
    @Test
    void getWasteCollectionReportByVehicleId() {
    	Long vehicleId=201L;
        List<WasteLog> workerLogs = Collections.singletonList(wasteLog1);
        Page<WasteLog> pageOfWorkerLogs = new PageImpl<>(workerLogs, pageable, workerLogs.size());
        when(wasteLogRepository.findByVehicleId(
                eq(vehicleId), any(Pageable.class)))
                .thenReturn(pageOfWorkerLogs);

        Page<WasteLogResponseDTO> resultPage = wasteLogService.getWasteCollectionReport(
                null, null, null, vehicleId, null, pageable);
        assertFalse(resultPage.isEmpty());
        assertEquals(1, resultPage.getTotalElements());
        assertEquals(vehicleId, resultPage.getContent().get(0).getVehicleId());
    }
    
    @Test
    void getWasteCollectionReportByWorkerId() {
    	Long workerId=301L;
        List<WasteLog> workerLogs = Collections.singletonList(wasteLog1);
        Page<WasteLog> pageOfWorkerLogs = new PageImpl<>(workerLogs, pageable, workerLogs.size());
        when(wasteLogRepository.findByWorkerId(
                eq(workerId), any(Pageable.class)))
                .thenReturn(pageOfWorkerLogs);

        Page<WasteLogResponseDTO> resultPage = wasteLogService.getWasteCollectionReport(
                null, null, null, null, workerId, pageable);
        assertFalse(resultPage.isEmpty());
        assertEquals(1, resultPage.getTotalElements());
        assertEquals(workerId, resultPage.getContent().get(0).getWorkerId());
    }
    
    
    @Test
    void getWasteCollectionReport_With_ZoneId_and_VehicleId() {
    	Long vehicleId=201L;
    	Long zoneId=101L;

        List<WasteLog> filteredList = Collections.singletonList(wasteLog1);
        Page<WasteLog> pageOfFilteredLogs = new PageImpl<>(filteredList, pageable, filteredList.size());
        when(wasteLogRepository.findByZoneIdAndVehicleId(
                eq(zoneId), eq(vehicleId),any(Pageable.class)))
                .thenReturn(pageOfFilteredLogs);

        Page<WasteLogResponseDTO> resultPage = wasteLogService.getWasteCollectionReport(
                null, null, zoneId, vehicleId, null, pageable);
        assertFalse(resultPage.isEmpty());
        assertEquals(1, resultPage.getTotalElements());
        assertEquals(vehicleId, resultPage.getContent().get(0).getVehicleId());
        assertEquals(zoneId, resultPage.getContent().get(0).getZoneId());
        
    }
    
    @Test
    void getWasteCollectionReport_With_VehicleId_And_WorkerId() {
    	Long vehicleId=201L;
    	Long workerId=301L;

        List<WasteLog> filteredList = Collections.singletonList(wasteLog1);
        Page<WasteLog> pageOfFilteredLogs = new PageImpl<>(filteredList, pageable, filteredList.size());
        when(wasteLogRepository.findByVehicleIdAndWorkerId(
                eq(vehicleId), eq(workerId),any(Pageable.class)))
                .thenReturn(pageOfFilteredLogs);

        Page<WasteLogResponseDTO> resultPage = wasteLogService.getWasteCollectionReport(
                null, null, null, vehicleId, workerId, pageable);
        assertFalse(resultPage.isEmpty());
        assertEquals(1, resultPage.getTotalElements());
        assertEquals(vehicleId, resultPage.getContent().get(0).getVehicleId());
        assertEquals(workerId, resultPage.getContent().get(0).getWorkerId());
        
    }
    
    
    @Test
    void getWasteCollectionReport_WithDateRange() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
         
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<WasteLog> filteredList = Collections.singletonList(wasteLog1);
        Page<WasteLog> pageOfFilteredLogs = new PageImpl<>(filteredList, pageable, filteredList.size());
        when(wasteLogRepository.findByCollectionTimeBetween(
                eq(startDateTime), eq(endDateTime),any(Pageable.class)))
                .thenReturn(pageOfFilteredLogs);

        Page<WasteLogResponseDTO> resultPage = wasteLogService.getWasteCollectionReport(
                startDate, endDate, null, null, null, pageable);
        assertFalse(resultPage.isEmpty());
        assertEquals(1, resultPage.getTotalElements());
        
    }

    @Test
    void getWasteCollectionReport_WithDateRangeAndZoneId_ReturnsFilteredPagedLogs() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        Long zoneId = 101L;
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<WasteLog> filteredList = Collections.singletonList(wasteLog1);
        Page<WasteLog> pageOfFilteredLogs = new PageImpl<>(filteredList, pageable, filteredList.size());
        when(wasteLogRepository.findByCollectionTimeBetweenAndZoneId(
                eq(startDateTime), eq(endDateTime), eq(zoneId), any(Pageable.class)))
                .thenReturn(pageOfFilteredLogs);

        Page<WasteLogResponseDTO> resultPage = wasteLogService.getWasteCollectionReport(
                startDate, endDate, zoneId, null, null, pageable);
        assertFalse(resultPage.isEmpty());
        assertEquals(1, resultPage.getTotalElements());
        
    }

    @Test
    void getWasteCollectionReport_NoRecordsFound_ThrowsResourceNotFoundException() {
        when(wasteLogRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                wasteLogService.getWasteCollectionReport(null, null, null, null, null, pageable));
    } 

    @Test 
    void getWasteCollectionReport_InvalidDateRange_ThrowsException() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        assertThrows(MissingDateRangeException.class, () ->
                wasteLogService.getWasteCollectionReport(startDate, null, null, null, null, pageable)); // Missing endDate
        LocalDate invalidStartDate = LocalDate.of(2023, 1, 31);
        LocalDate invalidEndDate = LocalDate.of(2023, 1, 1);
        assertThrows(InvalidRequestException.class, () ->
                wasteLogService.getWasteCollectionReport(invalidStartDate, invalidEndDate, null, null, null, pageable)); // StartDate after EndDate
    }


    // --- getWasteCollectionReportByWorker Tests (Paged) ---
    
    @Test
    void getWasteCollectionReportByWorker_valid_workerId() {
    	Long workerId=1L;
        List<WasteLog> workerLogs = Collections.singletonList(wasteLog1);
        Page<WasteLog> pageOfWorkerLogs = new PageImpl<>(workerLogs, pageable, workerLogs.size());
        when(wasteLogRepository.findByWorkerId(
                eq(workerId), any(Pageable.class)))
                .thenReturn(pageOfWorkerLogs);

        Page<WasteLogResponseDTO> resultPage = wasteLogService.getWasteCollectionReportByWorker(
                workerId, null, null, null, null, pageable);
        assertFalse(resultPage.isEmpty());
        assertEquals(1, resultPage.getTotalElements());
       
    }
    
    @Test
    void getWasteCollectionReportByWorker_invalid_workerId_ThrowsResourceNotFoundException() {
        Long workerId = 999L;
        when(wasteLogRepository.findByWorkerId(eq(workerId), any(Pageable.class))).thenReturn(Page.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                wasteLogService.getWasteCollectionReportByWorker(workerId, null, null, null, null, pageable));
    }
    
    
    @Test 
    void getWasteCollectionReportByWorker_InvalidDateRange_ThrowsException() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        assertThrows(MissingDateRangeException.class, () ->
                wasteLogService.getWasteCollectionReportByWorker( null, startDate,null, null, null, pageable)); // Missing endDate
        LocalDate invalidStartDate = LocalDate.of(2023, 1, 31);
        LocalDate invalidEndDate = LocalDate.of(2023, 1, 1);
        assertThrows(InvalidRequestException.class, () ->
                wasteLogService.getWasteCollectionReportByWorker( null,invalidStartDate, invalidEndDate, null, null, pageable)); // StartDate after EndDate
    }


    
    @Test
    void getWasteCollectionReportByWorker_WithWorkerIdAndDateRange_ReturnsPagedWasteLogs() {
        Long workerId = 301L;
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        

        List<WasteLog> workerLogs = Collections.singletonList(wasteLog1);
        Page<WasteLog> pageOfWorkerLogs = new PageImpl<>(workerLogs, pageable, workerLogs.size());
        when(wasteLogRepository.findByWorkerIdAndCollectionTimeBetween(
                eq(workerId), eq(startDateTime), eq(endDateTime), any(Pageable.class)))
                .thenReturn(pageOfWorkerLogs);

        Page<WasteLogResponseDTO> resultPage = wasteLogService.getWasteCollectionReportByWorker(
                workerId, startDate, endDate, null, null, pageable);
        assertFalse(resultPage.isEmpty());
        assertEquals(1, resultPage.getTotalElements());
       
    }
    @Test
    void getWasteCollectionReportByZoneId() {
    	Long zoneId=101L;
        List<WasteLog> workerLogs = Collections.singletonList(wasteLog1);
        Page<WasteLog> pageOfWorkerLogs = new PageImpl<>(workerLogs, pageable, workerLogs.size());
        when(wasteLogRepository.findByZoneId(
                eq(zoneId), any(Pageable.class)))
                .thenReturn(pageOfWorkerLogs);

        Page<WasteLogResponseDTO> resultPage = wasteLogService.getWasteCollectionReport(
                null, null, zoneId, null, null, pageable);
        assertFalse(resultPage.isEmpty());
        assertEquals(1, resultPage.getTotalElements());
        assertEquals(zoneId, resultPage.getContent().get(0).getZoneId());
    }
    
    @Test
    void getWasteCollectionReportByWorker_WithWorkerIdAndDateRange_And_ZoneId_And_VehicleId_ReturnsPagedWasteLogs() {
        Long workerId = 301L;
        Long zoneId = 101L;
        Long vehicleId = 201L;
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        

        List<WasteLog> workerLogs = Collections.singletonList(wasteLog1);
        Page<WasteLog> pageOfWorkerLogs = new PageImpl<>(workerLogs, pageable, workerLogs.size());
        when(wasteLogRepository.findByWorkerIdAndCollectionTimeBetweenAndZoneIdAndVehicleId(
                eq(workerId), eq(startDateTime), eq(endDateTime), eq(zoneId),eq(vehicleId),any(Pageable.class)))
                .thenReturn(pageOfWorkerLogs);

        Page<WasteLogResponseDTO> resultPage = wasteLogService.getWasteCollectionReportByWorker(
                workerId, startDate, endDate, zoneId, vehicleId, pageable);
        assertFalse(resultPage.isEmpty());
        assertEquals(1, resultPage.getTotalElements());
        assertEquals(workerId, resultPage.getContent().get(0).getWorkerId());
        assertEquals(zoneId, resultPage.getContent().get(0).getZoneId());
        assertEquals(vehicleId, resultPage.getContent().get(0).getVehicleId());
       
    }
    
    @Test
    void getWasteCollectionReportByWorker_WithWorkerIdAndDateRange_And_ZoneId_ReturnsPagedWasteLogs() {
        Long workerId = 301L;
        Long zoneId = 101L;
      
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        

        List<WasteLog> workerLogs = Collections.singletonList(wasteLog1);
        Page<WasteLog> pageOfWorkerLogs = new PageImpl<>(workerLogs, pageable, workerLogs.size());
        when(wasteLogRepository.findByWorkerIdAndCollectionTimeBetweenAndZoneId(
                eq(workerId), eq(startDateTime), eq(endDateTime), eq(zoneId),any(Pageable.class)))
                .thenReturn(pageOfWorkerLogs);

        Page<WasteLogResponseDTO> resultPage = wasteLogService.getWasteCollectionReportByWorker(
                workerId, startDate, endDate, zoneId, null, pageable);
        assertFalse(resultPage.isEmpty());
        assertEquals(1, resultPage.getTotalElements());
        assertEquals(workerId, resultPage.getContent().get(0).getWorkerId());
        assertEquals(zoneId, resultPage.getContent().get(0).getZoneId());
        
       
    }
    
    @Test
    void getWasteCollectionReportByWorker_WithWorkerIdAndDateRange_And_VehicleId_ReturnsPagedWasteLogs() {
        Long workerId = 301L;
    
        Long vehicleId = 201L;
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        

        List<WasteLog> workerLogs = Collections.singletonList(wasteLog1);
        Page<WasteLog> pageOfWorkerLogs = new PageImpl<>(workerLogs, pageable, workerLogs.size());
        when(wasteLogRepository.findByWorkerIdAndCollectionTimeBetweenAndVehicleId(
                eq(workerId), eq(startDateTime), eq(endDateTime),eq(vehicleId),any(Pageable.class)))
                .thenReturn(pageOfWorkerLogs);

        Page<WasteLogResponseDTO> resultPage = wasteLogService.getWasteCollectionReportByWorker(
                workerId, startDate, endDate, null, vehicleId, pageable);
        assertFalse(resultPage.isEmpty());
        assertEquals(1, resultPage.getTotalElements());
        assertEquals(workerId, resultPage.getContent().get(0).getWorkerId());
        
        assertEquals(vehicleId, resultPage.getContent().get(0).getVehicleId());
       
    }
    
    
    
    
    
//    @Test
//    void getWasteCollectionReportByVehicleId_with_workerId() {
//    	Long vehicleId=201L;
//    	long workerId=301L;
//        List<WasteLog> workerLogs = Collections.singletonList(wasteLog1);
//        Page<WasteLog> pageOfWorkerLogs = new PageImpl<>(workerLogs, pageable, workerLogs.size());
//        when(wasteLogRepository.findByVehicleId(
//                eq(vehicleId), any(Pageable.class)))
//                .thenReturn(pageOfWorkerLogs);
//
//        Page<WasteLogResponseDTO> resultPage = wasteLogService.getWasteCollectionReport(
//                null, null, null, vehicleId, null, pageable);
//        assertFalse(resultPage.isEmpty());
//        assertEquals(1, resultPage.getTotalElements());
//        assertEquals(vehicleId, resultPage.getContent().get(0).getVehicleId());
//    }
//    
  



} 
