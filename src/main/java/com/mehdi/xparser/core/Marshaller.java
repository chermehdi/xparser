package com.mehdi.xparser.core;

import com.mehdi.xparser.annotations.Composite;
import com.mehdi.xparser.annotations.RootElement;
import com.mehdi.xparser.annotations.Typed;
import com.mehdi.xparser.utils.ReflectionUtils;
import com.mehdi.xparser.utils.XUtils;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;

/**
 * generic marshaller class to persist simple pojos to valid Xml format
 *
 * @param <T> generique parameter of the class
 * @author Mehdi_Maick
 */
public class Marshaller<T> {

    private Writer writer;

    private T object;

    private int indent = 0;

    private int INDENT_SIZE = 2;

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
                // TODO: must define a behavior for non-root elements
            }
            closeUpXmlDoc();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * write all the fields to the writer object, fields are of two types:
     * Typed fields annotated with @Typed
     * or simple Fields that are treated as Simple Strings
     * <p>
     * throws IOException and IllegalAccessException
     */
    private void writeFields(T object, Field[] declaredFields) throws IOException, IllegalAccessException {
        for (Field field : declaredFields) {
            if (ReflectionUtils.isAnnotationPresent(field, Typed.class)) {
                writeTypedField(field, object);
            } else {
                writeSimpleField(field, object);
            }
        }
    }

    private void writeTypedField(Field field, Object object) throws IOException, IllegalAccessException {
        if (ReflectionUtils.isAnnotationPresent(field, Composite.class)) {
            Field[] fieldOwnFields = field.getType().getDeclaredFields();
            boolean visibility = field.isAccessible();
            field.setAccessible(true);
            Object nestedObject = field.get(object);
            openRootTag(field);
            for (Field fieldOwnField : fieldOwnFields) {
                writeTypedField(fieldOwnField, nestedObject);
            }
            field.setAccessible(visibility);
            closeRootTag(field);
        } else {
            writeSimpleField(field, (T) object);
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
        indent -= INDENT_SIZE;
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
        indent += INDENT_SIZE;
        String tagName = XUtils.getTagNameFromField(field);
        doIndent();
        writer.write("<" + tagName + ">");
    }

    private void openRootTag(Field field) throws IOException {
        indent += INDENT_SIZE;
        String tagName = XUtils.getTagNameFromField(field);
        doIndent();
        writer.write("<" + tagName + ">\n");
    }

    private void closeRootTag(Field field) throws IOException {
        doIndent();
        String fieldTagName = "</" + XUtils.getTagNameFromField(field) + ">";
        writer.write(fieldTagName + "\n");
        indent -= INDENT_SIZE;
    }

    private void setUpXmlDoc() throws IOException {
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        String objectTagName = "<" + XUtils.getTagName(object.getClass()) + ">\n";
        indent += INDENT_SIZE;
        writer.write(objectTagName);
    }

    private void closeUpXmlDoc() throws IOException {
        indent -= INDENT_SIZE;
        String objectTagName = "</" + XUtils.getTagName(object.getClass()) + ">";
        writer.write(objectTagName);
    }


    private String getIndent() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) sb.append(' ');
        return sb.toString();
    }

    public void setIndentSize(int indentSize) {
        if (indentSize < 0) throw new RuntimeException("indent size cannot be negative");
        this.INDENT_SIZE = indentSize;
    }
}
