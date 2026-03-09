package com.inspectpro.helper;

import lombok.Data;

public @Data class ResponseEntityObject<T> {

    private Boolean status;
    private String message;
    private T object;
    private long totalElements; // New field for total count

    // Constructor for status and object
    public ResponseEntityObject(boolean status, T object) {
        this.status = status;
        this.object = object;
    }

    // Constructor for status and message
    public ResponseEntityObject(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    // Constructor for status, message, and object
    public ResponseEntityObject(boolean status, String message, T object) {
        this.status = status;
        this.message = message;
        this.object = object;
    }

    // Constructor for status, message, totalElements, and object
    public ResponseEntityObject(boolean status, String message, long totalElements, T object) {
        this.status = status;
        this.message = message;
        this.totalElements = totalElements; // Set total count
        this.object = object;
    }

}
