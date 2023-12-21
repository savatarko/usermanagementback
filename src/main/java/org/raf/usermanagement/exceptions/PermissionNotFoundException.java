package org.raf.usermanagement.exceptions;

import org.springframework.http.HttpStatus;

public class PermissionNotFoundException extends CustomException{
    public PermissionNotFoundException() {
        super("permission not found!", ErrorCode.PERMISSION_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
