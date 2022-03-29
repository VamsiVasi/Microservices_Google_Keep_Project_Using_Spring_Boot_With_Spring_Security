package com.albanero.User_Microservice_.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class Resource_NotFound_Exception_ extends Exception{

    private static final long serialVersionUID = 1L;

    public Resource_NotFound_Exception_(String message){
        super(message);
    }
}