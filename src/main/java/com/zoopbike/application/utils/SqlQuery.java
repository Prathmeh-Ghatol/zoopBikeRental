package com.zoopbike.application.utils;

import org.springframework.web.servlet.tags.form.SelectTag;

public class SqlQuery {

    public final static String findBikePattnerProviderByEmail= "select * from bike_provider where email = :email";
    public final static String findApplicationUserByEmail= "SELECT  * from  application_user where application_user_email =:email";
    public  final static String GetAllApplicationUser= "SELECT * from application_user";

}
