package com.Part.Time.Model;

public class PaymentHistory {

    private String payment_pending;
    private String payment_amount;
    private String payment_date;
    private String payment_userid;
    private String icon_image;

    public PaymentHistory(String payment_pending, String payment_amount, String payment_date, String payment_userid, String icon_image) {
        this.payment_pending = payment_pending;
        this.payment_amount = payment_amount;
        this.payment_date = payment_date;
        this.payment_userid = payment_userid;
        this.icon_image = icon_image;
    }

    public String getPayment_pending() {
        return payment_pending;
    }

    public String getPayment_amount() {
        return payment_amount;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public String getPayment_userid() {
        return payment_userid;
    }

    public String getIcon_image() {
        return icon_image;
    }
}
