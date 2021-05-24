package com.pokebible.healthcheck;

import com.pokebible.PokemonService;
import com.pokebible.actuator.Metric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

/**
 * 
 * Healtcheck : Diplay Pokebible Metrics in metrics section
 * 
 * in HealthCheck: 
 *   http://localhost:8085/actuator/health - Ex "metrics":{"status":"UP","details":{"NumberOfPokemon":"151","counterRead":"3.0" ... }
 * in Actuator Metrics: 
 *   http://localhost:8085/actuator/metrics/com.fizou.pokebible.counter
 *   http://localhost:8085/actuator/metrics/com.fizou.pokebible.counter?availableTags=type%3Aread
 */
@Component("metrics")
public class HealthIndicatorMetrics extends AbstractHealthIndicator {

    private static final Logger logger = LoggerFactory.getLogger(HealthIndicatorExternalService.class);
    
    @Autowired
    private PokemonService service;

    @Autowired
    private Metric metrics;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        
        logger.info("Starting Metrics HealthCheck...");

        try {
                        
            logger.debug("HealthCheck Metrics UP");
            builder.up()
                .withDetail("NumberOfPokemon", ""+service.count())
                .withDetail(Metric.Type.DATABASE_ACCESS.getCounterName(), ""+metrics.get(Metric.Type.DATABASE_ACCESS))
                .withDetail(Metric.Type.API_ACCESS.getCounterName(), ""+metrics.get(Metric.Type.API_ACCESS))
                ;
        } catch (Exception e) {
            logger.error("HealthCheck Metrics Down: "+e);
            builder.down()
                .withDetail("error", ""+e)
                ;
        }
    }
}
