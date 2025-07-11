package com.example.model; 
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="waste_log")

@Getter
@Setter

@NoArgsConstructor 
@AllArgsConstructor 
public class WasteLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;
    
    private Long zoneId;
    private Long vehicleId;
    private Long workerId;
    private Double weightCollected;
    
    private LocalDateTime collectionTime;
}