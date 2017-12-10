package com.mehdi.xparser.utils;

import com.mehdi.xparser.annotations.Typed;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.stream.Stream;

public class ReflectionUtils {

    private ReflectionUtils() {
        throw new UnsupportedOperationException("could not instantiate XUtils class");
    }

    public static boolean isAnnotationPresent(Field obj, Class<? extends Annotation> target) {
        Annotation[] annotations = obj.getAnnotations();
        for (Annotation annotation : annotations) {
            if(annotation.annotationType() == target) return true;
            if (annotation.annotationType().isAnnotationPresent(target)) return true;
        }
        return false;
    }
}
