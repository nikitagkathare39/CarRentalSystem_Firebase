package com.android.carrental.model;

import java.io.Serializable;

public class CreditCard implements Serializable {

    private String cardNumber;
    private String cvv;
    private String zipCode;
    private String expMonth;
    private String expYear;

    public CreditCard(String cardNumber, String cvv, String zipCode, String exp_month, String exp_year) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.zipCode = zipCode;
        this.expMonth = exp_month;
        this.expYear = exp_year;
    }

    public CreditCard() {

    }

    public String getExpMonth() {
        return expMonth;
    }

    public String getExpYear() {
        return expYear;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public String getZipCode() {
        return zipCode;
    }

}

