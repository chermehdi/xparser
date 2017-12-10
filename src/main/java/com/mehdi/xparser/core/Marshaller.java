package com.mehdi.xparser.core;

import java.io.IOException;
import java.io.Writer;

public class Marshaller<T> {

    private Writer writer;

    private T object;

    public Marshaller(T obj, Writer writer) {
        this.object = obj;
        this.writer = writer;
    }


    public void marshall() {
        try {
            setUpXmlDoc();
            
        }catch (Exception e){

        }
    }

    private void setUpXmlDoc() throws IOException {
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
    }
}
