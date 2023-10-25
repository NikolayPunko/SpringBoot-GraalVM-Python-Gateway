package com.example.SpringTestGraalVM.exceptions;

public class PersonNotCreatedException extends RuntimeException {
    public PersonNotCreatedException(String message){
        super(message);
    }
}
