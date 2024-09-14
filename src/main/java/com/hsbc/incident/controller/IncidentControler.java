package com.hsbc.incident.controller;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;

import com.hsbc.incident.entity.Incident;
import com.hsbc.incident.service.IncidentService;

@CrossOrigin
@RestController
@RequestMapping("/incident")
public class IncidentControler {
	
	@Autowired
	private IncidentService incidentService;
	

    @GetMapping("page")
    public ResponseEntity<Page<Incident>> getPaginatedIncidents(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(incidentService.getIncidentsPaginated(page, size));
    }
	
	 @GetMapping("list")
	 public ResponseEntity<Collection<Incident>> getAllIncidents() {
		 Incident incident = new Incident();
	     List<Incident> ls = new ArrayList();
	     ls.add(incident);
	     //return ResponseEntity.ok(ls);
		 return ResponseEntity.ok(incidentService.getAllIncidents());
	 }
	 
	 @PostMapping("create")
	 public ResponseEntity<Incident> createIncident(@RequestBody Incident incident) {
	     return  ResponseEntity.ok(incidentService.createIncident(incident));
	 }
	 
	 @PutMapping("update/{id}")
	 public ResponseEntity<Incident> updateIncident(@PathVariable Long id, @RequestBody Incident incident) {
	     return ResponseEntity.ok(incidentService.updateIncident(id,incident));
	 }

	 @DeleteMapping("delete/{id}")
	 public ResponseEntity<Void> deleteIncident(@PathVariable Long id) {
	     incidentService.deleteIncident(id);
	     return ResponseEntity.noContent().build();
	 }

}
