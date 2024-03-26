package com.zoopbike.application.utils;

import com.zoopbike.application.entity.ApplicationUser;
import com.zoopbike.application.entity.BikeProviderPartner;
import com.zoopbike.application.repo.ApplicationUserRepo;
import com.zoopbike.application.repo.BikePartnerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoadUser implements UserDetailsService {
    private ApplicationUserRepo applicationUserRepo;
    private BikePartnerRepo bikePartnerRepo;
    @Autowired
    public LoadUser(ApplicationUserRepo applicationUserRepo, BikePartnerRepo bikePartnerRepo){
        this.applicationUserRepo=applicationUserRepo;
        this.bikePartnerRepo=bikePartnerRepo;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationUser=null;
        BikeProviderPartner bikeProviderPartner=null;
        ApplicationUser applicationUser1=this.applicationUserRepo.findApplicationUserByEmail(username);
        if(applicationUser1!=null){
            return applicationUser1;
        }
        BikeProviderPartner bikeProviderPartner1=this.bikePartnerRepo.findBikeProviderPartner(username);
            if(bikeProviderPartner1!=null){
                return bikeProviderPartner1;
            }
            return null;
        }

    }

