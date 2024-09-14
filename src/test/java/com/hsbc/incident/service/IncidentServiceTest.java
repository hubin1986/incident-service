package com.hsbc.incident.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.hsbc.incident.entity.Incident;
import com.hsbc.incident.repository.IncidentRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class IncidentServiceTest {

    @Mock
    private IncidentRepository repository;

    @InjectMocks
    private IncidentService incidentService;

    private Incident incident;

    @BeforeEach
    void setUp() {
        incident = new Incident();
        incident.setId(1L);
        incident.setTitle("Test Incident");
        incident.setDescription("Test Description");
        incident.setCreate_time(new Date());
        incident.setUpdate_time(new Date());
    }

    @Test
    void testGetAllIncidents() {
        when(repository.findAll()).thenReturn(Arrays.asList(incident));
        
        List<Incident> incidents = incidentService.getAllIncidents();
        
        assertNotNull(incidents);
        assertEquals(1, incidents.size());
        assertEquals("Test Incident", incidents.get(0).getTitle());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetIncidentsPaginated() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Incident> incidentPage = new PageImpl<>(Arrays.asList(incident), pageable, 1);
        
        when(repository.findAll(pageable)).thenReturn(incidentPage);
        
        Page<Incident> result = incidentService.getIncidentsPaginated(0, 10);
        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    void testGetIncidentById() {
        when(repository.findById(1L)).thenReturn(Optional.of(incident));
        
        Incident foundIncident = incidentService.getIncidentById(1L);
        
        assertNotNull(foundIncident);
        assertEquals(1L, foundIncident.getId());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testCreateIncident() {
        when(repository.save(any(Incident.class))).thenReturn(incident);
        
        Incident newIncident = new Incident();
        newIncident.setTitle("New Incident");
        
        Incident createdIncident = incidentService.createIncident(newIncident);
        
        assertNotNull(createdIncident);
        assertEquals("Test Incident", createdIncident.getTitle());  // 因为mock返回incident
        verify(repository, times(1)).save(any(Incident.class));
    }

    @Test
    void testUpdateIncident() {
        when(repository.findById(1L)).thenReturn(Optional.of(incident));
        when(repository.save(any(Incident.class))).thenReturn(incident);
        
        Incident updatedIncident = new Incident();
        updatedIncident.setTitle("Updated Title");
        
        Incident result = incidentService.updateIncident(1L, updatedIncident);
        
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Incident.class));
    }

    @Test
    void testDeleteIncident() {
        when(repository.existsById(1L)).thenReturn(true);
        
        incidentService.deleteIncident(1L);
        
        verify(repository, times(1)).deleteById(1L);
    }
}
