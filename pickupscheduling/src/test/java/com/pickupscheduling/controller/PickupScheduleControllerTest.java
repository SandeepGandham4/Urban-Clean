package com.pickupscheduling.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickupscheduling.dto.PickupScheduleRequestDTO;
import com.pickupscheduling.dto.PickupScheduleResponseDTO;
import com.pickupscheduling.dto.ZoneResponseDTO;
import com.pickupscheduling.enums.Frequency;
import com.pickupscheduling.enums.Status;
import com.pickupscheduling.entity.PickupSchedule;
import com.pickupscheduling.service.PickupScheduleService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PickupScheduleController.class)
class PickupScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PickupScheduleService service;

    @Autowired
    private ObjectMapper mapper;

    private PickupScheduleRequestDTO requestDTO;
    private PickupScheduleResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        requestDTO = new PickupScheduleRequestDTO(101L, "Zone A", Frequency.DAILY, "9-11AM", Status.SCHEDULED);
        responseDTO = new PickupScheduleResponseDTO(1L, 101L, "Zone A", Frequency.DAILY, "9-11AM", Status.SCHEDULED);
    }

    @Test
    void createSchedule_shouldReturnCreatedSchedule() throws Exception {
        when(service.createSchedule(any())).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/urbanclean/supervisor/schedules/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.zoneId").value(101L));
    }

    @Test
    void getAllSchedules_shouldReturnList() throws Exception {
        when(service.getAllSchedules()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/v1/urbanclean/supervisor/schedules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].zoneName").value("Zone A"));
    }

    @Test
    void getNewlyAddedZones_shouldReturnZones() throws Exception {
        ZoneResponseDTO zone = new ZoneResponseDTO();
        zone.setId(101L);
        zone.setName("Zone X");
        when(service.newlyAddedZones()).thenReturn(List.of(zone));

        mockMvc.perform(get("/api/v1/urbanclean/supervisor/schedules/notify"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].zoneName").value("Zone X"));
    }

    @Test
    void updateSchedule_shouldReturnUpdatedSchedule() throws Exception {
        PickupSchedule updated = new PickupSchedule();
        updated.setScheduleId(1L);
        updated.setZoneId(101L);
        updated.setZoneName("Updated Zone");
        updated.setFrequency(Frequency.WEEKLY_MONDAY);
        updated.setTimeSlot("10-12PM");
        updated.setStatus(Status.SCHEDULED);

        when(service.updateSchedule(eq(1L), any())).thenReturn(updated);

        mockMvc.perform(put("/api/v1/urbanclean/supervisor/schedules/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.zoneName").value("Updated Zone"));
    }

    @Test
    void deleteSchedule_shouldReturnNoContent() throws Exception {
        doNothing().when(service).deleteSchedule(1L);

        mockMvc.perform(delete("/api/v1/urbanclean/supervisor/schedules/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void numberOfSchedules_shouldReturnCount() throws Exception {
        when(service.numberOfSchedules()).thenReturn(4L);

        mockMvc.perform(get("/api/v1/urbanclean/supervisor/schedules/schedulescount"))
                .andExpect(status().isOk())
                .andExpect(content().string("4"));
    }

    @Test
    void updateScheduleStatus_shouldBeOk() throws Exception {
        doNothing().when(service).updateScheduleStatus(101L);

        mockMvc.perform(put("/api/v1/urbanclean/supervisor/schedules/schedulestatus")
                .contentType(MediaType.APPLICATION_JSON)
                .content("101"))
                .andExpect(status().isOk());
    }

    // Additional tests for remaining endpoints can be added as needed
}
