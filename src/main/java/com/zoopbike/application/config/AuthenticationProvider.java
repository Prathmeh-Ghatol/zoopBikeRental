package com.zoopbike.application.config;

import com.zoopbike.application.entity.ApplicationUser;
import com.zoopbike.application.entity.BikeProviderPartner;
import com.zoopbike.application.repo.ApplicationUserRepo;
import com.zoopbike.application.repo.BikePartnerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    @Autowired
    ApplicationUserRepo applicationUserRepo;

    @Autowired
    BikePartnerRepo bikeProviderPartnerRepo;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        ApplicationUser applicationUser = applicationUserRepo.findApplicationUserByEmail(username);
        BikeProviderPartner bikeProviderPartner = bikeProviderPartnerRepo.findBikeProviderPartner(username);

        if (bikeProviderPartner==null &&applicationUser != null && password.equals(applicationUser.getPassword())) {
            List<GrantedAuthority> authorities = new ArrayList<>(applicationUser.getAuthorities());
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        }else if (applicationUser==null &&
                bikeProviderPartner!=null && password==bikeProviderPartner.getPassword()){
            List<GrantedAuthority>authorities=new ArrayList<>(bikeProviderPartner.getAuthorities());
            return new UsernamePasswordAuthenticationToken(bikeProviderPartner.getEmail(),null,authorities);
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
