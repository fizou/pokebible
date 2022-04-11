package com.pokebible.restapi;

import java.io.IOException;
 
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;

/**
 *
 * This filter RETREIVE AND VERIFY JWT TOKEN in Authorization request header of API request and verify its validity
 * 
 * We arrive here by: 
 *  - GET http://localhost:8085/api/... 
 * 
 * Note: This filter is declared/activated in WebSecurityConfigurerAdapterImpl Class
 * 
    .anyRequest().authenticated()
    .and()
    .addFilterBefore(new FilterRestApiAuthentication(), UsernamePasswordAuthenticationFilter.class)
 * 
 */

public class FilterRestApiAuthentication extends GenericFilterBean {
     
    private static final Logger logger = LoggerFactory.getLogger(FilterRestApiAuthentication.class);

    private static final UrlPathHelper urlPathHelper = new UrlPathHelper();

    RequestMatcher customFilterUrl = new AntPathRequestMatcher("/api/**");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        
        if (customFilterUrl.matches(httpServletRequest)) {
            // Verify authentification only on API request
            
            logger.debug("API token verification on " + urlPathHelper.getPathWithinApplication((HttpServletRequest) request));
            Authentication authentication = ApiCredentials.verifyToken((HttpServletRequest) request);
            if (authentication!=null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("Case 1 - Authentication with token successfull - Set SecurityContextHolder");
            } else {
                // If user is already authenticate by his browser and try to access Api let him do it.
                if  (SecurityContextHolder.getContext().getAuthentication()!=null) {
                    logger.debug("Case 2 - Authentication with token failed BUT the user is already authenticated in its browser");
                    //let him go role will be checked by security
                    // means you don't need to provide token when logged. Provide token Only if not logged anonymous
                } else {
                    logger.debug("Case 3 - Authentication with token failed - Set SecurityContextHolder to null");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                
            }
            
        } else {
            // Ignore other Requests than /api/** : Rest API Authentification is only for API request
            logger.debug("Ignore API token verification on " + urlPathHelper.getPathWithinApplication((HttpServletRequest) request));
        }
         
        filterChain.doFilter(request, response);
    }
     
}
