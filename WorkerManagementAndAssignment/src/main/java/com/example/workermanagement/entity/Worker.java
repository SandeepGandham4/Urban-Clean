package com.example.workermanagement.entity;



import com.example.workermanagement.enums.WorkerRole;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "workers")
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String contactInfo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WorkerRole role;
    @OneToOne(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private Assignment assignment;
}
