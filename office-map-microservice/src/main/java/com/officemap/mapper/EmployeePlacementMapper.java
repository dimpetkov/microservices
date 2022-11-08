package com.officemap.mapper;

import com.officemap.dto.EmployeePlacementDto;
import com.officemap.units.EmployeePlacement;
import org.springframework.stereotype.Service;

@Service
public class EmployeePlacementMapper {

    public EmployeePlacementDto employeePlacementEntityToDto(EmployeePlacement employeePlacement) {
        EmployeePlacementDto employeePlacementDto = new EmployeePlacementDto();
        employeePlacementDto.setId(employeePlacement.getId());
        employeePlacementDto.setAllocationDate(employeePlacement.getAllocationDate());
        employeePlacementDto.setEmployeeId(employeePlacement.getEmployeeId());
        employeePlacementDto.setDeskId(employeePlacement.getDeskId());
        return employeePlacementDto;
    }
}
