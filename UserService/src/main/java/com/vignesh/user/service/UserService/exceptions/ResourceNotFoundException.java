package com.vignesh.user.service.UserService.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(){
        super("Resource Not Found On Server");
    }

    public ResourceNotFoundException(String messge){
        super(messge);
    }
}
