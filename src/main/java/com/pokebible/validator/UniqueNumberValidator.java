package com.pokebible.validator;

import com.pokebible.Pokemon;
import com.pokebible.PokemonService;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * Implementation of the interface to create new annotation 
 * @UniqueNumberConstraint 
 * before Pokemon CLASS in Pokemon.java 
 * to do do specific control on unicity of Number 
 *
 */
public class UniqueNumberValidator implements ConstraintValidator<UniqueNumberConstraint, Object> {

    private static final Logger logger = LoggerFactory.getLogger(UniqueNumberValidator.class);

    @Autowired
    private PokemonService service;

    @Override
    public void initialize(final UniqueNumberConstraint constraint) {}

    @Override
    public boolean isValid(final Object o, final ConstraintValidatorContext context) {

        Boolean result=true;
        String message="";
        
        try {
            logger.debug("Object to validate: "+o);
            Pokemon pokemonToCheck = (Pokemon) o;
            logger.debug("pokemon to check: "+pokemonToCheck);
            
            //this.service = PokemonService.serviceReference;
            //logger.debug("Service before calling isPokemonNumberExist: "+service);
            
            Pokemon pokemonInDatabase = service.findByNumber(pokemonToCheck.getNumber());
            if (pokemonInDatabase!=null) {
                if (pokemonInDatabase.getId()!=pokemonToCheck.getId()) {
                    logger.info("pokemon Number "+pokemonToCheck.getNumber()+" in database already exists with different Id: "+pokemonInDatabase.getId());
                    message="Number "+pokemonToCheck.getNumber()+" already exists.";
                    result=false;                    
                }
            }

            if (!result) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message).addPropertyNode("Number").addConstraintViolation();
            }
            
            
        } catch (final Exception e) {
            logger.error("Error: "+e);
            //e.printStackTrace();
        }
        return result;
    }
}