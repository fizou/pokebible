package com.pokebible.healthcheck;

import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * 
 * Healtcheck : Display the result of the test of an fake external url in externalservice section.
 * 
 */
@Component("externalservice")
public class HealthIndicatorExternalService implements HealthIndicator {

    private static final Logger logger = LoggerFactory.getLogger(HealthIndicatorExternalService.class);

    //private static final String URL = "https://cleanuri.com/api/v1/shorten"; //OK
    private static final String URL = "https://q-routing-mpa.prosodie.com/ro52/ui"; //KO

    @Override
    public Health health() {
        logger.info("Starting External Service HealthCheck...");
        try {
            // check if url shortener service url is reachable
            Socket socket = new Socket(new java.net.URL(URL).getHost(),443);

            logger.debug("HealthCheck External Service UP");
            return Health
                    .up()
                    .withDetail("url", URL)
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
