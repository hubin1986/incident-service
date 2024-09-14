package com.hsbc.incident.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hsbc.incident.entity.Incident;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
	
}