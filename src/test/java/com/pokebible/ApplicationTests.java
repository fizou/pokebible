package com.pokebible;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static org.junit.Assert.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {
        
    private final static Logger logger = LoggerFactory.getLogger(ApplicationTests.class);
 
    @Autowired
    private PokemonRepository repository;
   
    @Test
    public void contextLoads() {
        logger.warn("ApplicationTests - Begin");

        logger.warn("ApplicationTests - Insert Bulbizarre as double");
        Pokemon pokemon = new Pokemon("Bulbizarre", Pokemon.TYPE_GRASS, Pokemon.TYPE_POISON);
        //pokemon = null;
        assertNotNull(pokemon);
        
        List<Pokemon> pokemons = new ArrayList<>();
        
        String queryString="Pikachu";
        logger.warn("ApplicationTests - findByName("+queryString+")");
        pokemons = repository.findByName(queryString);
        
/*        logger.warn("ApplicationTests - findAll");
        Iterator<Pokemon> iterator = (Iterator<Pokemon>) repository.findAll();
        while (iterator.hasNext()){
            pokemons.add(iterator.next());
        }
*/        
        logger.warn("ApplicationTests - Display Result");
        for (int i=0;i<pokemons.size();i++) {
            logger.info("ApplicationTests - Pokemon: "+pokemons.toString());
        }    
        assertTrue(pokemons.size()!=0);
        
        logger.warn("ApplicationTests - End");
    }

}
