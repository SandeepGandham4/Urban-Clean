package com.pickupscheduling.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pickupscheduling.dto.PickupScheduleRequestDTO;
import com.pickupscheduling.dto.PickupScheduleResponseDTO;
import com.pickupscheduling.entity.PickupSchedule;
import com.pickupscheduling.enums.Frequency;
import com.pickupscheduling.enums.Status;
import com.pickupscheduling.exception.ResourceNotFoundException;
import com.pickupscheduling.exception.ZoneNotFoundException;
import com.pickupscheduling.feign.VehicleServiceFeignClient;
import com.pickupscheduling.feign.ZoneServiceFeignClient;
import com.pickupscheduling.repository.PickupScheduleRepository;
import com.pickupscheduling.serviceimpl.PickupScheduleServiceImpl;

@ExtendWith(MockitoExtension.class)
class PickupScheduleServiceImplTest {

    @Mock
    private PickupScheduleRepository repository;

    @Mock
    private ZoneServiceFeignClient zoneClient;

    @Mock
    private VehicleServiceFeignClient vehicleClient;

    @InjectMocks
    private PickupScheduleServiceImpl service;

    private PickupSchedule mockSchedule;

    @BeforeEach
    void init() {
        mockSchedule = new PickupSchedule();
        mockSchedule.setScheduleId(1L);
        mockSchedule.setZoneId(101L);
        mockSchedule.setZoneName("TestZone");
        mockSchedule.setFrequency(Frequency.WEEKLY_MONDAY);
        mockSchedule.setTimeSlot("9-11AM");
        mockSchedule.setStatus(Status.SCHEDULED);
    }

    @Test
    void testCreateSchedule_Success() {
        PickupScheduleRequestDTO request = new PickupScheduleRequestDTO(
            101L, "TestZone", Frequency.WEEKLY_MONDAY, "9-11AM", Status.SCHEDULED
        );
        when(repository.findByZoneId(101L)).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(mockSchedule);

        PickupScheduleResponseDTO response = service.createSchedule(request);

        assertEquals(101L, response.getZoneId());
        assertEquals("TestZone", response.getZoneName());
        assertEquals(Frequency.WEEKLY_MONDAY, response.getFrequency());
        assertEquals(Status.SCHEDULED, response.getStatus());
    }

    @Test
    void testCreateSchedule_AlreadyExists_ThrowsException() {
        when(repository.findByZoneId(101L)).thenReturn(Optional.of(mockSchedule));

        PickupScheduleRequestDTO request = new PickupScheduleRequestDTO(
            101L, "TestZone", Frequency.WEEKLY_MONDAY, "9-11AM", Status.SCHEDULED
        );

        assertThrows(ZoneNotFoundException.class, () -> service.createSchedule(request));
    }

    @Test
    void testGetAllSchedules_ReturnsList() {
        when(repository.findAll()).thenReturn(List.of(mockSchedule));

        List<PickupScheduleResponseDTO> schedules = service.getAllSchedules();

        assertFalse(schedules.isEmpty());
        assertEquals("TestZone", schedules.get(0).getZoneName());
        assertEquals(Frequency.WEEKLY_MONDAY, schedules.get(0).getFrequency());
    }

    @Test
    void testGetAllSchedules_Empty_ThrowsException() {
        when(repository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class, () -> service.getAllSchedules());
    }

    @Test
    void testUpdateSchedule_Success() {
        PickupScheduleRequestDTO request = new PickupScheduleRequestDTO(
            101L, "UpdatedZone", Frequency.DAILY, "10-12PM", Status.SCHEDULED
        );
        when(repository.findById(1L)).thenReturn(Optional.of(mockSchedule));
        when(repository.save(any())).thenReturn(mockSchedule);

        PickupSchedule updated = service.updateSchedule(1L, request);

        assertEquals("UpdatedZone", updated.getZoneName());
        assertEquals(Frequency.DAILY, updated.getFrequency());
        assertEquals("10-12PM", updated.getTimeSlot());
    }

    @Test
    void testDeleteSchedule_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.deleteSchedule(1L));
    }

    @Test
    void testNumberOfSchedules() {
        when(repository.findAll()).thenReturn(List.of(mockSchedule, mockSchedule));
        assertEquals(2L, service.numberOfSchedules());
    }

    @Test
    void testUpdateScheduleStatus() {
        when(repository.findByZoneId(101L)).thenReturn(Optional.of(mockSchedule));
        service.updateScheduleStatus(101L);
        assertEquals(Status.COMPLETED, mockSchedule.getStatus());
    }
}
