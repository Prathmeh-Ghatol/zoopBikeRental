package com.zoopbike.application.utils;

import com.zoopbike.application.entity.ApplicationUser;
import com.zoopbike.application.entity.Bike;
import com.zoopbike.application.entity.BikeProviderPartner;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class CacheStore {
    public  Map< String ,ApplicationUser>applicationUserMap=new HashMap<>();


    public Map<String, BikeProviderPartner>bikeProviderPartnerMap=new HashMap<>();

    public Map<UUID,Bike>bikesCache=new ConcurrentHashMap();


}
