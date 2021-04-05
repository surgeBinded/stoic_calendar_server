package com.cebotariropot.stoiccalendarserver.stoic_calendar_server.exceptions;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String errorMessage){
        super(errorMessage);
    }
}
