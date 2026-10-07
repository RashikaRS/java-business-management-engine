package com.bizengine.exception;

/** UNCHECKED: thrown when an object is constructed/mutated with data that breaks an invariant. */
public class InvalidBusinessDataException extends RuntimeException {
    public InvalidBusinessDataException(String message) {
        super(message);
    }
}
