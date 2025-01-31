package com.transports.spring.exception;

public class PassengerDoesNotBelongToUserException extends TransportsException {

    protected PassengerDoesNotBelongToUserException(Throwable cause) {
        super(cause);
    }

    public PassengerDoesNotBelongToUserException(){
        super();
    }
}
