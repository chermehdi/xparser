package com.mehdi.xparser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Paths;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class XParserTest {

    FileReader reader;

    @Before
    public void setUp() throws Exception {
        String root = Paths.get("").toAbsolutePath().toString();
        reader = new FileReader(root + "/student.xml");
    }

    @Test
    public void unmarshal() {
        Student marshaled = XParser.unmarshal(reader, Student.class);
        assertNotNull(marshaled);
        assertThat(marshaled.getAge(), is(16));
        assertThat(marshaled.getFirstName(), is("mehdi"));
        assertThat(marshaled.getLastName(), is("cheracher"));
    }

    @Test
    public void marshal() {
        Student s = new Student();
        s.firstName = "mehdi";
        s.lastName = "hello";
        s.age = 32;
        StringWriter sw = new StringWriter();
        XParser.marshal(s, sw);
        assertNotNull(sw);
    }

    @After  
    public void tearDown() throws Exception {
        reader.close();
    }

}
