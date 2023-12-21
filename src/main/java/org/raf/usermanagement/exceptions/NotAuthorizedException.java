package org.raf.usermanagement.exceptions;

import org.springframework.http.HttpStatus;

public class NotAuthorizedException extends CustomException{
    public NotAuthorizedException() {
        super("You are not authorized for this action!", ErrorCode.NOT_AUTHORIZED, HttpStatus.FORBIDDEN);
    }
}
