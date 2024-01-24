package com.firestms.Exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, String id) {
        super("Resource of type:" + resourceName + " and id:" + id + " not found");
    }
}
