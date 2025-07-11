package com.pickupscheduling.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pickupscheduling.entity.PickupSchedule;

@Repository
public interface PickupScheduleRepository extends JpaRepository<PickupSchedule,Long> {

    List<PickupSchedule> findAllByZoneId(Long zoneId);

    Optional<PickupSchedule> findByZoneId(Long zoneId);
	
    @Modifying
    @Transactional
	void deleteByZoneId(Long zoneId);
	
	List<PickupSchedule> findAllByAllRoutesAssigned(boolean value);
	
	List<PickupSchedule> findAllByAllWorkersAreAssigned(boolean value);


	List<PickupSchedule> findByTimeSlot(String timeSlot);
}