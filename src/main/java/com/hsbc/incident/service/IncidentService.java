package com.hsbc.incident.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import com.hsbc.incident.entity.Incident;
import com.hsbc.incident.exception.InvalidDataException;
import com.hsbc.incident.exception.NotFoundException;
import com.hsbc.incident.repository.IncidentRepository;

@Service
public class IncidentService {

	@Autowired
    private final IncidentRepository repository;

    public IncidentService(IncidentRepository repository) {
        this.repository = repository;
    }

    public List<Incident> getAllIncidents() {
        return repository.findAll();
    }
    
 
    public Page<Incident> getIncidentsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

    public Incident getIncidentById(Long id) {
    	Incident data = repository.findById(id)
        		.orElseThrow(() -> new NotFoundException(id));
    	return data;
    }

    public Incident createIncident(Incident incident) {
    	if (incident.getTitle() == null || incident.getTitle().isEmpty()) {
            throw new InvalidDataException("Incident title must not be empty.");
        }
    	incident.setCreate_time(new Date());
    	incident.setUpdate_time(new Date());
        return repository.save(incident);
    }

    public Incident updateIncident(Long id, Incident updatedIncident) {
    	if (updatedIncident.getTitle() == null || updatedIncident.getTitle().isEmpty()) {
            throw new InvalidDataException("Incident title must not be empty.");
        }
    	return repository.findById(id)
                .map(incident -> {
                    incident.setTitle(updatedIncident.getTitle());
                    incident.setDescription(updatedIncident.getDescription());
                    incident.setUpdate_time(new Date());
                    return repository.save(incident);
                })
                .orElseThrow(() -> new NotFoundException(id));
    }

    public void deleteIncident(Long id) {
    	 if (!repository.existsById(id)) {
             throw new NotFoundException(id);
         }
        repository.deleteById(id);
    }
}

