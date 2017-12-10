package com.mehdi.xparser.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * tells the parser that the element annotated with @RootElement has a corresponding tag in the xml document
 * that the parser is trying to handle
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RootElement {

    String value() default "";
}
