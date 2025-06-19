package com.ead.authuser.exceptions;

public class DuplicatedUsernameException extends RuntimeException{

    public DuplicatedUsernameException(String message) {
        super(message);
    }
}
