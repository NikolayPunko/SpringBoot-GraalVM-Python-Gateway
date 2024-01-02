package com.example.SpringTestGraalVM.exceptions;

public class UserOrgNotCreatedException extends RuntimeException {
    public UserOrgNotCreatedException(String message){
        super(message);
    }
}
