package com.android.carrental.model;

import java.io.Serializable;

public class CarModel implements Serializable {

    private String name;
    private String url;

    public CarModel(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public CarModel() {

    }


    @Override
    public String toString() {
        return name;
    }
}
