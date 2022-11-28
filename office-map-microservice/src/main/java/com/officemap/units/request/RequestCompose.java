package com.officemap.units.request;

import com.officemap.enumeration.ComponentsEnum;

public class RequestCompose {

    ComponentsEnum componentType;
    String name;
    String details;
    Long id;
    Long parentId;
    Long employeeId;
    Long deskId;

    public RequestCompose(ComponentsEnum componentType,
                          String name, String details, Long id, Long parentId,
                          Long employeeId, Long deskId) {
        this.componentType = componentType;
        this.name = name;
        this.details = details;
        this.id = id;
        this.parentId = parentId;
        this.employeeId = employeeId;
        this.deskId = deskId;
    }

    public RequestCompose() {

    }

    public RequestCompose(ComponentsEnum componentType, String name, long parentId, String details) {
        this.componentType = componentType;
        this.name = name;
        this.details = details;
        this.parentId = parentId;
    }

    public RequestCompose(Long deskId) {
    }

    public ComponentsEnum getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentsEnum componentType) {
        this.componentType = componentType;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public Long getParentId() {
        return parentId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public Long getDeskId() {
        return deskId;
    }
}

