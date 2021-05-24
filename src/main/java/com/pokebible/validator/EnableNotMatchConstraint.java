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
 * Interface to create new annotation 
 * @EnableNotMatchConstraint 
 * before Pokemon CLASS in Pokemon.java 
 * to do do specific control on the fact that a field NOT match another field (In our case type2 muste NOT match type1)
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotMatchValidator.class)
@Documented
public @interface EnableNotMatchConstraint {

    String message() default "Fields must match!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}