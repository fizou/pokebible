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
        logger.info("Begin Tests");

        logger.info("Read database");
        logger.info("-> Database size: "+service.count()+"");
        assertTrue(service.count()!=0);

        Pokemon pokemon = new Pokemon("000", "Missing No", Pokemon.Type.NORMAL, Pokemon.Type.NONE, "Sans Numero");
        logger.info("Save('"+pokemon+"')");
    	service.save(pokemon);
        logger.info("-> Database size after insert: "+service.count()+"");
        assertTrue(service.count()!=0);

        logger.info("Read all rows");
        int cpt=0;
        Iterator<Pokemon> iterator = service.findAll().iterator();
        while (iterator.hasNext()){
            pokemon = iterator.next();
            logger.info("-> "+pokemon.getNumber()+" "+pokemon.getName());
            cpt++;
        }
        logger.info("-> Database size: "+cpt);
        assertTrue(cpt!=0);

        String queryString="Bulbasaur";
        logger.info("findByName('"+queryString+"') with complete Name");
        List<Pokemon> pokemons = service.findByName(queryString);
        logger.info("-> Result size: "+pokemons.size());
        assertTrue(pokemons.size()!=0);
/*
        queryString="bulba";
        logger.info("findByName('"+queryString+"') with Partiel name");
        pokemons = service.findByName(queryString);
        logger.info("-> Result size: "+pokemons.size());
        assertTrue(pokemons.size()!=0);
*/
        queryString="001";
        logger.info("findByNumber('"+queryString+"')");
        pokemon = service.findByNumber(queryString);
        logger.info("-> Result pokemon: {}", pokemon);
        assertTrue(pokemons.size()!=0);

        Pokemon pokemon1 = service.findByNumber("131");
        Pokemon pokemon2 = service.findByNumber("149");
        logger.info("Fight {} vs {}", pokemon1, pokemon2);
        Pokemon pokemonWinner = service.fight(pokemon1, pokemon2);
        logger.info("-> Result Winner: {}", pokemonWinner);
                
        logger.info("End Tests");
    }

}
