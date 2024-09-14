package com.hsbc.incident.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsbc.incident.entity.Incident;
import com.hsbc.incident.service.IncidentService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class IncidentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IncidentService incidentService;

    @InjectMocks
    private IncidentControler incidentController;

    private Incident incident;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(incidentController).build();
        incident = new Incident();
        incident.setId(1L);
        incident.setTitle("Test Incident");
        incident.setDescription("Test Description");
    }

    @Test
    void testGetAllIncidents() throws Exception {
        when(incidentService.getAllIncidents()).thenReturn(Arrays.asList(incident));

        mockMvc.perform(get("/incident/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Incident"))
                .andExpect(jsonPath("$[0].description").value("Test Description"));

        verify(incidentService, times(1)).getAllIncidents();
    }

    @Test
    void testGetPaginatedIncidents() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Incident> incidentPage = new PageImpl<>(Arrays.asList(incident), pageable, 1);

        when(incidentService.getIncidentsPaginated(0, 10)).thenReturn(incidentPage);

        mockMvc.perform(get("/incident/page")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Test Incident"))
                .andExpect(jsonPath("$.content[0].description").value("Test Description"));

        verify(incidentService, times(1)).getIncidentsPaginated(0, 10);
    }

    @Test
    void testCreateIncident() throws Exception {
        when(incidentService.createIncident(any(Incident.class))).thenReturn(incident);

        mockMvc.perform(post("/incident/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(incident)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Incident"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(incidentService, times(1)).createIncident(any(Incident.class));
    }

    @Test
    void testUpdateIncident() throws Exception {
        when(incidentService.updateIncident(eq(1L), any(Incident.class))).thenReturn(incident);

        mockMvc.perform(put("/incident/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(incident)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Incident"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(incidentService, times(1)).updateIncident(eq(1L), any(Incident.class));
    }

    @Test
    void testDeleteIncident() throws Exception {
        doNothing().when(incidentService).deleteIncident(1L);

        mockMvc.perform(delete("/incident/delete/1"))
                .andExpect(status().isNoContent());

        verify(incidentService, times(1)).deleteIncident(1L);
    }
}
