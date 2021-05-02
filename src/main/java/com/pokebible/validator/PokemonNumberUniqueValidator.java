package com.pokebible.validator;

import com.pokebible.PokemonService;
import com.pokebible.validator.PokemonNumberUniqueConstraint;
import java.util.Collections;
import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of PokemonNumberConstraint interface
 * 
 */
public class PokemonNumberUniqueValidator implements ConstraintValidator<PokemonNumberUniqueConstraint, String> {

    private static final Logger logger = LoggerFactory.getLogger(PokemonNumberUniqueValidator.class);
    
    @Autowired
    private PokemonService service;

    public PokemonNumberUniqueValidator(){
    }
    
    @Override
    public void initialize(PokemonNumberUniqueConstraint constraintAnnotation) {
        // @Autowired on service doesn't work in ConstraintValidator so we need to get the service by an static method to set it
        this.service = PokemonService.serviceReference;
    }
    
    
    @Override
    public boolean isValid(String fieldValue, ConstraintValidatorContext constraintValidatorContext) {
        logger.debug("Try to detect if a pokemon number already exists for fieldValue: "+fieldValue);

        Boolean result=true;
        String message="";
/*
        // Control value Of field
        if (fieldValue == null) {
            logger.debug("isValid - fieldValue is null");
            message="Field is required";
            result=false;
        }
        if (!fieldValue.matches("[0-9]+")) {
            logger.info("isValid - fieldValue doesn't match Regexp");
            message="Only 001, 002, ... format is accepted.";
            result=false;
        }
        if (fieldValue.length() != 3) {
            logger.info("isValid - fieldValue length is not equal to 3");
            message="Only 001, 002, ... format is accepted.";
            result=false;
        }
*/        
        // Get active group here
        Set<Class<?>> activeGroups = null;
        if (constraintValidatorContext instanceof ConstraintValidatorContextImpl) {
            activeGroups = ((ConstraintValidatorContextImpl) constraintValidatorContext).getConstraintDescriptor().getGroups();
        } else {
            activeGroups = Collections.emptySet();
        }

        logger.debug("Group detected (Only 'OnInsert' group needs to control existing value): "+activeGroups);
        
        // Control Num not Already exists in database  
        logger.debug("Service before calling isPokemonNumberExist: "+service);
        if (service.isPokemonNumberExist(fieldValue)) {
            logger.debug("FieldValue already exists");
            message="Number "+fieldValue+" already exists.";
            result=false;
        }
        
        if (result==false&&constraintValidatorContext!=null) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                .buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        }
        
        logger.debug("Result: "+result);
        if (!message.equals("")) logger.info("Message: "+message);
        
        return result;
    }

}
/*
public class PokemonNumValidator implements PokemonNumConstraint<PokemonNumValidator, String> {

    //private PokemonRepository pokemonRepository;

    //public PokemonNumValidator(PokemonRepository pokemonRepository) {
    //    this.pokemonRepository = pokemonRepository;
    //}

    @Override
    public void initialize(PokemonNumConstraint num) {
    }

    @Override
    public boolean isValid(String num, ConstraintValidatorContext cxt) {
    }

}
*/   
