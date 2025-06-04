package de.unibayreuth.se.campuscoffee.domain.exceptions;

public class InvalidHouseNumberException extends RuntimeException {
    public InvalidHouseNumberException(String message) {
            super(message);
        }
}
