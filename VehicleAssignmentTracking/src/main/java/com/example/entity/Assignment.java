package com.example.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Assignment{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Long assignmentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "route_id", nullable = false)
    private Long routeId; 

    @Column(name = "date_assigned", nullable = false)
    private LocalDate dateAssigned;
    
    @Column(nullable = false)
    private boolean isWorkerAssigned=false;
    
    @Column(nullable=false)
    private boolean isWasteLogged=false;
    
    @Column(nullable = false)
    private String vehicleStatus;
    
    @Column(nullable = false)
    private String pathDetails;

    @Override
    public String toString() {
        return "Assignment{" +
                "assignmentId=" + assignmentId +
                ", vehicleId=" + (vehicle != null ? vehicle.getVehicleId() : null) +
                ", routeId=" + routeId +
                ", dateAssigned=" + dateAssigned +
                '}';
    }
}
