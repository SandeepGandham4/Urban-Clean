package com.pickupscheduling.entity;

import com.pickupscheduling.enums.Frequency;
import com.pickupscheduling.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="pickup_schedule")
public class PickupSchedule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long scheduleId;
	
	@Column(nullable = false)
    private Long zoneId;
	
	@Column(nullable = false)
	private String zoneName;
	
	@Enumerated(EnumType.STRING)
    private Frequency frequency;
 
    @Column(nullable = false)
    private String timeSlot;
 
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @Column(nullable = false)
    private boolean allRoutesAssigned=false;
    
    @Column(nullable = false)
    private boolean allWorkersAreAssigned=false;
}
