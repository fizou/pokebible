package com.pokebible.actuator;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PokebibleMetrics {

    /**
     *
     * Declaration and Method to Work with Actuator Metrics 
     * 
     * Metric are acessible on 
     * http://localhost:8085/actuator/metrics
     * http://localhost:8085/actuator/metrics/com.fizou.pokebible.counters.database
     * http://localhost:8085/actuator/metrics/com.fizou.pokebible.counters.database?availableTags=type%3Aread
     *
     */
    private static final Logger logger = LoggerFactory.getLogger(PokebibleMetrics.class);


    // Actuator Metrics
    private final MeterRegistry meterRegistry;
    public Counter counterAccess;
    public Counter counterRead;
    public Counter counterCreate;
    public Counter counterUpdate;
    public Counter counterDelete;
    
    public PokebibleMetrics(MeterRegistry meterRegistry) {

        logger.info("Starting Actuator (Metrics Initialization)...");

        this.meterRegistry = meterRegistry; 
        
        /*
        //counterAccess = Counter.builder("com.fizou.pokebible.counters") // Using the fluent API
        //        .description("The number of request on database")
        //        .register(meterRegistry);
        counterRead = Counter.builder("com.fizou.pokebible.counters2") // Using the fluent API
                .tag("type", "read")
                .description("The number of request to find pokemons")
                .register(meterRegistry);

        counterCreate = meterRegistry.counter("com.fizou.pokebible.counters", "type", "create"); 
        counterUpdate = meterRegistry.counter("com.fizou.pokebible.counters", "type", "update"); 
        counterDelete = meterRegistry.counter("com.fizou.pokebible.counters", "type", "delete"); 

        counterRead.increment();
        counterUpdate.increment();
        counterDelete.increment();
        */
        
        for(Counters counter : Counters.values())
        {
            //logger.debug("Counter Declaration "+counters.getFullPathCounterTagName());
            logger.debug("Counter Declaration "+counter.getFullPathCounterTagName()+": "+get(counter));
        }
        
        //increment(Counters.DATABASE_READ);
                
    }

    private final static String CounterPath = "com.fizou.pokebible.counters";

    public enum Counters
    {
        // Counters Declaration
        DATABASE_ACCESS("database"),     
        API_ACCESS("api.access"),     

        ;         


        private String counterName;

        private Counters(String counterName) {
            this.counterName = counterName;
        }

        public String getCounterName() {
            return counterName;
        }
        
        public String getFullPathCounterTagName() {
            return CounterPath+"."+counterName;
        }

        public String getName() {
            return name();
        }
    }
    
    private Map<String, Counter> counters = new HashMap<>();

    public Counter create(Counters tag){
        // com.fizou.pokebible.counters.database read 
        Counter counter = Counter.builder(CounterPath+"."+tag.getCounterName()).description("").baseUnit("Counter").register(meterRegistry);
        counters.put(tag.getName(), counter);
        
        return counter;
    }

    public void increment(Counters tag){

        Counter counter = counters.get(tag.getName());
        if(counter == null) {
            counter = create(tag);
        }
        
        //logger.debug("Metric Read before increment: "+tag.getFullPathCounterTagName()+" - Value: "+counter.count());

        counter.increment();

        logger.debug("Counter increment: "+tag.getFullPathCounterTagName()+" - Value: "+counter.count());

    }
    
    public double get(Counters tag){

        Counter counter = counters.get(tag.getName());
        if(counter == null) {
            counter = create(tag);
        }

        //logger.debug("Metric Read: "+tag.getFullPathCounterTagName()+" - Value: "+counter.count());
        
        return counter.count();
    
    }
}
