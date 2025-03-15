package com.transports.spring.exception;

public class InvolvedDoesNotExistException extends TransportsException {

    protected InvolvedDoesNotExistException(Throwable cause) {
        super(cause);
    }

    public InvolvedDoesNotExistException(){
        super();
    }
}
