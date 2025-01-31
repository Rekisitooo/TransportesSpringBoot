package com.transports.spring.exception;

public class TransportsException extends Exception {

    protected TransportsException(final Throwable cause){
        super(cause);
    }

    protected TransportsException(){
        super();
    }
}
