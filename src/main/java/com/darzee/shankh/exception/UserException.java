package com.darzee.shankh.exception;

import org.springframework.http.HttpStatus;

public class UserException extends Exception{

    private HttpStatus statusCode;

    private Integer code;
    private String type;
    private String message;

    public UserException(String message) {
        new UserException(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public UserException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
        this.code = statusCode.value();
    }

}
