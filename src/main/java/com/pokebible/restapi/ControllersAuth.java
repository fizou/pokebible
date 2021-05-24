package com.pokebible.restapi;

import com.pokebible.Pokemon;
import com.pokebible.PokemonService;
import com.pokebible.actuator.Metric;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.Api;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
 * Rest Controller: Battle
 * 
 */

@Api(tags = "Auth")
@RestController
public class ControllersAuth {
	
    private static final Logger logger = LoggerFactory.getLogger(ControllersAuth.class);

    private static final UrlPathHelper urlPathHelper = new UrlPathHelper();

    @Autowired
    private PokemonService service;
  
    @Autowired
    private Metric metrics;
        
    // Generate token to access API
    @RequestMapping(
        value="/api/auth/generateToken",
        method = RequestMethod.POST,
        produces = { MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseBody
    public ResponseEntity<ApiResponse> generateToken(@RequestBody ApiCredentials credentials, HttpServletRequest request) {

        //
        // Important this controller method is NOT called : The call to /api/auth/generateToken is intercepted by FilterGenerateToken.class via WebSecurityConfigurerAdapterImpl
        //
        // It is just here to add a description and generate it automatically the Swagger-ui interface 
        //
        logger.info("/api/auth/generateToken - User: {}", service.getLoggedUserName());

        metrics.increment(Metric.Type.API_ACCESS);
        
        logger.debug("/api/auth/generateToken - Credentials: {}", credentials);

        return ResponseEntity.ok(
            new ApiResponse(
                HttpServletResponse.SC_OK,
                "Nothing to return... if you see that, there is an issue.",
                urlPathHelper.getPathWithinApplication((HttpServletRequest) request)
            )
        );        
    }    

    // Test api with token
    @GetMapping(path = "/api/auth/test")
    @ResponseBody
    public ResponseEntity<ApiResponse> test(HttpServletRequest request) {

        logger.info("/api/auth/test - User: {}", service.getLoggedUserName());

        metrics.increment(Metric.Type.API_ACCESS);
        
        //return "{\"message\":\"Your token is valid. Api engine is running and waiting your real requests...\"}";
        
        return ResponseEntity.ok(
            new ApiResponse(
                HttpServletResponse.SC_OK,
                "Your token is valid. Rest Api engine is running and waiting your real requests...",
                urlPathHelper.getPathWithinApplication((HttpServletRequest) request)
            )
        );        
    }
    
    
}
