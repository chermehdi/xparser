package com.mehdi.xparser.core;

import com.mehdi.xparser.annotations.Number;
import com.mehdi.xparser.annotations.Text;
import com.mehdi.xparser.annotations.Typed;
import com.mehdi.xparser.utils.ReflectionUtils;
import com.mehdi.xparser.utils.XUtils;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * generic unmarshaller class for parsing xml documents into simple pojos
 *
 * @param <T> generic parameter of the class
 * @author Mehdi_Maick
 */
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

    /**
     * fill the fields of the object from the XNode element
     *
     * @param object the object to be filled
     * @param fields list of all declared fields of the target class
     * @throws IllegalAccessException
     */
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
                // assumes it's text
                field.set(object, childValue);
            }
            field.setAccessible(visibility);
        }
    }

    /**
     * sets a given field's value, it does it based on the annotation type, if the the field's not
     * annotated with an @Typed, it treats it as simple String value
     *
     * @param object the target object
     * @param field  the current treated field
     * @param value  the current value of the field
     * @throws IllegalAccessException
     */
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
