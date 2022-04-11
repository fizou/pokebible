package com.pokebible.restapi;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
//import io.swagger.annotations.ApiModelProperty;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * This is the JSON custom Reponse of Pokebible REST API 
 * 
 */
@JsonPropertyOrder({"timestamp", "status", "statusLabel", "message", "path", "version", "debug" })
public class ApiResponse {
    
    private static final Logger logger = LoggerFactory.getLogger(ApiResponse.class);
    
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

//    @ApiModelProperty(position = 0)
    private String timestamp;
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

//    @ApiModelProperty(position = 1)
    private String status;
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

//    @ApiModelProperty(position = 2)
    private String statusLabel;
    public String getStatusLabel() {
        return statusLabel;
    }
    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

//    @ApiModelProperty(position = 3)
    private String message;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

//    @ApiModelProperty(position = 4)
    private String path;
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

//    @ApiModelProperty(position = 5)
    private String version;
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

//    @ApiModelProperty(position = 6)
    private String debug;
    public String getDebug() {
        return debug;
    }
    public void setDebug(String debug) {
        this.debug = debug;
    }

    
    public ApiResponse(int status, String message, String path) {

        setTimestamp(dateFormat.format(new Date()));
        setStatus(""+status);

        String statusLabel = org.springframework.http.HttpStatus.resolve(status).name();
        if (statusLabel.length()> 1 ) statusLabel = statusLabel.substring(0, 1).toUpperCase() + statusLabel.substring(1).toLowerCase(); // Capitalize First Letter Only
        setStatusLabel(""+statusLabel);

        setMessage(message);
        setPath(path);
        setVersion("1.0");
        setDebug("");
        
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(this);
        } catch (Exception e) {
            logger.error("Cannot transform "+this.toString()+" to json: "+e);
        }
        logger.debug("Json: "+jsonString);
        return jsonString;
        
    }
    
}

