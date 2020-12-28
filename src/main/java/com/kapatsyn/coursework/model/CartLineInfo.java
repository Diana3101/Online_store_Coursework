package com.kapatsyn.coursework.model;

public class CartLineInfo {
    private ThingInfo thingInfo;
    private int quantity;

    public CartLineInfo() {
        this.quantity = 0;
    }

    public ThingInfo getThingInfo() {
        return thingInfo;
    }

    public void setThingInfo(ThingInfo thingInfo) {
        this.thingInfo = thingInfo;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return this.thingInfo.getPrice() * this.quantity;
    }
}
