package com.pokebible.validator;

import java.lang.reflect.Field;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Implementation of the interface to create new annotation 
 * @EnableNotMatchConstraint 
 * before Pokemon CLASS in Pokemon.java 
 * to do do specific control on the fact that a field NOT match another field (In our case type2 muste NOT match type1)
 *
 */
public class NotMatchValidator implements ConstraintValidator<EnableNotMatchConstraint, Object> {

    private static final Logger logger = LoggerFactory.getLogger(NotMatchValidator.class);

    @Override
    public void initialize(final EnableNotMatchConstraint constraint) {}

    @Override
    public boolean isValid(final Object o, final ConstraintValidatorContext context) {
        boolean result = true;
        try {
            String mainField, secondField, message;
            Object firstObj, secondObj;

            final Class<?> clazz = o.getClass();
            final Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(NotMatch.class)) {
                    mainField = field.getName();
                    secondField = field.getAnnotation(NotMatch.class).field();
                    message = field.getAnnotation(NotMatch.class).message();

                    if (message == null || "".equals(message))
                        message = "Fields " + mainField + " and " + secondField + " must not match.";

                    firstObj = BeanUtils.getProperty(o, mainField);
                    secondObj = BeanUtils.getProperty(o, secondField);

                    result = firstObj == null && secondObj == null || firstObj != null && !firstObj.equals(secondObj);
                    if (!result) {
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate(message).addPropertyNode(mainField).addConstraintViolation();
                        break;
                    }
                }
            }
        } catch (final Exception e) {
            logger.error("Error: "+e);
            //e.printStackTrace();
        }
        return result;
    }
}