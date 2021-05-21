package com.pokebible.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *
 * Interface to create new annotation @PokemonNumberConstraint before PokemonNumber property in Pokemon.java do do specific control on it.
 *
 */
@Documented
@Constraint(validatedBy = PokemonNumberUniqueValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PokemonNumberUniqueConstraint {

    String message() default "Error on Unique constraint.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}