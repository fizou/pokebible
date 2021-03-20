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
        logger.debug("ApplicationTests - Begin");

        logger.info("ApplicationTests - Pokedex ("+repository.count()+")");
        assertTrue(repository.count()!=0);

        logger.info("ApplicationTests - Insert Bulbizarre as double");
        //Pokemon pokemon = new Pokemon("Bulbizarre", Pokemon.TYPE_GRASS, Pokemon.TYPE_POISON);
    	this.repository.save(new Pokemon("001","Bulbasaur",Pokemon.TYPE_GRASS,Pokemon.TYPE_POISON,"Bulbizarre"));
        //pokemon = null;

        logger.info("ApplicationTests - Pokedex after insert("+repository.count()+")");
        assertTrue(repository.count()!=0);

        logger.info("ApplicationTests - Read All Pokedex");
        
        /*
        
        int cpt=0;
        Iterator<Pokemon> iterator = (Iterator<Pokemon>) this.repository.findAll();
        while (iterator.hasNext()){
            logger.debug("ApplicationTests - "+iterator.next().getNum()+" "+iterator.next().getName());
            cpt++;
        }
        logger.info("ApplicationTests - Display Result size "+cpt);
        assertTrue(cpt!=0);


        String queryString="Pikachu";
        logger.info("ApplicationTests - findByName("+queryString+")");
        List<Pokemon> pokemons = repository.findByName(queryString);
        
        logger.info("ApplicationTests - Display Result size "+pokemons.size());
        assertTrue(pokemons.size()!=0);

        */

        logger.debug("ApplicationTests - End");
    }

}
