package com.pokebible.restapi;

import com.pokebible.Pokemon;
import com.pokebible.PokemonService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RepositoryRestController
@RestController
public class RestControllers {
	
    private static final Logger logger = LoggerFactory.getLogger(RestControllers.class);

    @Autowired
    private PokemonService service;
    
    @GetMapping(path = "/api/pokemons/search/testapi")
    @ResponseBody
    public String testApi() {

        logger.info("/api/pokemons/search/testapi - User: {}", service.getLoggedUserName());

        return "<H1>Test</H1><HR>Test page is running...";

    }

    @ApiOperation("generateToken")
    @RequestMapping(
        value="/api/generateToken",
        method = RequestMethod.POST,
        produces = { MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseBody
    public String generateToken(@RequestBody Credentials credentials) {

        //
        // Important this controller method is NOT called : The calls to /api/generateToken are intercepted by FilterGenerateToken.class via WebSecurityConfigurerAdapterImpl
        //
        // It is just here to add description and generate automatically in Swagger-UI interface 
        //
        logger.info("/api/generateToken - User: {}", service.getLoggedUserName());

        logger.debug("/api/generateToken - Credentials: {}", credentials);

        return "Nothing to return... if you see that, there is an issue.";
    }    
    
    @RequestMapping(
        value="/api/pokemons/search/pokemons",
        method = RequestMethod.GET,
        produces = { MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseBody
    public List<Pokemon> findPokemon() {

        logger.info("/api/pokemons/search/pokemons - User: {}", service.getLoggedUserName());
        List<Pokemon> list = service.findPokemon();

        logger.debug("/api/pokemons/search/pokemons - list: {}", list.size());
        
        return list;
    }    
    
}
