package com.officemap.dto;

public class ComponentDto {
    private Long id;
    private String componentType;
    private String name;
    private Long parentId;
    private String details;


    public ComponentDto(Long id, String componentType, String name, Long parentId, String details) {
        this.id = id;
        this.componentType = componentType;
        this.name = name;
        this.parentId = parentId;
        this.details = details;
    }

    public ComponentDto() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
