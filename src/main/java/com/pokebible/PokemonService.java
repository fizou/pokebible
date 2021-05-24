package com.pokebible;

import com.pokebible.actuator.Metric;
import com.pokebible.actuator.Metric.Type;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * Service Class with all necessary common Method called by web or rest controllers.
 *
 */

@Service
public class PokemonService {

    private static final Logger logger = LoggerFactory.getLogger(PokemonService.class);

    @Autowired
    private PokemonRepository repository;

    @Autowired
    private Metric metrics;

   //public static PokemonService serviceReference; // reference that can be used by Java Class which cannot autowired (Try also...)
    //@PostConstruct
    //private void init() {
    //    serviceReference=this;
    //}
    
    //
    // CRUD Operation Find All, Find by Id, Save, Delete 
    //
    public List<Pokemon> findAll() {

        logger.debug("Find all Pokemons order by Number Asc");

        metrics.increment(Type.DATABASE_ACCESS);

        return repository.findAllByOrderByNumberAsc();
    }

    public Page<Pokemon> findAllPaginated(int pageNumber, int pageSize, String pageSortField, String pageSortDirection, String pageSearchField, String pageSearchString) {
        
        logger.debug("Find all Pokemons with pagination");

        metrics.increment(Type.DATABASE_ACCESS);

        logger.debug("Criteria - pageNumber: "+pageNumber);
        logger.debug("Criteria - pageSize: "+pageSize);
        logger.debug("Criteria - pageSortField: "+pageSortField);
        logger.debug("Criteria - pageSortDirection: "+pageSortDirection);
        logger.debug("Criteria - pageSearchString: "+pageSearchString);
        logger.debug("Criteria - pageSearchField: "+pageSearchField);

        // Contruct sort and pageable object with control on values
        Sort sort=null;
        if (pageSortField!=null&&!pageSortField.equals("")) {
            if (pageSortDirection==null||pageSortDirection.equals("desc")) {
                sort = Sort.by(pageSortField).descending();
            } else {
                sort = Sort.by(pageSortField).ascending();
            }
        }
        Pageable pageable=null;
        if (sort!=null) {
            pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        } else {
            pageable = PageRequest.of(pageNumber - 1, pageSize);
        }
        
        // Read with Filter 
        logger.debug("Reading database with Criteria...");
        Page<Pokemon> pokemonPage;
        if (pageSearchField==null||pageSearchField.equals("")||pageSearchString==null||pageSearchString.equals("")) {
            pokemonPage = repository.findAll(pageable);
        } else {
            if (pageSearchField.equalsIgnoreCase("Number")) {
                pokemonPage = repository.findByNumberEqualsIgnoreCase(pageSearchString, pageable);
            } else if (pageSearchField.equalsIgnoreCase("Type1")) {
                pokemonPage = repository.findByType1ContainingIgnoreCase(pageSearchString, pageable);
            } else if (pageSearchField.equalsIgnoreCase("Type2")) {
                pokemonPage = repository.findByType2ContainingIgnoreCase(pageSearchString, pageable);
            } else if (pageSearchField.equalsIgnoreCase("Type")) {
                // Pageable+sort is not authorised on custom query: So get all types corresponding to the filter, do manual sort and set pageable manually
                List<Pokemon> pokemons = repository.findByTypeContainingIgnoreCaseOrderByNumberAsc(pageSearchString); 
                if (sort.isSorted()) {
                    for (Order order : sort) {
                        if (order.getProperty().equals("Number")) if (order.isAscending()) pokemons.sort(java.util.Comparator.comparing(Pokemon::getNumber)); else pokemons.sort(java.util.Comparator.comparing(Pokemon::getNumber).reversed());
                        if (order.getProperty().equals("Name")) if (order.isAscending()) pokemons.sort(java.util.Comparator.comparing(Pokemon::getName)); else pokemons.sort(java.util.Comparator.comparing(Pokemon::getName).reversed());
                        if (order.getProperty().equals("Type1")) if (order.isAscending()) pokemons.sort(java.util.Comparator.comparing(Pokemon::getType1)); else pokemons.sort(java.util.Comparator.comparing(Pokemon::getType1).reversed());
                        if (order.getProperty().equals("Type2")) if (order.isAscending()) pokemons.sort(java.util.Comparator.comparing(Pokemon::getType2)); else pokemons.sort(java.util.Comparator.comparing(Pokemon::getType2).reversed());
                    }
                }
                pokemonPage = Pokemon.toPage(pokemons, pageable);
            } else {
                pokemonPage = repository.findByNameContainingIgnoreCase(pageSearchString, pageable);
            }
        }
        
        logger.debug("Result - getNumberOfElements: {}",  pokemonPage.getNumberOfElements());
        logger.debug("Result - getTotalElements: {}",  pokemonPage.getTotalElements());
        logger.debug("Result - getTotalPages: {}",  pokemonPage.getTotalPages());
        
        return pokemonPage;
    }
    
    public Pokemon findById(Long id) {

        logger.debug("Find Pokemon by id: {}", id);

        metrics.increment(Type.DATABASE_ACCESS);

        Optional<Pokemon> pokemon = repository.findById(id);
        if (pokemon.isPresent()) {
            return pokemon.get(); 
        } else {
            return null;
            //Throw -> new PokemonNotFoundException(id));
        } 
                
    }

    public List<Pokemon> findByName(String name) {

        logger.debug("Find Pokemon by name: {}", name);

        metrics.increment(Type.DATABASE_ACCESS);

        List<Pokemon> pokemons = repository.findByNameContainingIgnoreCaseOrderByNumberAsc(name);
        
        logger.debug("Result: {}", pokemons.size());

        return pokemons;
                
    }

    public Pokemon findByNumber(String number) {

        logger.debug("Find Pokemon by number: {}", number);

        metrics.increment(Type.DATABASE_ACCESS);

        List<Pokemon> pokemons = repository.findByNumberEqualsIgnoreCaseOrderByNumberAsc(number);
        
        logger.debug("Result: {}", pokemons.size());
        
        if (pokemons.size()>0) {
            return pokemons.get(0);
        } else {
            return null;
        }
                
    }

    public long count() {

        long count=repository.count();
        
        metrics.increment(Type.DATABASE_ACCESS);

        logger.debug("Count Pokemon: {}",count);
        
        return count;
                
    }
    
    public Pokemon save(Pokemon pokemon) {

        logger.debug("Save Pokemon: {}", pokemon);

        metrics.increment(Type.DATABASE_ACCESS);

        return repository.save(pokemon);
    }
    
    public void delete(Long id) {

        logger.debug("Delete Pokemon: {}", id);

        metrics.increment(Type.DATABASE_ACCESS);

        repository.deleteById(id);
    }
    
    //
    // Return true if PokemonNumber Exist
    //
    public Boolean isPokemonNumberExist(String pokemonNumber) {

        logger.debug("Is there a Pokemon existing with this number: "+pokemonNumber);

        List<Pokemon> pokemons = repository.findByNumberEqualsIgnoreCaseOrderByNumberAsc(pokemonNumber);
        logger.debug("Number of pokemon founded: "+pokemons.size());
        boolean result = false;
        if (!pokemons.isEmpty()) {
            result = true;
        }
        logger.debug("Result: "+result);

        metrics.increment(Type.DATABASE_ACCESS);

        return result;
    }

    //
    // Return The User Name of logged user
    //
    public String getLoggedUserName() {

        logger.debug("Searching Logged UserName...");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String userName = "";
        if (auth != null) {
            userName=auth.getName();
	} else {
            userName="Anonymous";
	}
        
        logger.debug("Result: "+userName);
        
        return userName;
    }
    
    public Pokemon fight(Pokemon pokemon1, Pokemon pokemon2) {
       
        //Rule 1: pokemon1 is always the winner
        //Pokemon pokemon = pokemon1;
        //return pokemon;

        //Rules 2: 
        //    - We are taking the type1 of pokemon1 for attack
        //    - We are checking multiplier for this attack type against type1 of pokemon2
        //    - We are modify the multiplier for this attack type against type2 of pokemon2 (If pokemon2 has one)
        //
        //    - THEN we are taking the type2 of pokemon1 for attack and keep the best multiplier (type1 or type2)
        //
        //    - We do the same for pokemon2 as attacker
        //    - We compare multiplier of pokemon 1 and pokemon 2
        //    - We Declare winner (if tied, same multiplier, randomize winner)
        //
        logger.info("Battle "+pokemon1.toString()+" vs "+pokemon2.toString());
        
        double multiplier1 = getMultiplierFromTableEffect(pokemon1.getType1(),pokemon2.getType1());
        logger.debug("multiplierA1 ("+pokemon1.getType1()+" vs "+pokemon2.getType1()+"): "+multiplier1);
        multiplier1 = multiplier1 * getMultiplierFromTableEffect(pokemon1.getType1(),pokemon2.getType2());
        logger.debug("multiplierA2 ("+pokemon1.getType1()+" vs "+pokemon2.getType2()+"): "+multiplier1);
        if (!pokemon1.getType2().equals("NONE")) {
            double multiplier = getMultiplierFromTableEffect(pokemon1.getType2(),pokemon2.getType1());
            logger.debug("multiplierA3 ("+pokemon1.getType2()+" vs "+pokemon2.getType1()+"): "+multiplier);
            multiplier = multiplier * getMultiplierFromTableEffect(pokemon1.getType2(),pokemon2.getType2());
            logger.debug("multiplierA4 ("+pokemon1.getType2()+" vs "+pokemon2.getType2()+"): "+multiplier);
            if (multiplier>multiplier1) multiplier1=multiplier;
        }
        
        double multiplier2 = getMultiplierFromTableEffect(pokemon2.getType1(),pokemon1.getType1());
        logger.debug("multiplierB1 ("+pokemon2.getType1()+" vs "+pokemon1.getType1()+"): "+multiplier2);
        multiplier2 = multiplier2 * getMultiplierFromTableEffect(pokemon2.getType1(),pokemon1.getType2());
        logger.debug("multiplierB2 ("+pokemon2.getType1()+" vs "+pokemon1.getType2()+"): "+multiplier2);
        if (!pokemon2.getType2().equals("NONE")) {
            double multiplier = getMultiplierFromTableEffect(pokemon2.getType2(),pokemon1.getType1());
            logger.debug("multiplierB3 ("+pokemon2.getType2()+" vs "+pokemon1.getType1()+"): "+multiplier);
            multiplier = multiplier * getMultiplierFromTableEffect(pokemon2.getType2(),pokemon1.getType2());
            logger.debug("multiplierB4 ("+pokemon2.getType2()+" vs "+pokemon1.getType2()+"): "+multiplier);
            if (multiplier>multiplier2) multiplier2=multiplier;
        }

        logger.debug("multiplier1 ("+pokemon1.getType()+" vs "+pokemon2.getType()+"): "+multiplier1);
        logger.debug("multiplier2 ("+pokemon2.getType()+" vs "+pokemon1.getType()+"): "+multiplier2);
        Pokemon winner;
        if (multiplier1>multiplier2) {
            winner=pokemon1;
        } else if (multiplier1<multiplier2) {
            winner=pokemon2;
        } else { // multiplier1==multiplier2
            int randomWinnerNumber = (int)Math.floor(Math.random()*(2)+1);
            logger.debug("Random winner: "+randomWinnerNumber);
            if (randomWinnerNumber==1){
                winner=pokemon1;
            } else {
                winner=pokemon2;
            }
        }
        logger.info("Winner "+winner.toString());
        return winner;
       
    }
    
    public double getMultiplierFromTableEffect(String attackType, String defenderType) {
        EffectType attackTypeEnum = EffectType.valueOf(attackType);
        if (defenderType.equals("NONE")) return 1;
        EffectType defenderTypeEnum = EffectType.valueOf(defenderType);
        
        return tableEffect[defenderTypeEnum.ordinal()][attackTypeEnum.ordinal()].getEffect();
    }

    // taken and adpat from str
    private enum EffectType {
        NORMAL, FIGHTING, FLYING, POISON, GROUND, ROCK, BUG, GHOST, STEEL, FIRE, WATER, GRASS, ELECTRIC, PSYCHIC, ICE, DRAGON, DARK, FAIRY
    }
    
    // Contains the values for what type advantage will deal in extra damage 
    public enum EffectLevel {
        INEFFECTIVE(0.0), WEAK(0.5), NORMAL(1.0), STRONG(2.0);

        private double value;

        EffectLevel(final double newValue) {
            value = newValue;
        }

        public double getEffect() {
            return value;
        }
    }
    
    private static final EffectLevel norm = EffectLevel.NORMAL;
    private static final EffectLevel weak = EffectLevel.WEAK;
    private static final EffectLevel str  = EffectLevel.STRONG;
    private static final EffectLevel inef = EffectLevel.INEFFECTIVE;
    private static final EffectLevel tableEffect[][] =
    {        //                                                 ATT
             // norm  fght  fly   pois  grnd  rock  bug   ghst  stel  fire  wter  grss  elec  psyc  ice   drag  dark  fair
    /* norm */ {norm, str , norm, norm, norm, norm, norm, inef, norm, norm, norm, norm, norm, norm, norm, norm, norm, norm}, /* norm */
    /* fght */ {norm, norm, str , norm, norm, norm, norm, inef, norm, norm, norm, norm, norm, norm, norm, norm, norm, str }, /* fght */
    /* fly  */ {norm, weak, norm, norm, inef, str , weak, norm, norm, norm, norm, weak, str , norm, str , norm, norm, norm}, /* fly  */
    /* pois */ {norm, weak, norm, weak, str , norm, weak, norm, norm, norm, norm, weak, norm, str , norm, norm, norm, weak}, /* pois */
    /* grnd */ {norm, norm, norm, weak, norm, weak, norm, norm, norm, norm, str , str , inef, norm, str , norm, norm, norm}, /* grnd */
    /* rock */ {weak, str , weak, weak, str , norm, norm, norm, norm, norm, str , str , norm, norm, norm, norm, norm, norm}, /* rock */
    /* bug  */ {norm, weak, str , norm, weak, str , norm, norm, norm, str , norm, weak, norm, norm, norm, norm, norm, norm}, /* bug  */
    /* ghst */ {inef, inef, norm, weak, norm, norm, weak, norm, norm, norm, norm, norm, norm, norm, norm, norm, str , norm}, /* ghst */
/* DEF stel */ {weak, str , weak, inef, str , weak, weak, weak, weak, str , norm, weak, norm, weak, weak, weak, weak, norm}, /* stel DEF */
    /* fire */ {norm, norm, norm, norm, str , str , weak, norm, weak, weak, str , weak, norm, norm, weak, norm, norm, weak}, /* fire */
    /* wter */ {norm, norm, norm, norm, norm, norm, norm, norm, weak, weak, weak, str , str , norm, weak, norm, norm, norm}, /* wter */
    /* grss */ {norm, norm, str , str , weak, norm, str , norm, norm, str , weak, weak, weak, norm, str , norm, norm, norm}, /* grss */
    /* elec */ {norm, norm, weak, norm, str , norm, norm, norm, weak, norm, norm, norm, weak, norm, norm, norm, norm, norm}, /* elec */
    /* psyc */ {norm, weak, norm, norm, norm, norm, str , str , norm, norm, norm, norm, norm, weak, norm, norm, str , norm}, /* psyc */
    /* ice  */ {norm, str , norm, norm, norm, str , norm, norm, str , str , norm, norm, norm, norm, weak, norm, norm, norm}, /* ice  */
    /* drag */ {norm, norm, norm, norm, norm, norm, norm, norm, norm, weak, weak, weak, weak, norm, str , str , norm, str }, /* drag */
    /* dark */ {norm, str , norm, norm, norm, str , weak, norm, norm, norm, norm, norm, norm, inef, norm, norm, weak, str }, /* dark */
    /* fair */ {norm, weak, norm, str,  norm, norm, weak, norm, str , norm, norm, norm, norm, norm, norm, inef, norm, weak}  /* fair */
             // norm  fght  fly   pois  grnd  rock  bug   ghst  stel  fire  wter  grss  elec  psyc  ice   drag  dark  fair
    };       //                                                 ATT
    
    
}
