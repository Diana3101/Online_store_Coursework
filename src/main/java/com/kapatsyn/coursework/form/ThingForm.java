package com.kapatsyn.coursework.form;

import com.kapatsyn.coursework.entity.Thing;
import org.springframework.web.multipart.MultipartFile;

public class ThingForm {
    private String code;
    private String name;
    private double price;

    private boolean newThing = false;

    // Upload file.
    private MultipartFile fileData;

    public ThingForm() {
        this.newThing = true;
    }

    public ThingForm(Thing thing) {
        this.code = thing.getCode();
        this.name = thing.getName();
        this.price = thing.getPrice();
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

    public MultipartFile getFileData() {
        return fileData;
    }

    public void setFileData(MultipartFile fileData) {
        this.fileData = fileData;
    }

    public boolean isNewThing() {
        return newThing;
    }

    public void setNewThing(boolean newThing) {
        this.newThing = newThing;
    }

}
