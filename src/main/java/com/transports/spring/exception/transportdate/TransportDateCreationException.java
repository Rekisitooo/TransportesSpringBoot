package com.transports.spring.exception.transportdate;

import com.transports.spring.exception.TransportsException;

public class TransportDateCreationException extends TransportsException {

    public TransportDateCreationException() {
        super("Date creation failed.");
    }
}
