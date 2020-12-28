package com.kapatsyn.coursework.model;

import com.kapatsyn.coursework.entity.Thing;

import java.util.ArrayList;
import java.util.List;

public class ThingInfo {
    private String code;
    private String name;
    private double price;

    public ThingInfo() {
    }

    public ThingInfo(Thing thing) {
        this.code = thing.getCode();
        this.name = thing.getName();
        this.price = thing.getPrice();
    }
    // Using in JPA/Hibernate query
    public ThingInfo(String code, String name, double price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
