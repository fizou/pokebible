package com.pokebible;

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

        Pokemon pokemon = new Pokemon("001","Bulbizarre","grass,poison");
        //pokemon = null;
        assertNotNull(pokemon);
        
        List<Pokemon> pokemons = repository.findByName("Bulbizarre");
        for (int i=0;i<pokemons.size();i++) {
            logger.info("ApplicationTests - Pokemon: "+pokemons.get(i).getName()+" - "+pokemons.get(i).getType());
        }    
        assertTrue(pokemons.size()!=0);
        
        logger.warn("ApplicationTests - End");
    }

}
