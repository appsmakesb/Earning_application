package com.Part.Time.Model;

public class Payment {

    private String payment_image;
    private String payment_minimum;
    private String payment_method;

    public Payment(String payment_image, String payment_minimum, String payment_method) {
        this.payment_image = payment_image;
        this.payment_minimum = payment_minimum;
        this.payment_method = payment_method;
    }

    public String getPayment_image() {
        return payment_image;
    }

    public String getPayment_minimum() {
        return payment_minimum;
    }

    public String getPayment_method() {
        return payment_method;
    }
}
