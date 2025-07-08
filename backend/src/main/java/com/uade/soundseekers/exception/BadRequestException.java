package com.uade.soundseekers.exception;

public class BadRequestException extends RuntimeException {

    // Constructor para solo el mensaje
    public BadRequestException(String message) {
        super(message);
    }

    // Constructor para el mensaje y la excepción original
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
