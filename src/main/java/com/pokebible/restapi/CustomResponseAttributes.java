package com.pokebible.restapi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static HttpServletResponse format(int status, String message, String path, HttpServletResponse response) {

        logger.debug("Format Rest Json response for "+status+" - "+message);

        response.setStatus(status, ""); 
        response.setContentType("application/json");
        
        ApiResponse apiResponse = new ApiResponse(status, message, path);        

/*
        String errorMessage = org.springframework.http.HttpStatus.resolve(status).name();
        if (errorMessage.length()> 1 ) errorMessage = errorMessage.substring(0, 1).toUpperCase() + errorMessage.substring(1).toLowerCase(); // Capitalize First Letter Only

        String body = "{ ";
        if (errorMessage.equals("Ok")) {
            body = "{ \"timestamp\": \""+dateFormat.format(new Date())+"\", \"status\": \""+status+"\", \"message\": \""+message+"\", \"path\": \""+path+"\", \"version\": \"1.0\"";
        } else {
            body = "{ \"timestamp\": \""+dateFormat.format(new Date())+"\", \"status\": \""+status+"\", \"error\": \""+errorMessage+"\", \"message\": \""+message+"\", \"path\": \""+path+"\", \"version\": \"1.0\"";
        }
        if (logger.isDebugEnabled()) {
            body = body + ", \"debug\": \"\" }";
        } else {
            body = body + " }";
        }
*/

        logger.debug("Json Result: "+apiResponse.toJson());
        
        try {
            response.getWriter().write(apiResponse.toJson());
        } catch (Exception e) {
            logger.debug("Error on getWriter(): "+e);
        }

        return response;
    }

}