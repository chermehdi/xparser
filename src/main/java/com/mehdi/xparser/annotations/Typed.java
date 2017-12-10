package com.mehdi.xparser.annotations;

import java.lang.annotation.*;

/**
 * a common annotation to all parser known types
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@Inherited
public @interface Typed {
}
