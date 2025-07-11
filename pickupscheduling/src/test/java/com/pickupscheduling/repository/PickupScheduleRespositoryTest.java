package com.pickupscheduling.repository;

import com.pickupscheduling.entity.PickupSchedule;
import com.pickupscheduling.enums.Frequency;
import com.pickupscheduling.enums.Status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PickupScheduleRepositoryTest {

    @Autowired
    private PickupScheduleRepository repository;

    private PickupSchedule schedule1;
    private PickupSchedule schedule2;

    @BeforeEach
    void init() {
        schedule1 = new PickupSchedule();
        schedule1.setZoneId(101L);
        schedule1.setZoneName("Zone A");
        schedule1.setFrequency(Frequency.DAILY);
        schedule1.setTimeSlot("9-11AM");
        schedule1.setStatus(Status.SCHEDULED);
        schedule1.setAllRoutesAssigned(false);
        schedule1.setAllWorkersAreAssigned(true);

        schedule2 = new PickupSchedule();
        schedule2.setZoneId(102L);
        schedule2.setZoneName("Zone B");
        schedule2.setFrequency(Frequency.WEEKLY_MONDAY);
        schedule2.setTimeSlot("2-4PM");
        schedule2.setStatus(Status.SCHEDULED);
        schedule2.setAllRoutesAssigned(true);
        schedule2.setAllWorkersAreAssigned(false);

        repository.saveAll(List.of(schedule1, schedule2));
    }

    @Test
    void testFindAllByZoneId() {
        List<PickupSchedule> results = repository.findAllByZoneId(101L);
        assertEquals(1, results.size());
        assertEquals("Zone A", results.get(0).getZoneName());
    }

    @Test
    void testFindByZoneId() {
        Optional<PickupSchedule> optional = repository.findByZoneId(102L);
        assertTrue(optional.isPresent());
        assertEquals("Zone B", optional.get().getZoneName());
    }

    @Test
    void testFindAllByAllRoutesAssigned() {
        List<PickupSchedule> routesAssigned = repository.findAllByAllRoutesAssigned(true);
        assertEquals(1, routesAssigned.size());
        assertEquals(102L, routesAssigned.get(0).getZoneId());
    }

    @Test
    void testFindAllByAllWorkersAreAssigned() {
        List<PickupSchedule> workersAssigned = repository.findAllByAllWorkersAreAssigned(true);
        assertEquals(1, workersAssigned.size());
        assertEquals(101L, workersAssigned.get(0).getZoneId());
    }

    @Test
    void testFindByTimeSlot() {
        List<PickupSchedule> slots = repository.findByTimeSlot("9-11AM");
        assertEquals(1, slots.size());
        assertEquals("Zone A", slots.get(0).getZoneName());
    }

    @Test
    void testDeleteByZoneId() {
        repository.deleteByZoneId(101L);
        Optional<PickupSchedule> optional = repository.findByZoneId(101L);
        assertTrue(optional.isEmpty());
    }
}
