package com.officemap.mapper;

import com.officemap.dto.ComponentDto;
import com.officemap.dto.EmployeePlacementDto;
import com.officemap.units.Component;
import com.officemap.units.EmployeePlacement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

import static com.officemap.enumeration.ComponentsEnum.COUNTRY;
import static org.junit.jupiter.api.Assertions.*;

class EmployeePlacementMapperTest {

    @Autowired
    private EmployeePlacementMapper placementMapper = new EmployeePlacementMapper();

    @Test
    void employeePlacementEntityToDto() {
        EmployeePlacement employeePlacement = new EmployeePlacement(1, 5);
        EmployeePlacementDto employeePlacementDto = placementMapper.employeePlacementEntityToDto(employeePlacement);
        assertAll(
                () -> assertTrue(employeePlacementDto.getEmployeeId().equals(employeePlacement.getEmployeeId())),
                () -> assertTrue(employeePlacementDto.getDeskId().equals(employeePlacement.getDeskId()))
        );
    }
}