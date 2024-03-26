package com.zoopbike.application.utils;

import org.hibernate.grammars.hql.HqlParser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class zoopBikeRentalApplicationConstant {


    public final static String defualtApplicationPageNO="0";
    public final static  String defaultApplicationPageSize="5";

    public  LocalDateTime getCurrentDateTime(){
        return  LocalDateTime.now();


    }

    public final static Integer zeroHour=0;
    public final static Integer zeroDay=0;

    public static Map<String,String>RoleMaps=new HashMap<>();
    static {
        RoleMaps.put("Application_user", "ROLE_APPLICATION_USER");
        RoleMaps.put("BikeProvider_user", "ROLE_BIKEPROVIDER_USER");
        RoleMaps.put("Admin_user", "ROLE_ADMIN");


    }
}
