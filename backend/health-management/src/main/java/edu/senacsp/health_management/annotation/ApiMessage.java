package edu.senacsp.health_management.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows specifying the endpoint's success message via the @ApiMessage annotation,
 * which works together with SuccessResponseWrapper to automatically wrap the response body
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiMessage {
    String value();
}