package com.pokebible.restapi;

/**
 *
 * set JSON Custom ERROR Format
 * 
  {
    "timestamp": "2021/05/10 22:38:23",
    "status": 500,
    "statusLabel": "Internal Server Error",
    "message": "This is a real big mistake....",
    "errors": "[ { ... } ]",
    "path": "/api/auth/generateToken",
    "version": "1.0",
    "debug": "com.pokebible.restapi.RestControllers: ..."  
  }
 * 
 */
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    private static final Logger logger = LoggerFactory.getLogger(CustomErrorAttributes.class);
    
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {

        // Let Spring handle the error first, we will modify later :)
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);

        // Rewrite response with selected field from errorAttributes
        LinkedHashMap<String, Object> errorAttributesOverride = new LinkedHashMap<String, Object>();
        errorAttributesOverride.put("timestamp", dateFormat.format(new Date()));
        errorAttributesOverride.put("status", errorAttributes.get("status"));
        errorAttributesOverride.put("statusLabel", errorAttributes.get("error"));
        errorAttributesOverride.put("message", errorAttributes.get("message"));
        errorAttributesOverride.put("errors", errorAttributes.get("errors"));
        errorAttributesOverride.put("version", "1.0");
        if (logger.isDebugEnabled()) errorAttributesOverride.put("debug", errorAttributes.get("trace"));

        //return errorAttributes;
        return errorAttributesOverride;
        
/*        
        // format & update timestamp
        Object timestamp = errorAttributes.get("timestamp");
        if (timestamp == null) {
            errorAttributes.put("timestamp", dateFormat.format(new Date()));
        } else {
            errorAttributes.put("timestamp", dateFormat.format((Date) timestamp));
        }

        // Rename error to statusLabel
        Object statusLabel = errorAttributes.get("error");
        errorAttributes.remove("error");
        errorAttributes.put("statusLabel", statusLabel);

        // Move Trace to the end
        Object trace = errorAttributes.get("trace");
        errorAttributes.remove("trace");

        // insert a new key
        errorAttributes.put("version", "1.0");
        
        
        return errorAttributes;
*/

    }

}