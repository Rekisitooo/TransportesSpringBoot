package com.transports.spring.exception;

public class TransportsException extends Exception {

    protected TransportsException(final Throwable cause){
        super(cause);
    }

    protected TransportsException(final String errorMessage){
        super(errorMessage);
    }

    protected TransportsException(){
        super();
    }
}
