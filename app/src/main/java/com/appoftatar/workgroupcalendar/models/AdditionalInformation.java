package com.appoftatar.workgroupcalendar.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;


@IgnoreExtraProperties
public class AdditionalInformation {

    Integer minutesShift1;
    Integer minutesShift2;
    Integer minutesShift3;
    Float paymentOfMinuteShift1;
    Float paymentOfMinuteShift2;
    Float paymentOfMinuteShift3;

    public AdditionalInformation() {
    }

    public Integer getMinutesShift1() {
        return minutesShift1;
    }

    public Integer getMinutesShift2() {
        return minutesShift2;
    }

    public Integer getMinutesShift3() {
        return minutesShift3;
    }

    public Integer getMinutesShift(Integer numberShift)
    {
       if(numberShift==1)
        return minutesShift1;

       else if(numberShift==2)
           return minutesShift2;
       else
           return minutesShift3;
    }

    public Float getPaymentOfMinuteShift1() {
        return paymentOfMinuteShift1;
    }

    public Float getPaymentOfMinuteShift2() {
        return paymentOfMinuteShift2;
    }

    public Float getPaymentOfMinuteShift3() {
        return paymentOfMinuteShift3;
    }

    public Float getPaymentOfMinuteShift(Integer numberShift) {
        if(numberShift==1)
            return paymentOfMinuteShift1;

        else if(numberShift==2)
            return paymentOfMinuteShift2;
        else
            return paymentOfMinuteShift3;
    }

    public void setMinutesShift1(Integer minutesShift1) {
        this.minutesShift1 = minutesShift1;
    }

    public void setMinutesShift2(Integer minutesShift2) {
        this.minutesShift2 = minutesShift2;
    }

    public void setMinutesShift3(Integer minutesShift3) {
        this.minutesShift3 = minutesShift3;
    }

    public void setPaymentOfMinuteShift1(Float paymentOfMinuteShift1) {
        this.paymentOfMinuteShift1 = paymentOfMinuteShift1;
    }

    public void setPaymentOfMinuteShift2(Float paymentOfMinuteShift2) {
        this.paymentOfMinuteShift2 = paymentOfMinuteShift2;
    }

    public void setPaymentOfMinuteShift3(Float paymentOfMinuteShift3) {
        this.paymentOfMinuteShift3 = paymentOfMinuteShift3;
    }
}
