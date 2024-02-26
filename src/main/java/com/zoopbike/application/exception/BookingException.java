package com.zoopbike.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookingException extends RuntimeException{

    private String Message;
    private String Entity;
    public BookingException(String message, String entity){
        super(String.format("Exception throw %s and cause is %s",message,entity));
        this.Message=message;
        this.Entity=entity;
    }
}
