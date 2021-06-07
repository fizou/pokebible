package com.pokebible.actuator;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Metric {

    /**
     *
     * Declaration of Pokebible Metrics (database.access, api.access, ...) and Method to Work with Actuator Metrics 
     * 
     * Metric are acessible on 
     * http://localhost:8085/actuator/metrics
     * http://localhost:8085/actuator/metrics/com.pokebible.counters.database.access
     * http://localhost:8085/actuator/metrics/com.pokebible.counters.database.access?availableTags=type%3Aread
     *
     */
    private static final Logger logger = LoggerFactory.getLogger(Metric.class);

    // Actuator Metrics
    private final MeterRegistry meterRegistry;
    
    public Metric(MeterRegistry meterRegistry) {

        logger.info("Starting Actuator (Metrics Initialization)...");

        this.meterRegistry = meterRegistry; 
                
        for(Type counter : Type.values())
        {
            logger.debug("Counter Declaration "+counter.getFullPathCounter()+": "+get(counter));
        }
                        
    }

    private final static String CounterPath = "com.pokebible.counters";

    public enum Type
    {
        // Counters Declaration
        DATABASE_ACCESS("database.access"),     
        API_ACCESS("api.access"),     

        ;         


        private String counterName;

        private Type(String counterName) {
            this.counterName = counterName;
        }

        public String getCounterName() {
            return counterName;
        }
        
        public String getFullPathCounter() {
            return CounterPath+"."+counterName;
        }

        public String getName() {
            return name();
        }
    }
    
    private Map<String, Counter> counters = new HashMap<>();

    public Counter create(Type type){
        //Counter counter = Counter.builder(CounterPath+"."+type.getCounterName()).tag("type", "create")description("").baseUnit("Counter").register(meterRegistry);
        Counter counter = Counter.builder(CounterPath+"."+type.getCounterName()).description("").baseUnit("Counter").register(meterRegistry);
        counters.put(type.getName(), counter);
        
        return counter;
    }

    public void increment(Type type){

        Counter counter = counters.get(type.getName());
        if(counter == null) {
            counter = create(type);
        }
        
        counter.increment();

        logger.debug("Counter increment: "+type.getFullPathCounter()+" - Value: "+counter.count());

    }
    
    public double get(Type type){

        Counter counter = counters.get(type.getName());
        if(counter == null) {
            counter = create(type);
        }

        return counter.count();
    
    }
}
