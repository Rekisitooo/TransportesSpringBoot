package com.transports.spring.exception;

public class InvalidDataInputException extends TransportsException {

    public InvalidDataInputException() {
        super("Incorrect data input.");
    }
}
