package com.officemap.service;

import com.officemap.dto.ComponentDto;
import com.officemap.enumeration.StatusEnum;
import com.officemap.mapper.ComponentMapper;
import com.officemap.repository.ComponentRepository;
import com.officemap.repository.EmployeePlacementRepository;
import com.officemap.units.Component;
import com.officemap.units.request.RequestCompose;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static com.officemap.enumeration.ComponentsEnum.COUNTRY;
import static com.officemap.enumeration.ComponentsEnum.DESK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@WebMvcTest(ComponentsService.class)
class ComponentsServiceTest {

    @MockBean private ComponentRepository componentRepository;
    @MockBean private ComponentMapper componentMapper;
    @MockBean private EmployeePlacementRepository employeePlacementRepository;
    @MockBean private ComponentsService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new ComponentsService(componentRepository, componentMapper, employeePlacementRepository);
    }

    @Test
    void testGetComponentById() {
        underTest.getComponentById(1L);
        verify(componentRepository).findById(1L);
    }

    @Test
    void testGetComponentsList() {
        underTest.getComponentsList();
        verify(componentRepository).findAll();
    }

    @Test
    void getComponentsByParentId() {
        underTest.getComponentsByParentId(1L);
        verify(componentRepository).findAllByParentId(1L);
    }

    @Test
    void testGetComponentsByType() {
        underTest.getComponentsByType("COUNTRY");
        verify(componentRepository).findAllByComponentsEnum(COUNTRY.ordinal());
    }

    @Test
    void testAddComponent() {
        RequestCompose requestCompose = new RequestCompose(COUNTRY, "Bulgaria", 0L, "Language: Bulgarian");
        ComponentDto componentDto = new ComponentDto(1L, COUNTRY.getComponentType(), "Bulgaria", 0L, "Language: Bulgarian");
        given(underTest.addComponent(requestCompose)).willReturn(componentDto);
        ArgumentCaptor<Component> componentArgumentCaptor = ArgumentCaptor.forClass(Component.class);
        verify(componentRepository).save(componentArgumentCaptor.capture());
        Component capturedComponent = componentArgumentCaptor.getValue();
        assertThat(capturedComponent.getComponentEnum()).isEqualByComparingTo(requestCompose.getComponentType());
        assertThat(capturedComponent.getName()).isEqualTo(requestCompose.getName());
        assertThat(capturedComponent.getParentId()).isEqualTo(requestCompose.getParentId());
        assertThat(capturedComponent.getDetails()).isEqualTo(requestCompose.getDetails());
    }

    @Test
    void testAddComponentDesk() {
        RequestCompose requestCompose = new RequestCompose(DESK, "C1", 0L, "");
        underTest.addComponent(requestCompose);
        assertThat(requestCompose.getDetails()).isEqualTo(StatusEnum.AVAILABLE.getStatus());
    }

    @Test
    void testWarningWhenParentsRelationNotPossible() {
        RequestCompose requestCompose = new RequestCompose(COUNTRY, "Bulgaria", 1L, "Language: Bulgarian");
        Object returnValue = underTest.addComponent(requestCompose);
        assertEquals(("Parent child relation is NOT POSSIBLE"), returnValue);
        verify(componentRepository, never()).save(any());
    }

    @Test
    void updateComponent() {
        RequestCompose requestCompose = new RequestCompose(COUNTRY, "Bulgaria", 0L, "Language: Bulgarian");
        underTest.updateComponent(1L, requestCompose);
        verify(componentRepository).findById(1L);
    }

    @Test
    void testDeleteComponent() {
        Component component = new Component(1L, COUNTRY, "Bulgaria", 0L, "Language: Bulgarian");
        underTest.deleteComponent(1L);
        verify(componentRepository).findById(1L);
    }
}