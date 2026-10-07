package com.bizengine.exception;

public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(Object id) {
        super("No entity found with id: " + id);
    }
}
