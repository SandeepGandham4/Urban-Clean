package com.urbanclean.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.urbanclean.entity.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {

}
