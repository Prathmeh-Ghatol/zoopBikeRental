package com.zoopbike.application.utils;

import org.springframework.web.servlet.tags.form.SelectTag;

public class SqlQuery {

    public final static String findBikePattnerProviderByEmail= "SELECT * FROM bike_provider_partner WHERE email = :email";
    public final static String findApplicationUserByEmail= "SELECT  * from  application_user where application_user_email =:email";
    public  final static String GetAllApplicationUser= "SELECT * from application_user";

}
