package com.officemap.units;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "placements")
public class EmployeePlacement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @CreationTimestamp
    private Date allocationDate;
    private Long employeeId;
    private Long deskId;

    public EmployeePlacement() {
    }

    public EmployeePlacement(Long id, Date allocationDate, Long employeeId, Long deskId) {
        this.id = id;
        this.allocationDate = allocationDate;
        this.employeeId = employeeId;
        this.deskId = deskId;
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
