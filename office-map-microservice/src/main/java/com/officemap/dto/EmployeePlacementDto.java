package com.officemap.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

public class EmployeePlacementDto extends RepresentationModel<EmployeePlacementDto> {

    private Long id;
    private Date allocationDate;
    private Long employeeId;
    private Long deskId;

    public EmployeePlacementDto(Long id, Date allocationDate, Long employeeId, Long deskId) {
        this.id = id;
        this.allocationDate = allocationDate;
        this.employeeId = employeeId;
        this.deskId = deskId;
    }

    public EmployeePlacementDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getAllocationDate() {
        return allocationDate;
    }

    public void setAllocationDate(Date allocationDate) {
        this.allocationDate = allocationDate;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getDeskId() {
        return deskId;
    }

    public void setDeskId(Long deskId) {
        this.deskId = deskId;
    }
}
