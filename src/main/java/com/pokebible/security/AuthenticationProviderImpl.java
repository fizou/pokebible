package com.pokebible.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationProviderImpl implements AuthenticationProvider {

    /*
     * Custom authentification based on: 
     *    Give the ROLE_USER to user/password credential
     *    Give the ROLE_ADMIN to admin/password credential
     * 
     */

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationProviderImpl.class);
    
    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        
        String username = auth.getName();
        String password = auth.getCredentials().toString();
        
        logger.info("Username: "+username);
        logger.info("Password: "+password);
        
        if ("user".equals(username) && "password".equals(password)) {
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
            logger.info("Role affected: ROLE_USER");
            return new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
        } else if ("admin".equals(username) && "password".equals(password)) {
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            logger.info("Role affected: ROLE_ADMIN");
            return new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
        } else {
            logger.error("Access Refused: User Unknown");
            throw new BadCredentialsException("External system authentication failed");
        }
        

    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}