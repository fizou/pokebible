package com.pokebible;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApplicationTests {
        
    private final static Logger logger = LoggerFactory.getLogger(ApplicationTests.class);
 
    @Autowired
    private PokemonService service;
   
    @Test
    public void contextLoads() {
        logger.debug("ApplicationTests - Begin");

        logger.info("ApplicationTests - Database Size ("+service.count()+")");
        assertTrue(service.count()!=0);

        logger.info("ApplicationTests - Insert Missing No");
    	service.save(new Pokemon("000","Missing No",Pokemon.Type.NORMAL,Pokemon.Type.NORMAL,"Sans Numero"));
        logger.info("ApplicationTests - *** Repository Size after insert("+service.count()+")");
        assertTrue(service.count()!=0);

        logger.info("ApplicationTests - Read All Repository");
        int cpt=0;
        Iterator<Pokemon> iterator = service.findAll().iterator();
        while (iterator.hasNext()){
            Pokemon pokemon = iterator.next();
            logger.debug("ApplicationTests - "+pokemon.getNumber()+" "+pokemon.getName());
            cpt++;
        }
        logger.info("ApplicationTests - *** Repository Size "+cpt);
        assertTrue(cpt!=0);

        logger.info("ApplicationTests - findByName : Name Complete and case sensitive Name");
        String queryString="Bulbasaur";
        logger.info("ApplicationTests - queryString: "+queryString);
        List<Pokemon> pokemons = service.findByName(queryString);
        logger.info("ApplicationTests - *** Display Result size "+pokemons.size());
        assertTrue(pokemons.size()!=0);
        
        logger.info("ApplicationTests - findByNameContainingIgnoreCaseOrderByNumAsc : Partiel Name and no case sensitive");
        queryString="bulba";
        logger.info("ApplicationTests - queryString: "+queryString);
        pokemons = service.findByName(queryString);
        logger.info("ApplicationTests - *** Display Result size "+pokemons.size());
        assertTrue(pokemons.size()!=0);

        logger.info("ApplicationTests - findByNumber");
        queryString="001";
        logger.info("ApplicationTests - queryString: "+queryString);
        Pokemon pokemon = service.findByNumber(queryString);
        logger.info("ApplicationTests - *** Display Result pokemon {}", pokemon);
        assertTrue(pokemons.size()!=0);

        Pokemon pokemon1 = service.findByNumber("131");
        Pokemon pokemon2 = service.findByNumber("149");
        logger.info("ApplicationTests - *** Fight {} vs {}", pokemon1, pokemon2);
        Pokemon pokemonWinner = service.fight(pokemon1, pokemon2);
        logger.info("ApplicationTests - *** Display Result Pokemon Winner {}", pokemonWinner);
        
        
        logger.debug("ApplicationTests - End");
    }

}
