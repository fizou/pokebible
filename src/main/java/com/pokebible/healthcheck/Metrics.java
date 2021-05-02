package com.pokebible.healthcheck;

import com.pokebible.PokemonRepository;
import com.pokebible.PokemonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

/**
 * 
 * Healtcheck : Diplay Pokebible Metrics 
 * 
 * in HealthCheck: 
 *   http://localhost:8085/actuator/health - Ex "metrics":{"status":"UP","details":{"NumberOfPokemon":"151","counterRead":"3.0" ... }
 * in Actuator Metrics: 
 *   http://localhost:8085/actuator/metrics/com.fizou.pokebible.counter
 *   http://localhost:8085/actuator/metrics/com.fizou.pokebible.counter?availableTags=type%3Aread
 */
@Component("metrics")
public class Metrics extends AbstractHealthIndicator {

    private static final Logger logger = LoggerFactory.getLogger(ExternalService.class);

    @Autowired
    private PokemonRepository repository;
    
    @Autowired
    private PokemonService service;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        
        logger.info("Starting Metrics HealthCheck...");

        try {
            
            long NumberOfPokemon = repository.count();
            logger.debug("NumberOfPokemon:"+repository.count());
            Double counterRead=service.counterRead.count();
            logger.debug("counterRead:"+counterRead);
            Double counterCreate=service.counterCreate.count();
            logger.debug("counterCreate:"+counterCreate);
            Double counterUpdate=service.counterUpdate.count();
            logger.debug("counterUpdate:"+counterUpdate);
            Double counterDelete=service.counterDelete.count();
            logger.debug("counterDelete:"+counterDelete);
            
            logger.debug("HealthCheck Metrics UP");
            builder.up()
                .withDetail("NumberOfPokemon", ""+NumberOfPokemon)
                .withDetail("PokemonRead", ""+counterRead)
                .withDetail("PokemonCreate", ""+counterCreate)
                .withDetail("PokemonUpdate", ""+counterUpdate)
                .withDetail("PokemonDelete", ""+counterDelete)
                ;
        } catch (Exception e) {
            logger.error("HealthCheck Metrics Down: "+e);
            builder.down()
                .withDetail("error", ""+e)
                ;
        }
    }
}
