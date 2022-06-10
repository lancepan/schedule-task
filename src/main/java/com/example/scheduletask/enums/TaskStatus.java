package com.example.scheduletask.enums;

public enum TaskStatus {
    ENABLED("enabled"),DISABLED("disabled");
    private String value;
    TaskStatus(String value){
        this.value = value;
    }
}
