package com.pokebible.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * Interface to create new annotation 
 * @NotMatch(field = "type1") 
 * before Type2 property in Pokemon.java  
 * to do do specific control on the fact that a field NOT match another field (In our case type2 muste NOT match type1)
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotMatch {

    String field();

    String message() default "";
}