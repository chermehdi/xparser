package com.mehdi.xparser.utils;

import com.mehdi.xparser.Student;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class XUtilsTest {

    @Test
    public void getTagName() {
        String stringName = XUtils.getTagName(String.class);
        String studentName = XUtils.getTagName(StudentQualifierObject.class);
        assertThat(stringName, is("string"));
        assertThat(studentName, is("student-qualifier-object"));
    }

    @Test
    public void getTagNameFromField() throws NoSuchFieldException {
        Field firstName = StudentQualifierObject.class.getDeclaredField("firstName");
        Field lastName = StudentQualifierObject.class.getDeclaredField("lastName");
        assertThat(XUtils.getTagNameFromField(firstName), is("first-name"));
        assertThat(XUtils.getTagNameFromField(lastName), is("last-name"));
    }

    class StudentQualifierObject {
        String firstName;
        String lastName;
    }
}