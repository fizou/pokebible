package com.pokebible.restapi;

import com.pokebible.actuator.PokebibleMetrics;
import java.io.IOException;
 
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
 
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;

/**
 *
 * This filter retreive JWT TOKEN in Authorization request header of API request and verify its validity
 * 
 * We arrive here by: 
 *  - GET http://localhost:8085/api/... 
 * 
 * Note: This filter is declare in WebSecurityConfigurerAdapterImpl Class
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
            
            logger.info("API token verification on " + urlPathHelper.getPathWithinApplication((HttpServletRequest) request));
            Authentication authentication = Credentials.verifyToken((HttpServletRequest) request);
            if (authentication!=null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("1");
            } else {
                // If user is already authenticate by his browser and try to access Api let him do it.
                //if  (SecurityContextHolder.getContext().getAuthentication()!=null) {
                //    logger.debug("2");
                //    //let him go role will be checked by security
                //    // means you don't need to provide token when logged. Provide token Only if not logged anonymous
                //} else {
                    logger.debug("3");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                //}
                
            }
            
        } else {
            // Ignore other Requests than /api/** : Rest API Authentification is only for API request
            logger.debug("Ignore API token verification on " + urlPathHelper.getPathWithinApplication((HttpServletRequest) request));
        }
         
        filterChain.doFilter(request, response);
    }
     
}
