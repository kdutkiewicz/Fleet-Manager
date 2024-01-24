package com.firestms.Exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String resourceName, String id) {
        super("Resource of type:" + resourceName + " and id:" + id + " already exist in database");
    }
}
