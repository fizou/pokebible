package com.pokebible.healthcheck;

import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * 
 * Healtcheck : Specific indicator
 * 
 */
@Component("externalservice")
public class ExternalService implements HealthIndicator {

    private static final Logger logger = LoggerFactory.getLogger(ExternalService.class);

    //private static final String URL = "https://cleanuri.com/api/v1/shorten"; //OK
    private static final String URL = "https://test"; //KO

    @Override
    public Health health() {
        logger.info("Starting External Service HealthCheck...");
        try {
            // check if url shortener service url is reachable
            Socket socket = new Socket(new java.net.URL(URL).getHost(),80);

            logger.debug("HealthCheck External Service UP");
            return Health
                    .up()
                    .build();
        } catch (Exception e) {
            
            logger.error("HealthCheck External Service Down Failed to connect to: {} : "+e, URL);
            return Health
                    .down()
                    .withDetail("url", URL)
                    .withDetail("error", ""+e)
                    .build();
        }
    }



    
}
