package org.raf.usermanagement.exceptions;

import org.springframework.http.HttpStatus;

public class NoUserException extends CustomException{


    public NoUserException() {
        super("user with given email and password doesnt exist", ErrorCode.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
    }
}
