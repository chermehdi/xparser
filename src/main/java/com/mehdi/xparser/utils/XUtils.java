package com.mehdi.xparser.utils;

import java.lang.reflect.Field;
import java.util.Objects;

public class XUtils {

    private XUtils() {
        throw new UnsupportedOperationException("could not instantiate XUtils class");
    }

    /**
     * this method returns the expected tag name of a clazz object, java bean naming conventions are
     * expected in the class's simple name
     * TODO: add handling of cases such as "XMLNode" {starts with two or more caps}
     */
    public static String getTagName(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        String className = clazz.getSimpleName();
        return getTagNameBeanConvention(className).toString();
    }

    public static String getTagNameFromField(Field field) {
        Objects.requireNonNull(field);
        String fieldName = field.getName();
        return getTagNameBeanConvention(fieldName).toString();
    }

    private static StringBuilder getTagNameBeanConvention(String fieldName) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fieldName.length(); i++) {
            if (Character.isUpperCase(fieldName.charAt(i))) {
                if (i > 0) {
                    sb.append('-');
                }
            }
            sb.append(Character.toLowerCase(fieldName.charAt(i)));
        }
        return sb;
    }
}
