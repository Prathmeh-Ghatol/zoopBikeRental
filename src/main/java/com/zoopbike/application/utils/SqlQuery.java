package com.zoopbike.application.utils;

import org.springframework.web.servlet.tags.form.SelectTag;

public class SqlQuery {

    public final static String findBikePattnerProviderByEmail= "select * from bike_provider where email = :email";
    public final static String findApplicationUserByEmail= "SELECT  * from  application_user where application_user_email =:email";
    public  final static String GetAllApplicationUser= "SELECT * from application_user";
    public final static String GetAllBikeOfBikeVender= "select * from bike where bike_partner_id =:id";

    public final static String  getAllBookingForApplicationUser="SELECT b.*, bb.date_book, bb.till_date FROM bike_booking bb JOIN bikes_booked bbk ON bb.booking_id = bbk.booking_id JOIN bike b ON bbk.bike_id = b.bike_id JOIN bike_user_book bub ON bb.booking_id = bub.booking_id WHERE bub.application_user_id  =:ApplicationId";

    public final static String getBikeByBookingId=" SELECT  bike_id FROM bikes_booked WHERE booking_id =:bookid";
}
