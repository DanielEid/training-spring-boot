package com.ecommerce.microcommerce.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Internal Error Server. Please try again :)")
public class InternalError extends RuntimeException {

    public InternalError(String message) {

        super(message);
    }
}