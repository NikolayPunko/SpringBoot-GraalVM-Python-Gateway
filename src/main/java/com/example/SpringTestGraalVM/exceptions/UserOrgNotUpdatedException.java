package com.example.SpringTestGraalVM.exceptions;

public class UserOrgNotUpdatedException extends RuntimeException {
    public UserOrgNotUpdatedException(String message){
        super(message);
    }
}
