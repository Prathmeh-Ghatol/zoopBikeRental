package com.zoopbike.application.utils;

import org.hibernate.grammars.hql.HqlParser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class zoopBikeRentalApplicationConstant {


    public final static String defualtApplicationPageNO="0";
    public final static  String defaultApplicationPageSize="5";

    public LocalDateTime getCurrentDateTime(){
        return  LocalDateTime.now();

    }

    public final static Integer zeroHour=0;
    public final static Integer zeroDay=0;

}
