package com.officemap.service;

import com.officemap.mapper.ComponentMapper;
import com.officemap.mapper.EmployeePlacementMapper;
import com.officemap.repository.ComponentRepository;
import com.officemap.repository.EmployeePlacementRepository;
import com.officemap.units.Component;
import com.officemap.units.EmployeePlacement;
import com.officemap.units.request.RequestCompose;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static com.officemap.enumeration.ComponentsEnum.COUNTRY;
import static com.officemap.enumeration.ComponentsEnum.DESK;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeePlacementsService.class)
class EmployeePlacementsServiceTest {

    @MockBean private EmployeePlacementRepository placementRepository;
    @MockBean private EmployeePlacementMapper placementMapper;
    @MockBean private ComponentRepository componentRepository;
    @MockBean private ComponentMapper componentMapper;
    @MockBean private EmployeePlacementsService placementsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        placementsService = new EmployeePlacementsService(placementRepository, placementMapper, componentRepository, componentMapper);
    }

    @Test
    void testGetEmployeePlacementById() {
        placementsService.getEmployeePlacementById(1L);
        verify(placementRepository).findById(1L);
    }

    @Test
    void getPlacementsDtoList() {
        placementsService.getPlacementsDtoList();
        verify(placementRepository).findAll();
    }

    @Test
    void getPlacementByDeskId() {
        Component component = new Component(5L, DESK, "C05", 3L, "Available");
        EmployeePlacement employeePlacement = new EmployeePlacement(150, 5);
        when(componentRepository.findById(5L)).thenReturn(java.util.Optional.of(component));
        placementsService.getPlacementByDeskId(5L);
        verify(componentRepository, times(4)).findById(5L);
    }

    @Test
    void getPlacementByEmployeeId() {
        placementsService.getPlacementByEmployeeId(10L);
        verify(placementRepository, times(2)).findByEmployeeId(10L);
    }

    @Test
    void createPlacement() {
    }

    @Test
    void updatePlacement() {
        EmployeePlacement employeePlacement = new EmployeePlacement(150, 5);
        RequestCompose requestCompose = new RequestCompose(10L);
        placementsService.updatePlacement(1L, requestCompose);
        verify(placementRepository).findById(1L);
    }

    @Test
    void deletePlacement() {
        placementsService.deletePlacement(1L);
        verify(placementRepository).findById(1L);
    }
}