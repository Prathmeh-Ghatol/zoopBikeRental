package com.zoopbike.application.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class ReviewException extends RuntimeException{
    private String Message;
    private String Entity;
    public ReviewException(String message, String entity){
        super(String.format("Exception throw %s and cause is %s",message,entity));
        this.Message=message;
        this.Entity=entity;
    }
}
