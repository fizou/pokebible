package com.pokebible;

import com.pokebible.actuator.Metric;
import com.pokebible.actuator.Metric.Type;
import java.util.ArrayList;
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
    
    public List<Pokemon.Type> getAllTypes() {

        logger.debug("getAllTypes...");

        List<Pokemon.Type> ListTypes=Pokemon.getTypes();
        
        ListTypes.remove(0); // Remove "NONE"
        
        logger.debug("Result: "+ListTypes.size());
        
        return ListTypes;
    }
    
    
    public Pokemon fight(Pokemon pokemon1, Pokemon pokemon2) {
       
        logger.debug("Battle "+pokemon1.toString()+" vs "+pokemon2.toString());

        double multiplier1 = Pokemon.Type.getEffect(pokemon1, pokemon2).getMultiplier();
        double multiplier2 = Pokemon.Type.getEffect(pokemon2, pokemon1).getMultiplier();
                
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
        logger.debug("Winner "+winner.toString());
        return winner;
       
    }
    
    public List<Pokemon> bestCounters(String number) {

        logger.debug("Find pokemons vunerableTo pokemon number: {}", number);

        metrics.increment(Type.DATABASE_ACCESS);

        List<Pokemon> bestCounters = new ArrayList();
        List<Pokemon> bestCountersStrong = new ArrayList();
        List<Pokemon> bestCountersSupereffective = new ArrayList();

        Pokemon pokemonToEvaluate = findByNumber(number);
        logger.debug("pokemonToEvaluate: {}", pokemonToEvaluate);

        if (pokemonToEvaluate!=null) {
            
            // get Only the best Match Type
            List<Pokemon> pokemons = findAll();
            logger.debug("pokemons size: {}", pokemons.size());
            
            for (int i=0;i<pokemons.size();i++) {
                
                Pokemon.Type.EffectLevel effectLevelAtt = Pokemon.Type.getEffect(pokemons.get(i), pokemonToEvaluate);
                if (effectLevelAtt==Pokemon.Type.EffectLevel.STRONG||effectLevelAtt==Pokemon.Type.EffectLevel.SUPEREFFECTIVE) {
                    Pokemon.Type.EffectLevel effectLevelDef = Pokemon.Type.getEffect(pokemonToEvaluate, pokemons.get(i));
                    if (effectLevelDef!=Pokemon.Type.EffectLevel.STRONG&&effectLevelDef!=Pokemon.Type.EffectLevel.SUPEREFFECTIVE) {
                        if (effectLevelAtt==Pokemon.Type.EffectLevel.SUPEREFFECTIVE) {
                            //logger.debug("add {} to bestCountersSupereffective List", pokemons.get(i));
                            bestCountersSupereffective.add(pokemons.get(i));
                        } else {
                            //logger.debug("add {} to bestCountersStrong List", pokemons.get(i));
                            bestCountersStrong.add(pokemons.get(i));
                        }
                    }

                }
                                
            }
        }
        
        //logger.debug("Result bestCountersSupereffective: {}", bestCountersSupereffective.size());
        //logger.debug("Result bestCountersStrong: {}", bestCountersStrong.size());

        if (bestCountersSupereffective.size()>0) {
            bestCounters=bestCountersSupereffective;
        } else {
            bestCounters=bestCountersStrong;
        }
                
        logger.debug("Result bestCounters: {}", bestCounters.size());

        return bestCounters;
                
    }
    
}
