package com.pokebible.restapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 *  Trap following Exception:
 * 
    404 - RESOURCE NOT FOUND
    400 - BAD REQUEST
    401 - UNAUTHORIZED (Wrong Identification)
    403 - FORBIDDEN (Wrong Role)
    500 - SERVER ERROR
 *
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ApiExceptionBadRequest extends RuntimeException {
    
    public ApiExceptionBadRequest(String message) {
        
        super(message);
        
    }
}

