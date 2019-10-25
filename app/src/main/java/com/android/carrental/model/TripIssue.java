package com.android.carrental.model;

public class TripIssue {
    private String user;
    private String station;
    private String bookingDate;
    private String startTime;
    private String endTime;
    private Car car;
    private int rate;
    private String issue;

    public TripIssue(String user, String address, String bookingDate, String startTime, String endTime, int rate, String trim) {
    }

    public TripIssue(String user, String station, String bookingDate, String startTime, String endTime, Car car, int rate, String issue) {
        this.user = user;
        this.station = station;
        this.bookingDate = bookingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.car = car;
        this.rate = rate;
        this.issue = issue;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }


    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }
}
