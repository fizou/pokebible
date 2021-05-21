package com.pokebible.restapi;

import com.pokebible.Pokemon;
import com.pokebible.PokemonService;
import com.pokebible.actuator.PokebibleMetrics;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.Api;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UrlPathHelper;

/**
 *
 * Rest Controller: Rewrite all CRUD to take the hand on debug, metrics, error management, response... 
 *
 * - GET /api/pokemons findAllPokemon
 * - POST /api/pokemons savePokemon
 * - GET /api/pokemons/{id} findByIdPokemon
 * - PUT /api/pokemons/{id} savePokemon
 * - DELETE /api/pokemons/{id} deletePokemon 
 * - PATCH /api/pokemons/{id} savePokemon    
 * 
 * -  /api/generateToken to manage role 
 * -  /api/testApi to test token and access
 * 
 */

@Api(tags = "Pokemon Rest")
//@RepositoryRestController
@RestController
public class RestControllers {
	
    private static final Logger logger = LoggerFactory.getLogger(RestControllers.class);

    private static final UrlPathHelper urlPathHelper = new UrlPathHelper();

    @Autowired
    private PokemonService service;
  
    @Autowired
    private PokebibleMetrics metrics;
    
    // GET /api/pokemons findAllPokemon
    @GetMapping("/api/pokemons") // Omit /api/... has we are in RepositoryRestController
    ResponseEntity<List<Pokemon>> findAll(
            @RequestParam(value = "page", required = false) Optional<Integer> page,
            @RequestParam(value = "size", required = false) Optional<Integer> size,
            @RequestParam(value = "sortField", required = false) Optional<String> sortField,
            @RequestParam(value = "sortDirection", required = false) Optional<String> sortDirection,
            @RequestParam(value = "searchField", required = false) Optional<String> searchField,
            @RequestParam(value = "searchString", required = false) Optional<String> searchString
        ) {

        logger.info("/api/pokemons (GET) - User: {}", service.getLoggedUserName());

        metrics.increment(PokebibleMetrics.Counters.API_ACCESS);

        // Param
        logger.debug("Param - page: {}",  page);
        logger.debug("Param - size: {}",  size);
        logger.debug("Param - sortField: {}",  sortField);
        logger.debug("Param - sortDirection: {}",  sortDirection);
        logger.debug("Param - searchField: {}",  searchField);
        logger.debug("Param - searchString: {}",  searchString);

        // Pagination
        int currentPage = page.orElse(1);
        if (currentPage==0) currentPage=1;
        logger.debug("Calculated - currentPage: {}",  currentPage);
        int pageSize = size.orElse(9);
        if (pageSize==0) pageSize=9; 
        logger.debug("Calculated - pageSize: {}",  pageSize);
        String pageSortField = sortField.orElse("Number");
        if (pageSortField.equals("")) pageSortField="Number";
        logger.debug("Calculated - pageSortField: {}",  pageSortField);
        String pageSortDirection = sortDirection.orElse("asc");
        if (pageSortDirection.equals("")) pageSortDirection="asc";
        logger.debug("Calculated - pageSortDirection: {}",  pageSortDirection);
        String pageSearchField = searchField.orElse("Name");
        if (pageSearchField.equals("")) pageSearchField="Name";
        logger.debug("Calculated - pageSearchField: '{}'",  pageSearchField);
        String pageSearchString = searchString.orElse("");
        logger.debug("Calculated - pageSearchString: '{}'",  pageSearchString);

        List<Pokemon> pokemons;
        if (!page.isPresent()&&!size.isPresent()&&!sortField.isPresent()&&!sortDirection.isPresent()&&!searchField.isPresent()&&!searchString.isPresent()) {
            pokemons = service.findAll();
        } else {
            pokemons = service.findAllPaginated(currentPage, pageSize, pageSortField, pageSortDirection, pageSearchField, pageSearchString).getContent();
        }
 
        logger.debug("/api/pokemons (GET) - Result: {}", pokemons.size());
        
        return ResponseEntity.ok(pokemons);

    }
     
    // Find by id
    @GetMapping("/api/pokemons/{id}")
    ResponseEntity<Pokemon> findById(@PathVariable Long id, HttpServletResponse response) {

        logger.info("/api/pokemons/{} (GET) - User: {}", id, service.getLoggedUserName());

        metrics.increment(PokebibleMetrics.Counters.API_ACCESS);
        
        Pokemon pokemon = service.findById(id);
        if (pokemon==null) {
            //throw new RuntimeException("Id not found: " +id);
            throw new ApiExceptionNotFound("Id "+id+" not found");
        }
 
        logger.debug("/api/pokemons/{} (GET) - Result: {}", id, pokemon);

        return ResponseEntity.ok(pokemon);
        
    }

    // Save new 
    @PostMapping("/api/pokemons")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Pokemon> save(@Validated(com.pokebible.validator.OnInsertGroup.class) @RequestBody Pokemon pokemon) {

        logger.info("/api/pokemons (POST) - Pokemon: {} - User: {}", pokemon, service.getLoggedUserName());

        metrics.increment(PokebibleMetrics.Counters.API_ACCESS);

        pokemon =  service.save(pokemon);
        
        logger.debug("/api/pokemons (POST) - Result: {}", pokemon);

        return ResponseEntity.ok(pokemon);
    }
    
    // Save or update
    @PutMapping("/api/pokemons/{id}")
    ResponseEntity<Pokemon> saveOrUpdate(@Validated(com.pokebible.validator.OnUpdateGroup.class) @RequestBody Pokemon pokemon, @PathVariable Long id) {

        logger.info("/api/pokemons/{} (PUT) - User: {}", id, service.getLoggedUserName());

        metrics.increment(PokebibleMetrics.Counters.API_ACCESS);
        
        // Overide Json id by the one in url
        pokemon.setId(id);
        
        pokemon =  service.save(pokemon);
        
        logger.debug("/api/pokemons/{} (PUT) - Result: {}", id, pokemon);

        return ResponseEntity.ok(pokemon);

        
    }
    
    // Update
    @PatchMapping("/api/pokemons/{id}")
    ResponseEntity<Pokemon> update(@RequestBody Map<String, String> map, @PathVariable Long id, HttpServletResponse response) {

        logger.info("/api/pokemons/{} (PATCH) - User: {}", id, service.getLoggedUserName());

        metrics.increment(PokebibleMetrics.Counters.API_ACCESS);
        
        Pokemon pokemon = service.findById(id);
        if (pokemon==null) {
            //throw new RuntimeException("Id not found: " +id);
            throw new ApiExceptionNotFound("Id "+id+" not found");
        }

        logger.debug("/api/pokemons/{} (PATCH) - Result Find: {}", id, pokemon);

        // Overide Json id by the one in url
        pokemon.setId(id);

        // Overide Only value in Map
        String name = map.get("name");
        if (!StringUtils.isEmpty(name)) pokemon.setName(name);
        String number = map.get("number");
        if (!StringUtils.isEmpty(number)) pokemon.setNumber(number);
        String type1 = map.get("type1");
        if (!StringUtils.isEmpty(type1)) pokemon.setType1(type1);
        String type2 = map.get("type2");
        if (!StringUtils.isEmpty(type2)) pokemon.setType2(type2);

        logger.debug("/api/pokemons/{} (PATCH) - Result Overide: {}", id, pokemon);
        
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Pokemon>> violations = validator.validate(pokemon);
        for (ConstraintViolation<Pokemon> violation : violations) {
            logger.error("/api/pokemons/{} (PATCH) - Error: ", id, violation.getMessage()); 
        }
        
        if (violations.size()==0) {
            pokemon =  service.save(pokemon);
            logger.debug("/api/pokemons/{} (PATCH) - Result update: {}", id, pokemon);
            return ResponseEntity.ok(pokemon);
        } else {
            //throw new RuntimeException("Error: " +violations.iterator().next().getMessage());
            throw new ApiExceptionBadRequest(violations.iterator().next().getMessage());
        }

 
    }
    
    // Delete
    @DeleteMapping("/api/pokemons/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id, HttpServletRequest request) {

        logger.info("/api/pokemons/{} (DELETE) - User: {}", id, service.getLoggedUserName());

        metrics.increment(PokebibleMetrics.Counters.API_ACCESS);
        
        Pokemon pokemon = service.findById(id);
        if (pokemon==null) {
            //throw new RuntimeException("Id not found: " +id);
            throw new ApiExceptionNotFound("Id "+id+" not found");
        }

        service.delete(id);
        
        return ResponseEntity.ok(
            new ApiResponse(
                HttpServletResponse.SC_OK,
                "Delete "+id+" successfully.",
                urlPathHelper.getPathWithinApplication((HttpServletRequest) request)
            )
        );        
    }

    // Test API with token
    @GetMapping(path = "/api/test")
    @ResponseBody
    public ResponseEntity<ApiResponse> testApi(HttpServletRequest request) {

        logger.info("/api/test - User: {}", service.getLoggedUserName());

        metrics.increment(PokebibleMetrics.Counters.API_ACCESS);
        
        //return "{\"message\":\"Your token is valid. Api engine is running and waiting your real requests...\"}";
        
        return ResponseEntity.ok(
            new ApiResponse(
                HttpServletResponse.SC_OK,
                "Your token is valid. Rest Api engine is running and waiting your real requests...",
                urlPathHelper.getPathWithinApplication((HttpServletRequest) request)
            )
        );        
    }
    
    // Generate token to access API
    @RequestMapping(
        value="/api/generateToken",
        method = RequestMethod.POST,
        produces = { MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseBody
    public ResponseEntity<ApiResponse> generateToken(@RequestBody ApiCredentials credentials, HttpServletRequest request) {

        //
        // Important this controller method is NOT called : The call to /api/generateToken is intercepted by FilterGenerateToken.class via WebSecurityConfigurerAdapterImpl
        //
        // It is just here to add a description and generate it automatically the Swagger-ui interface 
        //
        logger.info("/api/generateToken - User: {}", service.getLoggedUserName());

        metrics.increment(PokebibleMetrics.Counters.API_ACCESS);
        
        logger.debug("/api/generateToken - Credentials: {}", credentials);

        return ResponseEntity.ok(
            new ApiResponse(
                HttpServletResponse.SC_OK,
                "Nothing to return... if you see that, there is an issue.",
                urlPathHelper.getPathWithinApplication((HttpServletRequest) request)
            )
        );        
    }    
    
}
