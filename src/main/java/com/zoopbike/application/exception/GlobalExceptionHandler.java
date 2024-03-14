package com.zoopbike.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private Map<String , String>exceptionMap;

    @ExceptionHandler(value = ReviewException.class)
    public ResponseEntity<Map<String,String>> applicationUserException(ReviewException applicationUserException){
        exceptionMap=new HashMap<>();
        exceptionMap.put("Error Message", applicationUserException.getMessage());
        exceptionMap.put("Entity",applicationUserException.getEntity());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionMap);

    } @ExceptionHandler(value = BadaddressException.class)
    public ResponseEntity<Map<String,String>> badaddressException(BadaddressException badaddressException){
        exceptionMap=new HashMap<>();
        exceptionMap.put("Error Message", badaddressException.getMessage());
        exceptionMap.put("Entity",badaddressException.getEntity());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionMap);

    }
    @ExceptionHandler(value = BikeProviderPartnerException.class)
    public ResponseEntity<Map<String,String>> bikeProviderPartnerException(BikeProviderPartnerException bikeProviderPartnerException){
        exceptionMap=new HashMap<>();
        exceptionMap.put("Error Message", bikeProviderPartnerException.getMessage());
        exceptionMap.put("Entity",bikeProviderPartnerException.getEntity());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionMap);

    }
    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Map<String,String>> bikeProviderPartnerException(SQLIntegrityConstraintViolationException bikeProviderPartnerException){
        exceptionMap=new HashMap<>();
        exceptionMap.put("Error Message", bikeProviderPartnerException.getMessage());
        exceptionMap.put("Entity",bikeProviderPartnerException.getSQLState());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionMap);

    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> MethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){
        exceptionMap=new HashMap<>();
        exceptionMap.put("Entity",methodArgumentNotValidException.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionMap);

    }
    @ExceptionHandler(value = BadBikeException.class)
    public ResponseEntity<Map<String,String>> BikeException(BadBikeException badBikeException){
        exceptionMap=new HashMap<>();
        exceptionMap.put("Message",badBikeException.getMessage());
        exceptionMap.put("Entity",badBikeException.getEntity());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionMap);

    }
    @ExceptionHandler(value = BookingException.class)
    public ResponseEntity<Map<String,String>> BookingException(BookingException bookingException){
        exceptionMap=new HashMap<>();
        exceptionMap.put("Message",bookingException.getMessage());
        exceptionMap.put("Entity",bookingException.getEntity());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionMap);

    }

    @ExceptionHandler(value = BikeSearchingException.class)
    public ResponseEntity<Map<String,String>> BikeSearchingException(BikeSearchingException bikeSearchingException) {
        exceptionMap = new HashMap<>();
        exceptionMap.put("Message", bikeSearchingException.getMessage());
        exceptionMap.put("Entity", bikeSearchingException.getEntity());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionMap);
    }
    @ExceptionHandler(value = BikeReturnException.class)
    public ResponseEntity<Map<String,String>> BikeReturnExcpetion(BikeReturnException bikeReturnException) {
        exceptionMap = new HashMap<>();
        exceptionMap.put("Message", bikeReturnException.getMessage());
        exceptionMap.put("Entity", bikeReturnException.getEntity());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionMap);
    }
        //    @ExceptionHandler(value = Loca.class)
//    public ResponseEntity<Map<String,String>> BikeSearchingException(BikeSearchingException bikeSearchingException){
//        exceptionMap=new HashMap<>();
//        exceptionMap.put("Message",bikeSearchingException.getMessage());
//        exceptionMap.put("Entity",bikeSearchingException.getEntity());
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionMap);
//
//    }
}
