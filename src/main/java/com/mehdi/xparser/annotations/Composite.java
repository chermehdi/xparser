package com.mehdi.xparser.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * implies that this field is a composite type, and must be parsed or broken down into multiple
 * simpler fields like integers, or strings
 */
@Typed
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Composite {
}
