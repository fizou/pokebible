package com.pokebible.security;

import com.pokebible.restapi.CustomResponseAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.util.UrlPathHelper;

/*
 * Handle HTTP 403 (Acces Denied security exception on ANONYMOUS users) that go normaly to an error page
 * 
 * Redirect to login page or write json error depending of it is an API or un web access that generate this error 
 * 
 */

@Component
public class BasicAuthenticationEntryPointImpl extends BasicAuthenticationEntryPoint  {

    private static final Logger logger = LoggerFactory.getLogger(BasicAuthenticationEntryPointImpl.class);

    private static final UrlPathHelper urlPathHelper = new UrlPathHelper();

    RequestMatcher customFilterUrl = new AntPathRequestMatcher("/api/**");
    
    @Override
    public void commence(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx) 
      throws IOException {
        
	logger.debug("BasicAuthenticationEntryPointImpl - handle");
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String userName = "";
        if (auth != null) {
            userName=auth.getName();
	} else {
            userName="Anonymous";
	}
        
        logger.warn("User '" + userName + "' attempted to access the protected URL: ("+request.getMethod()+")" + request.getRequestURI());

        if (customFilterUrl.matches(request)) {
            // if error comes from API go to error page to generate json error
            //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED, "");
            //response.setContentType("application/json");
            //response.getWriter().write("{\"status\":\""+HttpServletResponse.SC_UNAUTHORIZED+"\", \"message\":\"You must be authenticated to access this url. Please provide a valid token.\"}");
            
            response = CustomResponseAttributes.format(HttpServletResponse.SC_UNAUTHORIZED, "You must be authenticated to access this url. Please provide a valid token.", urlPathHelper.getPathWithinApplication((HttpServletRequest) request), response);


        } else {
            // if error comes from other Requests than /api/** go to login page
            //httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/403");
            response.sendRedirect(request.getContextPath() + "/login");
        }

}

    @Override
    public void afterPropertiesSet() {

	logger.debug("BasicAuthenticationEntryPointImpl - handle");

        setRealmName("Baeldung");
        super.afterPropertiesSet();
    }

}
