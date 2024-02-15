package com.example.SpringBootGraalVMPythonGateway.exceptions;

public class UserOrgNotCreatedException extends RuntimeException {
    public UserOrgNotCreatedException(String message){
        super(message);
    }
}
