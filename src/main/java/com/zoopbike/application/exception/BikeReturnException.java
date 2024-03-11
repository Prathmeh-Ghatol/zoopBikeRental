package com.zoopbike.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BikeReturnException extends RuntimeException {

    String Entity;
    String Message;

    public BikeReturnException(String Message, String entity){
        super(String.format("Exception caugth due to resasone %s and entity %s ", Message,entity));
        this.Entity=entity;
        this.Message=Message;
    }
}
