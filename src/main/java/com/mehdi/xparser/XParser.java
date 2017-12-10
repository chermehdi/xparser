package com.mehdi.xparser;

import com.mehdi.xparser.core.Marshaller;
import com.mehdi.xparser.core.UnMarshaller;
import com.mehdi.xparser.core.XNode;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * main class of the api provides static methods for parsing models and converting them to xml <b> marshaling </b>
 * or converting xml to models using reflection <b>unmarshaling</b>
 *
 * @author Mehdi
 */
public class XParser {

    public static final int DEFAULT_BUFFER_SIZE = 1024;

    /**
     * private constructor to prevent users from instantiating the XParser class
     */
    private XParser() {
        throw new UnsupportedOperationException("cannot instantiate object of type XParser");
    }

    /**
     * parse an Object from an InputStream, the reader object must represent a valid
     * xml file, that matches the clazz parameter
     */
    public static <T> T unmarshal(Reader reader, Class<T> clazz) {
        InputStream is = null;
        try {
            is = getInputStream(reader);
            UnMarshaller<T> unMarshaller = buildUnmarshaller(is, clazz);
            return unMarshaller.unmarshal();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeStream(is);
        }
    }

    private static InputStream getInputStream(Reader reader) throws IOException {
        StringBuffer buffer = new StringBuffer();
        char[] charBuffer = new char[DEFAULT_BUFFER_SIZE];
        int pointer = 0;
        while ((pointer = reader.read(charBuffer, 0, DEFAULT_BUFFER_SIZE)) != -1) {
            buffer.append(charBuffer, 0, pointer);
        }
        InputStream is = new ByteArrayInputStream(buffer.toString().getBytes(StandardCharsets.UTF_8));
        return is;
    }

    private static void closeStream(InputStream is) {
        try {
            if (is != null)
                is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static <T> UnMarshaller<T> buildUnmarshaller(InputStream is, Class<T> clazz) {
        XNode root = new XNode(is);
        return new UnMarshaller<>(root, clazz);
    }

    private static <T> Marshaller buildMarshaller(T obj, Writer writer) {
        return new Marshaller<>(obj, writer);
    }

    /**
     * writes the Object to the Writer Object
     */
    public static <T> void marshal(T obj, Writer writer) {
        Marshaller marshaller = buildMarshaller(obj, writer);
        marshaller.marshall();
    }

}
