package com.example.SpringBootGraalVMPythonGateway.exceptions;

public class UserOrgNotUpdatedException extends RuntimeException {
    public UserOrgNotUpdatedException(String message){
        super(message);
    }
}
