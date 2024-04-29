package com.example.mobilalkfejl_serviceapp;

import java.io.Serializable;

public class ServiceItem implements Serializable {

    private String id;
    private String name;
    private int price;
    private int time;

    private String description;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ServiceItem(String id, String name, int price, int time, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.time = time;
        this.description = description;
    }

    public ServiceItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ServiceItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", time=" + time +
                ", description='" + description + '\'' +
                '}';
    }
}
