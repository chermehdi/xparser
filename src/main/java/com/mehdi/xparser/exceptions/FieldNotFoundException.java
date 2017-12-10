package com.mehdi.xparser.exceptions;

public class FieldNotFoundException extends RuntimeException {

    public FieldNotFoundException(String message) {
        super(message);
    }

    public FieldNotFoundException() {
        super("Could not instantiate the field");
    }
}
