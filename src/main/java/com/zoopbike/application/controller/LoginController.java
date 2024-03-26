package com.zoopbike.application.controller;

import com.zoopbike.application.config.AuthenticationProvider;
import com.zoopbike.application.config.JwtUtil;
import com.zoopbike.application.dto.logInDto;
import com.zoopbike.application.utils.LoadUser;
import org.apache.http.Header;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class LoginController {
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private LoadUser loadUser;
    @PostMapping(value = "/login")
    public ResponseEntity<Header>logIn(@RequestBody logInDto log) throws Exception {
        String userName=log.getUserName();
        String userPass=log.getPassword();
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    log.getUserName(), log.getPassword());
            // Authenticate the user using AuthenticationManager
            // Try to authenticate the user with provided username and password
            authenticationProvider.authenticate(authentication);
        } catch (Exception e) {
            // If authentication fails, throw an exception with a message
            throw new Exception("Invalid username or password");
        }

        // If authentication is successful, load UserDetails for the authenticated user

        final UserDetails userDetails = loadUser.loadUserByUsername(log.getUserName());

        // Generate a JWT token using UserDetails of the authenticated user
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwt);
        // Return the JWT token in the response body
        return ResponseEntity.ok().headers(headers).body(null);
    }
}



