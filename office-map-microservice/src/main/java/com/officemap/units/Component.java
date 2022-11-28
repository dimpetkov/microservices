package com.officemap.units;

import com.officemap.enumeration.ComponentsEnum;

import javax.persistence.*;

@Entity
@Table(name = "components")
public class Component {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
//    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComponentsEnum componentsEnum;
    private String type;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Long parentId;
    private String details;

    public Component() {
    }

    public Component(Long id, ComponentsEnum componentsEnum, String name, Long parentId, String details) {
        this.id = id;
        this.componentsEnum = componentsEnum;
        this.name = name;
        this.parentId = parentId;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ComponentsEnum getComponentEnum() {
        return componentsEnum;
    }

    public void setComponentEnum(ComponentsEnum componentsEnum) {
        this.componentsEnum = componentsEnum;
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

    public void setType(String type) {
        this.type = type;
    }
}
