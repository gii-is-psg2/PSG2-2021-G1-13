package org.springframework.samples.petclinic.service.exceptions;

public class GeneralException extends RuntimeException{

    public GeneralException(String message){
        super(message);
    }
}
