package com.pokebible.restapi;

import java.io.IOException;
import java.util.Collections;
 
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.util.UrlPathHelper;
 
/**
 *
 * This filter ADD JWT TOKEN in Authorization request header if authentification is successfull
 * 
 * We arrive here by: 
 *  - GET http://localhost:8085/api/generateToken?username=user&password=password OR
 *  - POST http://localhost:8085/api/generateToken + username=user&password=password in body OR
 *  - POST http://localhost:8085/api/generateToken + {"username": "admin", "password": "password"} in body
 * 
 * Note: /api/generateToken is declare in WebSecurityConfigurerAdapterImpl Class (Declaration in RestControllers.java is just for documentation, see comment there)
 * 
    .anyRequest().authenticated()
    .and()
    .addFilterBefore(new FilterGenerateToken("/api/generateToken", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
 * 
 */

public class FilterGenerateToken extends AbstractAuthenticationProcessingFilter {
 
    private static final Logger logger = LoggerFactory.getLogger(FilterGenerateToken.class);
 
    private static final UrlPathHelper urlPathHelper = new UrlPathHelper();
    
    private ApiCredentials credentials;

    public FilterGenerateToken(String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
    }
 
    @Override
    // Get username and password and try to authentify the user
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
 
        logger.debug(urlPathHelper.getPathWithinApplication((HttpServletRequest) request)+" attemptAuthentication");

        credentials = new ApiCredentials(request); // get user/password in Request parameter or body
        logger.info(urlPathHelper.getPathWithinApplication((HttpServletRequest) request)+" attemptAuthentication with username/password ("+credentials.getUsername()+"/"+credentials.getPassword()+")");
 
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword(), Collections.emptyList()));
    }
 
    @Override
    // if username and password are valid, create token and put it in response....
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
 
        logger.debug("Successful authentication on " + urlPathHelper.getPathWithinApplication((HttpServletRequest) request) + " - user: "+authResult.getName());
 
        // Write Authorization to Headers of Response.
        ApiCredentials.CreateToken(response, authResult);
 
        logger.info("Successful authentication on " + urlPathHelper.getPathWithinApplication((HttpServletRequest) request) + " - user: "+authResult.getName() + " - token:"+response.getHeader("Authorization"));

        response = CustomResponseAttributes.format(HttpServletResponse.SC_OK, "Successful authentication. Check authorization header of this response to retreive token.", urlPathHelper.getPathWithinApplication((HttpServletRequest) request), response);
                
    }

    @Override
    // if username and password are invalid, response an error
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        
        logger.error("Failed authentication on " + urlPathHelper.getPathWithinApplication((HttpServletRequest) request)  + " - user: "+credentials.getUsername());
        
        //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED, "");
        //response.setContentType("application/json");
        //response.getWriter().write("{\"Status\":\""+HttpServletResponse.SC_FORBIDDEN+"\", \"message\":\"Authentication failed: Wrong User or Password.\"}");

        response = CustomResponseAttributes.format(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed. Wrong User or Password.", urlPathHelper.getPathWithinApplication((HttpServletRequest) request), response);

    }    
}