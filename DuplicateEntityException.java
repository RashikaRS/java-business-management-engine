package com.bizengine.exception;

public class DuplicateEntityException extends BusinessException {
    public DuplicateEntityException(Object id) {
        super("Entity already exists with id: " + id);
    }
}
