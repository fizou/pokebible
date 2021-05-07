package com.pokebible;

import com.pokebible.actuator.PokebibleMetrics;
import com.pokebible.actuator.PokebibleMetrics.Counters;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class PokemonService {

    /**
     *
     * Service Class with all necessary common Method 
     *
     */
    private static final Logger logger = LoggerFactory.getLogger(PokemonService.class);

    @Autowired
    private PokemonRepository repository;

    @Autowired
    private PokebibleMetrics metrics;

   public static PokemonService serviceReference; // reference that can be used by Java Class which cannot autowired (Try also...)
         
    @PostConstruct
    private void init() {
        serviceReference=this;
    }
    
    // Return The User Name of logged user
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

    // Return The User Name of logged user
    public Boolean isPokemonNumberExist(String pokemonNumber) {

        logger.debug("Is there a Pokemon existing with this number: "+pokemonNumber);

        List<Pokemon> pokemons = repository.findByNumber(pokemonNumber);
        logger.debug("Number of pokemon founded: "+pokemons.size());
        boolean result = false;
        if (!pokemons.isEmpty()) {
            result = true;
        }
        logger.debug("Result: "+result);

        metrics.increment(Counters.DATABASE_ACCESS);

        return result;
    }
    
    //
    // Get Pokemon List 
    //
    public List<Pokemon> findPokemon() {

        logger.debug("Find all Pokemons");

        metrics.increment(Counters.DATABASE_ACCESS);

        return repository.findAllByOrderByNumberAsc();
    }

    public Page<Pokemon> findPokemonPaginated(int pageNumber, int pageSize, String pageSortField, String pageSortDirection, String pageFilter) {
        
        logger.info("Searching pokemon to display for the current page...");

        logger.debug("Criteria - pageNumber: "+pageNumber);
        logger.debug("Criteria - pageSize: "+pageSize);
        logger.debug("Criteria - pageSortField: "+pageSortField);
        logger.debug("Criteria - pageSortDirection: "+pageSortDirection);
        logger.debug("Criteria - filter: "+pageFilter);

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
        if (pageFilter==null||pageFilter.equals("")) {
            pokemonPage = repository.findAll(pageable);
        } else {
            pokemonPage = repository.findByNameContainingIgnoreCase(pageFilter, pageable);
        }
        
        logger.debug("Result - getNumberOfElements: {}",  pokemonPage.getNumberOfElements());
        logger.debug("Result - getTotalElements: {}",  pokemonPage.getTotalElements());
        logger.debug("Result - getTotalPages: {}",  pokemonPage.getTotalPages());
        
        metrics.increment(Counters.DATABASE_ACCESS);

        return pokemonPage;
    }

    
    
}
