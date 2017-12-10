package com.mehdi.xparser.core;

import com.mehdi.xparser.utils.XUtils;
import com.sun.javaws.jnl.XMLUtils;

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
            if (field.getType() == String.class) {
                field.set(object, childValue);
            } else if (field.getType() == Integer.class) {
                field.set(object, Integer.parseInt(childValue));
            } else if (field.getType() == Long.class) {
                field.set(object, Long.parseLong(childValue));
            } else {
                // TODO: should be fixed to handle all kind of types
                throw new RuntimeException("is not a valid type");
            }
            field.setAccessible(visibility);
        }
    }
}
