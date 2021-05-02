package com.pokebible.validator;

/**
 * Interface to create new annotation @PokemonNumberConstraint in front of PokemonNumber in Pokemon.java do do control on it.
 *
 * @author olfize
 */
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = PokemonNumberUniqueValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PokemonNumberUniqueConstraint {

    String message() default "Error on Unique constraint.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}