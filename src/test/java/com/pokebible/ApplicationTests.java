package com.pokebible;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
public class ApplicationTests {
        
    private final static Logger logger = LoggerFactory.getLogger(ApplicationTests.class);
 
    @Autowired
    private PokemonRepository repository;
   
    @Test
    public void contextLoads() {
        logger.debug("ApplicationTests - Begin");

        logger.info("ApplicationTests - Repository Size ("+repository.count()+")");
        assertTrue(repository.count()!=0);

        logger.info("ApplicationTests - Insert Missing No");
    	this.repository.save(new Pokemon("000","Missing No",Pokemon.Type.NORMAL,Pokemon.Type.NORMAL,"Sans Numero"));
        logger.info("ApplicationTests - *** Repository Size after insert("+repository.count()+")");
        assertTrue(repository.count()!=0);

        logger.info("ApplicationTests - Read All Repository");
        int cpt=0;
        Iterator<Pokemon> iterator = repository.findAll().iterator();
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
        List<Pokemon> pokemons = repository.findByName(queryString);
        logger.info("ApplicationTests - *** Display Result size "+pokemons.size());
        assertTrue(pokemons.size()!=0);
        
        logger.info("ApplicationTests - findByNameContainingIgnoreCaseOrderByNumAsc : Partiel Name and no case sensitive");
        queryString="bulba";
        logger.info("ApplicationTests - queryString: "+queryString);
        pokemons = repository.findByNameContainingIgnoreCaseOrderByNumberAsc(queryString);
        logger.info("ApplicationTests - *** Display Result size "+pokemons.size());
        assertTrue(pokemons.size()!=0);

        logger.info("ApplicationTests - findByNumber");
        queryString="001";
        logger.info("ApplicationTests - queryString: "+queryString);
        pokemons = repository.findByNumber(queryString);
        logger.info("ApplicationTests - *** Number of pokemon {}", pokemons.size());
        assertTrue(pokemons.size()!=0);

        logger.debug("ApplicationTests - End");
    }

}
