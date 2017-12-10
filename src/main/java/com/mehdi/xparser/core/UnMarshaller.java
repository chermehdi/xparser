package com.mehdi.xparser.core;

import com.mehdi.xparser.annotations.Number;
import com.mehdi.xparser.annotations.Text;
import com.mehdi.xparser.annotations.Typed;
import com.mehdi.xparser.utils.ReflectionUtils;
import com.mehdi.xparser.utils.XUtils;

import java.lang.reflect.Field;
import java.util.Objects;

public class UnMarshaller<T> {

    private Class<T> targetClazz;

    private XNode root;

    public UnMarshaller(XNode root, Class<T> targetClazz) {
        this.root = root;
        this.targetClazz = targetClazz;
    }

    public T unmarshal() {
        Objects.requireNonNull(targetClazz);
        Objects.requireNonNull(root);
        String tagName = XUtils.getTagName(targetClazz);
        Field[] fields = targetClazz.getDeclaredFields();
        try {
            T object = targetClazz.newInstance();
            fillFields(object, fields);
            return object;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void fillFields(T object, Field[] fields) throws IllegalAccessException {
        for (Field field : fields) {
            boolean visibility = field.isAccessible();
            String fieldName = XUtils.getTagNameFromField(field);
            XNode childNode = this.root.getChild(fieldName);
            String childValue = childNode.getValue();
            field.setAccessible(true);
            // TODO: annotations to check the types of variable
            if (ReflectionUtils.isAnnotationPresent(field, Typed.class)) {
                setFieldValue(object, field, childValue);
            } else {
                // assume it's text
                field.set(object, childValue);
            }
            field.setAccessible(visibility);
        }
    }

    private void setFieldValue(T object, Field field, String value) throws IllegalAccessException {
        if (field.isAnnotationPresent(Number.class)) {
            Number numberAnnotation = field.getAnnotation(Number.class);
            if (numberAnnotation.isLong()) {
                field.set(object, Long.parseLong(value));
            } else {
                field.set(object, Integer.parseInt(value));
            }
        }
        if (field.isAnnotationPresent(Text.class)) {
            field.set(object, value);
        }
    }
}
