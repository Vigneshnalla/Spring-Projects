package com.vignesh.hotel.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(){
        super("Resource Not Found On Server");
    }

    public ResourceNotFoundException(String messge){
        super(messge);
    }
}
