package com.pokebible.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.util.UrlPathHelper;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    /*
     * Handle HTTP 403 (Acces Denied security exception on LOGGED users) that go normaly to an error page
     * 
     * Redirect to login page or write json error depending of it is an API or un web access that generate this error 
     * 
     */

    private static final Logger logger = LoggerFactory.getLogger(AccessDeniedHandlerImpl.class);

    private static final UrlPathHelper urlPathHelper = new UrlPathHelper();

    RequestMatcher customFilterUrl = new AntPathRequestMatcher("/api/**");
    
    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       AccessDeniedException e) throws IOException, ServletException {

	logger.debug("SecurityDeniedHandler - handle");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String userName = "";
        if (auth != null) {
            userName=auth.getName();
	} else {
            userName="Anonymous";
	}
        
        logger.warn("User '" + userName + "' attempted to access the protected URL: ("+httpServletRequest.getMethod()+")" + httpServletRequest.getRequestURI());

        if (customFilterUrl.matches(httpServletRequest)) {
            // if error comes from API generate json error
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN, "");
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write("{\"status\":\""+HttpServletResponse.SC_UNAUTHORIZED+"\", \"message\":\"Your role does not permit to access this url.\"}");
        } else {
            // if error comes from other Requests than /api/** go to login page
            //httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/403");
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login");
        }
        
    }
}