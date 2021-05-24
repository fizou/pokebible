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
 * @UniqueNumberConstraint 
 * before Pokemon CLASS in Pokemon.java 
 * to do do specific control on unicity of Number 
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueNumberValidator.class)
@Documented
public @interface UniqueNumberConstraint {

    String message() default "Number must be unique!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}