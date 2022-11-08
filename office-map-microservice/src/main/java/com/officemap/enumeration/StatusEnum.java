package com.officemap.enumeration;

public enum StatusEnum {
    AVAILABLE("Available"),
    OCCUPIED("Employee id: "); // ToAdd employee ID

    private String status;

    StatusEnum(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
}
