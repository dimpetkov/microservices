package com.officemapmicroservice.dto;

import com.googlecode.jmapper.annotations.JMap;
import com.googlecode.jmapper.annotations.JMapConversion;
import com.officemapmicroservice.repositories.EmployeeAllocationRepository;

import static com.officemapmicroservice.common.DeskStatusMessages.NOT_AVAILABLE;

public class DeskDto {
    private final EmployeeAllocationRepository employeeAllocationRepository;

    @JMap("deskId")
    private Long deskId;

    @JMap("deskNumber")
    private int deskNumber;

    @JMap("status")
    private String deskStatus;

    @JMap("status")
    private Long occupiedByEmployeeId;

    public DeskDto(EmployeeAllocationRepository employeeAllocationRepository) {
        this.employeeAllocationRepository = employeeAllocationRepository;
    }

    @JMapConversion(from={"status"}, to={"occupiedByEmployeeId"})
    public Long convesion(String status) {
        if(status.equals(NOT_AVAILABLE)) {
            return employeeAllocationRepository.findByDesk(deskId).getDesk().getDeskId();
        }
        return null;
    }

}
