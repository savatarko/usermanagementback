package org.raf.usermanagement.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends CustomException{
    public UserAlreadyExistsException() {
        super("user with given email already exists!", ErrorCode.EMAIL_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
    }
}
