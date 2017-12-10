package com.mehdi.xparser.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * implies that this field holds a reference to another element
 */
@Typed
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Ref {
    String targetPath() default "";
}
