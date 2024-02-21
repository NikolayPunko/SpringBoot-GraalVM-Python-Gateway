package com.example.SpringBootGraalVMPythonGateway.exceptions;

public class EdocNotFoundException extends RuntimeException {
    public EdocNotFoundException(String message){
        super(message);
    }
}
