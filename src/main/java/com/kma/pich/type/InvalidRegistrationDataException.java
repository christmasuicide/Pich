package com.kma.pich.type;

public class InvalidRegistrationDataException extends RuntimeException {
    public InvalidRegistrationDataException() {
    }

    public InvalidRegistrationDataException(String message) {
        super(message);
    }
}
