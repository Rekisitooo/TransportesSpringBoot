package com.transports.spring.exception;

public class PassengerDoesNotExistException extends TransportsException {

    protected PassengerDoesNotExistException(Throwable cause) {
        super(cause);
    }

    public PassengerDoesNotExistException(){
        super();
    }
}
