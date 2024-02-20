package com.zoopbike.application.exception;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Builder
public class BadaddressException extends RuntimeException{
    private String Message;
    private String Entity;
    public BadaddressException(String message, String entity){
        super(message);
            this.Message=message;
            this.Entity=entity;
    }


}
