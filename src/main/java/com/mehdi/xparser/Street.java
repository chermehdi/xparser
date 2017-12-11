package com.mehdi.xparser;

import com.mehdi.xparser.annotations.Composite;
import com.mehdi.xparser.annotations.RootElement;

@RootElement
public class Street {

    String name;

    @Composite
    Address address;

    public Street() {

    }

    @Override
    public String toString() {
        return "Street{" +
                "name='" + name + '\'' +
                ", address=" + address +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }
}
