package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Zone;

public interface ZoneRepository extends JpaRepository<Zone, Long> {

	Zone findByName(String name);

}
