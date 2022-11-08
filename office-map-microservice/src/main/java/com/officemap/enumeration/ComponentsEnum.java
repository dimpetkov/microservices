package com.officemap.enumeration;

public enum ComponentsEnum {
    COUNTRY("Country"),
    CITY("City"),
    BUILDING("Building"),
    FLOOR("Floor"),
    ROOM("Room"),
    DESK("Desk");

    private String componentType;
    ComponentsEnum(String componentType) {
        this.componentType = componentType;
    }
    public String getComponentType(){
        return componentType;
    }
}
