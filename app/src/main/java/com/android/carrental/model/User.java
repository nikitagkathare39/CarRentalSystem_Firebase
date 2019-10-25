package com.android.carrental.model;

public class User {
    private String name;
    private String streetAddress;
    private String aptNumber;
    private String city;
    private String zipCode;
    private String email;
    private String phoneNumber;
    private String id;
    private CreditCard creditCard;

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public CreditCard getCreditCard() {
        return this.creditCard;
    }

    public String getName() {
        return name;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getAptNumber() {
        return aptNumber;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public void setAptNumber(String aptNumber) {
        this.aptNumber = aptNumber;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setId(String id) {
        this.id = id;
    }


    public User() {
        this.name = "";
        this.streetAddress = "";
        this.aptNumber = "";
        this.city = "";
        this.zipCode = "";
        this.email = "";
        this.id = "";
        this.phoneNumber = "";
        this.creditCard = null;
    }

    public User(String id, String cardId, String name, String email, String phoneNumber, String streetAddress, String aptNumber, String city, String zipCode) {
        this.name = name;
        this.streetAddress = streetAddress;
        this.aptNumber = aptNumber;
        this.city = city;
        this.zipCode = zipCode;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.creditCard = null;
        this.id = id;
    }
}