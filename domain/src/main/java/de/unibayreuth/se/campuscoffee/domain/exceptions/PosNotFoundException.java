package de.unibayreuth.se.campuscoffee.domain.exceptions;

public class PosNotFoundException extends RuntimeException {
    public PosNotFoundException(String message) {
        super(message);
    }
}
