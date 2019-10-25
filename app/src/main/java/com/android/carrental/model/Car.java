package com.android.carrental.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Car implements Serializable {

    private final String id;
    private final String name;
    private final CarModel model;
    private final String color;
    private final Station station;
    private final String licenceNumber;
    private final int rate;

    public Car() {
        this.id = "";
        this.name = "";
        this.model = null;
        this.color = "";
        this.station = null;
        this.licenceNumber = "";
        rate = 0;
    }

    public Car(String id, String name, String color, String licenceNumber, int rate, CarModel model, Station station) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.color = color;
        this.station = station;
        this.licenceNumber = licenceNumber;
        this.rate = rate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CarModel getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public Station getStation() {
        return station;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public int getRate() {
        return rate;
    }


}
