package com.zoopbike.application.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BikeProviderPartnerException extends RuntimeException{
   private String Message;
    private String Entity;
    public BikeProviderPartnerException(String message, String entity){
        super(String.format(message));
        this.Message=message;
        this.Entity=entity;
    }
}
