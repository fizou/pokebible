package com.pokebible.restapi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UrlPathHelper;

/**
 *
 * set JSON Custom SUCCESS Response Format
 * 
  {
    "timestamp": "2021/05/10 22:38:23",
    "status": 200,
    "statusLabel": "",
    "message": "Succesfull Authentication",
    "data": "[ { ... } ]",
    "path": "/api/auth/generateToken",
    "version": "1.0",
    "debug": "" 
  }
 * 
 */

public class CustomResponseAttributes {

    private static final Logger logger = LoggerFactory.getLogger(CustomResponseAttributes.class);
    
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private static final UrlPathHelper urlPathHelper = new UrlPathHelper();
    
    public static HttpServletResponse format(int status, String message, HttpServletRequest request, HttpServletResponse response) {

        logger.debug("Format Rest Json response for "+status+" - "+message);

        response.setStatus(status, ""); 
        response.setContentType("application/json");
        
        String path = urlPathHelper.getPathWithinApplication((HttpServletRequest) request);
                
        ApiResponse apiResponse = new ApiResponse(status, message, path);        

        logger.debug("Json Result: "+apiResponse.toJson());
        
        try {
            response.getWriter().write(apiResponse.toJson());
        } catch (Exception e) {
            logger.debug("Error on getWriter(): "+e);
        }

        return response;
    }

}