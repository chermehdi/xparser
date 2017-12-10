package com.mehdi.xparser.core;

import com.mehdi.xparser.annotations.Composite;
import com.mehdi.xparser.annotations.RootElement;
import com.mehdi.xparser.annotations.Typed;
import com.mehdi.xparser.utils.ReflectionUtils;
import com.mehdi.xparser.utils.XUtils;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;

public class Marshaller<T> {

    private Writer writer;

    private T object;

    private int indent = 0;

    public Marshaller(T obj, Writer writer) {
        this.object = obj;
        this.writer = writer;
    }


    public void marshall() {
        try {
            setUpXmlDoc();
            if (object.getClass().isAnnotationPresent(RootElement.class)) {
                writeFields(object, object.getClass().getDeclaredFields());
            } else {

            }
            closeUpXmlDoc();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeFields(T object, Field[] declaredFields) throws IOException {
        for (Field field : declaredFields) {
            if (ReflectionUtils.isAnnotationPresent(field, Typed.class)) {
                writeTypedField(field, object);
            } else {
                writeSimpleField(field, object);
            }
        }
    }

    private void writeTypedField(Field field, T object) throws IOException {
        if (ReflectionUtils.isAnnotationPresent(field, Composite.class)) {
        } else {
            writeSimpleField(field, object);
        }
    }

    private void writeSimpleField(Field field, T object) throws IOException {

        openTag(field);
        // TODO: if the object has attributes add them
        writeValue(field, object);
        closeTag(field);
    }

    private void closeTag(Field field) throws IOException {
        String fieldTagName = "</" + XUtils.getTagNameFromField(field) + ">";
        writer.write(fieldTagName + "\n");
        indent -= 2;
    }

    private void writeValue(Field field, T object) throws IOException {
        try {
            boolean visibilty = field.isAccessible();
            field.setAccessible(true);
            String value = field.get(object).toString();
            writer.write(value);
            field.setAccessible(visibilty);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void doIndent() throws IOException {
        writer.write(getIndent());
    }

    private void openTag(Field field) throws IOException {
        indent += 2;
        String tagName = XUtils.getTagNameFromField(field);
        doIndent();
        writer.write("<" + tagName + ">");
    }

    private void setUpXmlDoc() throws IOException {
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        String objectTagName = "<" + XUtils.getTagName(object.getClass()) + ">\n";
        indent += 2;
        writer.write(objectTagName);
    }

    private void closeUpXmlDoc() throws IOException {
        indent -= 2;
        String objectTagName = "</" + XUtils.getTagName(object.getClass()) + ">";
        writer.write(objectTagName);
    }

    private String getIndent() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) sb.append(' ');
        return sb.toString();
    }
}
