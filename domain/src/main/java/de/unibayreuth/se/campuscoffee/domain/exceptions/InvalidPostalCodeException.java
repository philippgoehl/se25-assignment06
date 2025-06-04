package de.unibayreuth.se.campuscoffee.domain.exceptions;

public class InvalidPostalCodeException extends RuntimeException {
    public InvalidPostalCodeException(String message) {
            super(message);
        }
}
