package com.android.carrental.model;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.carrental.R;

import java.io.Serializable;
import java.util.Date;

public class CarBooking implements Serializable {

    private String id;
    private String user;
    private Station station;
    private String bookingDate;
    private String startTime;
    private String endTime;
    private Car car;
    private int rate;
    private int hoursBooked;
    private boolean isComplete;

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRate() {
        return rate;
    }

    public CarBooking() {

    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void setHoursBooked(int hoursBooked) {
        this.hoursBooked = hoursBooked;
    }

    public CarBooking(String id, String user, Car car, Station station, String bookingDate, String startTime, String endTime, int rate, int hoursBooked, boolean isComplete) {
        this.user = user;
        this.station = station;
        this.bookingDate = bookingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.car = car;
        this.rate = rate;
        this.hoursBooked = hoursBooked;
        this.isComplete = isComplete;
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public String getUser() {
        return user;
    }

    public Station getStation() {
        return station;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
