package com.example.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.model.WasteLog;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WasteLogRepositoryTest {

    @Autowired
    private WasteLogRepository wasteLogRepository;

    private WasteLog wasteLog1;
    private WasteLog wasteLog2;
    private WasteLog wasteLog3; // Added for more diverse data

    @BeforeEach
    void setUp() {
        wasteLogRepository.deleteAll(); // Clear the repository before each test

        // Initialize and save essential test data
        wasteLog1 = new WasteLog();
        wasteLog1.setZoneId(10L);
        wasteLog1.setVehicleId(100L);
        wasteLog1.setWorkerId(1L);
        wasteLog1.setWeightCollected(500.0);
        wasteLog1.setCollectionTime(LocalDateTime.of(2023, 5, 10, 9, 0, 0)); // May 10th
        wasteLog1 = wasteLogRepository.save(wasteLog1);

        wasteLog2 = new WasteLog();
        wasteLog2.setZoneId(20L);
        wasteLog2.setVehicleId(200L);
        wasteLog2.setWorkerId(2L);
        wasteLog2.setWeightCollected(750.0);
        wasteLog2.setCollectionTime(LocalDateTime.of(2023, 5, 15, 14, 30, 0)); // May 15th
        wasteLog2 = wasteLogRepository.save(wasteLog2);

        wasteLog3 = new WasteLog();
        wasteLog3.setZoneId(10L); // Same zone as wasteLog1
        wasteLog3.setVehicleId(100L); // Same vehicle as wasteLog1
        wasteLog3.setWorkerId(1L); // Same worker as wasteLog1
        wasteLog3.setWeightCollected(600.0);
        wasteLog3.setCollectionTime(LocalDateTime.of(2023, 5, 12, 11, 0, 0)); // May 12th
        wasteLog3 = wasteLogRepository.save(wasteLog3);
    }

    // --- Basic CRUD Tests (from previous versions) ---

    @Test
    void givenWasteLog_whenSave_thenReturnsSavedWasteLog() {
        WasteLog newWasteLog = new WasteLog();
        newWasteLog.setZoneId(30L);
        newWasteLog.setVehicleId(300L);
        newWasteLog.setWorkerId(3L);
        newWasteLog.setWeightCollected(1000.0);
        newWasteLog.setCollectionTime(LocalDateTime.of(2023, 6, 1, 10, 0, 0));
        WasteLog savedWasteLog = wasteLogRepository.save(newWasteLog);
        assertNotNull(savedWasteLog.getLogId());
        assertEquals(30L, savedWasteLog.getZoneId());
    }

    @Test
    void givenId_whenFindById_thenReturnsWasteLog() {
        Optional<WasteLog> foundWasteLog = wasteLogRepository.findById(wasteLog1.getLogId());
        assertTrue(foundWasteLog.isPresent());
        assertEquals(wasteLog1.getLogId(), foundWasteLog.get().getLogId());
    }

    @Test
    void whenFindAll_thenReturnsAllWasteLogs() {
        List<WasteLog> wasteLogs = wasteLogRepository.findAll();
        assertEquals(3, wasteLogs.size());
        assertTrue(wasteLogs.contains(wasteLog1));
        assertTrue(wasteLogs.contains(wasteLog2));
        assertTrue(wasteLogs.contains(wasteLog3));
    }


    // --- Pageable Methods Tests ---

    @Test
    void findByCollectionTimeBetween_Pageable_ReturnsCorrectPage() {
        LocalDateTime startDate = LocalDate.of(2023, 5, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(2023, 5, 31).atTime(23, 59, 59);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("collectionTime"));
        Page<WasteLog> page = wasteLogRepository.findByCollectionTimeBetween(startDate, endDate, pageable);
        assertEquals(3, page.getTotalElements());
        assertEquals(wasteLog1.getLogId(), page.getContent().get(0).getLogId()); // Sorted by time
    }

    @Test
    void findByZoneId_Pageable_ReturnsCorrectPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<WasteLog> page = wasteLogRepository.findByZoneId(10L, pageable);
        assertEquals(2, page.getTotalElements()); // wasteLog1 and wasteLog3
        assertTrue(page.getContent().contains(wasteLog1));
        assertTrue(page.getContent().contains(wasteLog3));
    }

    @Test
    void findByVehicleId_Pageable_ReturnsCorrectPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<WasteLog> page = wasteLogRepository.findByVehicleId(200L, pageable);
        assertEquals(1, page.getTotalElements());
        assertEquals(wasteLog2.getLogId(), page.getContent().get(0).getLogId());
    }

    @Test
    void findByWorkerId_Pageable_ReturnsCorrectPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<WasteLog> page = wasteLogRepository.findByWorkerId(1L, pageable);
        assertEquals(2, page.getTotalElements()); // wasteLog1 and wasteLog3
        assertTrue(page.getContent().contains(wasteLog1));
        assertTrue(page.getContent().contains(wasteLog3));
    }

    @Test
    void findByZoneIdAndVehicleId_Pageable_ReturnsCorrectPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<WasteLog> page = wasteLogRepository.findByZoneIdAndVehicleId(10L, 100L, pageable);
        assertEquals(2, page.getTotalElements());
        assertTrue(page.getContent().contains(wasteLog1));
        assertTrue(page.getContent().contains(wasteLog3));
    }

    @Test
    void findByCollectionTimeBetweenAndZoneId_Pageable_ReturnsCorrectPage() {
        LocalDateTime startDate = LocalDate.of(2023, 5, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(2023, 5, 13).atTime(23, 59, 59);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("collectionTime"));
        Page<WasteLog> page = wasteLogRepository.findByCollectionTimeBetweenAndZoneId(startDate, endDate, 10L, pageable);
        assertEquals(2, page.getTotalElements()); // wasteLog1 and wasteLog3
        assertEquals(wasteLog1.getLogId(), page.getContent().get(0).getLogId());
        assertEquals(wasteLog3.getLogId(), page.getContent().get(1).getLogId());
    }

    @Test
    void findByVehicleIdAndWorkerId_Pageable_ReturnsCorrectPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<WasteLog> page = wasteLogRepository.findByVehicleIdAndWorkerId(200L, 2L, pageable);
        assertEquals(1, page.getTotalElements());
        assertEquals(wasteLog2.getLogId(), page.getContent().get(0).getLogId());
    }

    @Test
    void findByWorkerIdAndCollectionTimeBetweenAndZoneIdAndVehicleId_Pageable_ReturnsCorrectPage() {
        LocalDateTime startDate = LocalDate.of(2023, 5, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(2023, 5, 11).atTime(23, 59, 59);
        Pageable pageable = PageRequest.of(0, 10);
        Page<WasteLog> page = wasteLogRepository.findByWorkerIdAndCollectionTimeBetweenAndZoneIdAndVehicleId(
                1L, startDate, endDate, 10L, 100L, pageable);
        assertEquals(1, page.getTotalElements());
        assertEquals(wasteLog1.getLogId(), page.getContent().get(0).getLogId());
    }

    @Test
    void findByWorkerIdAndCollectionTimeBetweenAndZoneId_Pageable_ReturnsCorrectPage() {
        LocalDateTime startDate = LocalDate.of(2023, 5, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(2023, 5, 13).atTime(23, 59, 59);
        Pageable pageable = PageRequest.of(0, 10);
        Page<WasteLog> page = wasteLogRepository.findByWorkerIdAndCollectionTimeBetweenAndZoneId(
                1L, startDate, endDate, 10L, pageable);
        assertEquals(2, page.getTotalElements()); // wasteLog1 and wasteLog3
    }

    @Test
    void findByWorkerIdAndCollectionTimeBetween_Pageable_ReturnsCorrectPage() {
        LocalDateTime startDate = LocalDate.of(2023, 5, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(2023, 5, 31).atTime(23, 59, 59);
        Pageable pageable = PageRequest.of(0, 10);
        Page<WasteLog> page = wasteLogRepository.findByWorkerIdAndCollectionTimeBetween(
                1L, startDate, endDate, pageable);
        assertEquals(2, page.getTotalElements()); // wasteLog1 and wasteLog3
    }

    @Test
    void findByWorkerIdAndZoneId_Pageable_ReturnsCorrectPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<WasteLog> page = wasteLogRepository.findByWorkerIdAndZoneId(1L, 10L, pageable);
        assertEquals(2, page.getTotalElements()); // wasteLog1 and wasteLog3
    }

    @Test
    void findByWorkerIdAndZoneIdAndVehicleId_Pageable_ReturnsCorrectPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<WasteLog> page = wasteLogRepository.findByWorkerIdAndZoneIdAndVehicleId(1L, 10L, 100L, pageable);
        assertEquals(2, page.getTotalElements()); // wasteLog1 and wasteLog3
    }

    @Test
    void findByWorkerIdAndVehicleId_Pageable_ReturnsCorrectPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<WasteLog> page = wasteLogRepository.findByWorkerIdAndVehicleId(1L, 100L, pageable);
        assertEquals(2, page.getTotalElements()); // wasteLog1 and wasteLog3
    }

    @Test
    void findByWorkerIdAndCollectionTimeBetweenAndVehicleId_Pageable_ReturnsCorrectPage() {
        LocalDateTime startDate = LocalDate.of(2023, 5, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(2023, 5, 13).atTime(23, 59, 59);
        Pageable pageable = PageRequest.of(0, 10);
        Page<WasteLog> page = wasteLogRepository.findByWorkerIdAndCollectionTimeBetweenAndVehicleId(
                1L, startDate, endDate, 100L, pageable);
        assertEquals(2, page.getTotalElements()); // wasteLog1 and wasteLog3
    }


    // --- List Methods Tests ---

    @Test
    void findByCollectionTimeBetween_List_ReturnsCorrectList() {
        LocalDateTime startDate = LocalDate.of(2023, 5, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(2023, 5, 31).atTime(23, 59, 59);
        List<WasteLog> list = wasteLogRepository.findByCollectionTimeBetween(startDate, endDate);
        assertEquals(3, list.size());
    }

    @Test
    void findByZoneId_List_ReturnsCorrectList() {
        List<WasteLog> list = wasteLogRepository.findByZoneId(10L);
        assertEquals(2, list.size());
        assertTrue(list.contains(wasteLog1));
        assertTrue(list.contains(wasteLog3));
    }

    @Test
    void findByVehicleId_List_ReturnsCorrectList() {
        List<WasteLog> list = wasteLogRepository.findByVehicleId(200L);
        assertEquals(1, list.size());
        assertEquals(wasteLog2.getLogId(), list.get(0).getLogId());
    }

    @Test
    void findByWorkerId_List_ReturnsCorrectList() {
        List<WasteLog> list = wasteLogRepository.findByWorkerId(1L);
        assertEquals(2, list.size());
        assertTrue(list.contains(wasteLog1));
        assertTrue(list.contains(wasteLog3));
    }

    @Test
    void findByZoneIdAndVehicleId_List_ReturnsCorrectList() {
        List<WasteLog> list = wasteLogRepository.findByZoneIdAndVehicleId(10L, 100L);
        assertEquals(2, list.size());
        assertTrue(list.contains(wasteLog1));
        assertTrue(list.contains(wasteLog3));
    }

    @Test
    void findByCollectionTimeBetweenAndZoneId_List_ReturnsCorrectList() {
        LocalDateTime startDate = LocalDate.of(2023, 5, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(2023, 5, 13).atTime(23, 59, 59);
        List<WasteLog> list = wasteLogRepository.findByCollectionTimeBetweenAndZoneId(startDate, endDate, 10L);
        assertEquals(2, list.size());
        assertTrue(list.contains(wasteLog1));
        assertTrue(list.contains(wasteLog3));
    }

    @Test
    void findByVehicleIdAndWorkerId_List_ReturnsCorrectList() {
        List<WasteLog> list = wasteLogRepository.findByVehicleIdAndWorkerId(200L, 2L);
        assertEquals(1, list.size());
        assertEquals(wasteLog2.getLogId(), list.get(0).getLogId());
    }

    @Test
    void findByWorkerIdAndCollectionTimeBetweenAndZoneIdAndVehicleId_List_ReturnsCorrectList() {
        LocalDateTime startDate = LocalDate.of(2023, 5, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(2023, 5, 11).atTime(23, 59, 59);
        List<WasteLog> list = wasteLogRepository.findByWorkerIdAndCollectionTimeBetweenAndZoneIdAndVehicleId(
                1L, startDate, endDate, 10L, 100L);
        assertEquals(1, list.size());
        assertEquals(wasteLog1.getLogId(), list.get(0).getLogId());
    }

    @Test
    void findByWorkerIdAndCollectionTimeBetweenAndZoneId_List_ReturnsCorrectList() {
        LocalDateTime startDate = LocalDate.of(2023, 5, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(2023, 5, 13).atTime(23, 59, 59);
        List<WasteLog> list = wasteLogRepository.findByWorkerIdAndCollectionTimeBetweenAndZoneId(
                1L, startDate, endDate, 10L);
        assertEquals(2, list.size());
        assertTrue(list.contains(wasteLog1));
        assertTrue(list.contains(wasteLog3));
    }

    @Test
    void findByWorkerIdAndCollectionTimeBetween_List_ReturnsCorrectList() {
        LocalDateTime startDate = LocalDate.of(2023, 5, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(2023, 5, 31).atTime(23, 59, 59);
        List<WasteLog> list = wasteLogRepository.findByWorkerIdAndCollectionTimeBetween(1L, startDate, endDate);
        assertEquals(2, list.size());
        assertTrue(list.contains(wasteLog1));
        assertTrue(list.contains(wasteLog3));
    }

    @Test
    void findByWorkerIdAndZoneId_List_ReturnsCorrectList() {
        List<WasteLog> list = wasteLogRepository.findByWorkerIdAndZoneId(1L, 10L);
        assertEquals(2, list.size());
        assertTrue(list.contains(wasteLog1));
        assertTrue(list.contains(wasteLog3));
    }

    @Test 
    void findByWorkerIdAndZoneIdAndVehicleId_List_ReturnsCorrectList() {
        List<WasteLog> list = wasteLogRepository.findByWorkerIdAndZoneIdAndVehicleId(1L, 10L, 100L);
        assertEquals(2, list.size());
        assertTrue(list.contains(wasteLog1));
        assertTrue(list.contains(wasteLog3));
    }

    @Test
    void findByWorkerIdAndVehicleId_List_ReturnsCorrectList() {
        List<WasteLog> list = wasteLogRepository.findByWorkerIdAndVehicleId(1L, 100L);
        assertEquals(2, list.size());
        assertTrue(list.contains(wasteLog1));
        assertTrue(list.contains(wasteLog3));
    }

    @Test
    void findByWorkerIdAndCollectionTimeBetweenAndVehicleId_List_ReturnsCorrectList() {
        LocalDateTime startDate = LocalDate.of(2023, 5, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(2023, 5, 13).atTime(23, 59, 59);
        List<WasteLog> list = wasteLogRepository.findByWorkerIdAndCollectionTimeBetweenAndVehicleId(
                1L, startDate, endDate, 100L);
        assertEquals(2, list.size());
        assertTrue(list.contains(wasteLog1));
        assertTrue(list.contains(wasteLog3));
    }
}
