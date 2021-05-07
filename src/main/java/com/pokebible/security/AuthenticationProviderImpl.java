package com.pokebible.security;

import java.util.ArrayList;
import java.util.List;
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
     * Custom authentification method based on username/password credential provided
     *
     *    Give the ROLE_ADMIN to admin/password 
     *    Give the ROLE_USER to user/password 
     * 
     */

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationProviderImpl.class);
    
    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        
        String username = auth.getName();
        String password = "";
        if (auth.getCredentials()!=null) password=auth.getCredentials().toString();
        
        logger.debug("Username: "+username);
        logger.debug("Password: "+password);
        
        // Call external IDP here...

        // For this project it is hardcoded
        if ("user".equals(username) && "password".equals(password)) {
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
            logger.debug("Role: ROLE_USER");
            return new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
        } else if ("admin".equals(username) && "password".equals(password)) {
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            logger.debug("Role: ROLE_ADMIN");
            return new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
        } else {
            logger.warn("Access refused: Username/password ("+username+"/"+password+") is unknown");
            throw new BadCredentialsException("External system authentication failed");
        }

    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}