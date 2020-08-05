package com.appoftatar.workgroupcalendar.models;

public class SalaryInformationOfMonth {

    private Integer numberMonth;
    private Float timeShift1;
    private  Float timeShift2;
    private  Float timeShift3;
    private Float paymentShift1;
    private  Float paymentShift2;
    private Float paymentShift3;
    private Float additionalTime;
    private Float additionalPayment;
    private Float paymentMonth;
    private Float vacationPay;


    public SalaryInformationOfMonth() {
    }

    public Integer getNumberMonth() {
        return numberMonth;
    }

    public void setNumberMonth(Integer numberMonth) {
        this.numberMonth = numberMonth;
    }

    public Float getTimeShift1() {
        return timeShift1;
    }

    public Float getTimeShift2() {
        return timeShift2;
    }

    public Float getTimeShift3() {
        return timeShift3;
    }

    public Float getPaymentShift1() {
        return paymentShift1;
    }

    public Float getPaymentShift2() {
        return paymentShift2;
    }

    public Float getPaymentShift3() {
        return paymentShift3;
    }

    public Float getAdditionalTime() {
        return additionalTime;
    }

    public Float getAdditionalPayment() {
        return additionalPayment;
    }

    public Float getPaymentMonth() {
        return paymentMonth;
    }

    public Float getVacationPay() {
        return vacationPay;
    }

    public void setTimeShift1(Float timeShift1) {
        this.timeShift1 = timeShift1;
    }

    public void setTimeShift2(Float timeShift2) {
        this.timeShift2 = timeShift2;
    }

    public void setTimeShift3(Float timeShift3) {
        this.timeShift3 = timeShift3;
    }

    public void setPaymentShift1(Float paymentShift1) {
        this.paymentShift1 = paymentShift1;
    }

    public void setPaymentShift2(Float paymentShift2) {
        this.paymentShift2 = paymentShift2;
    }

    public void setPaymentShift3(Float paymentShift3) {
        this.paymentShift3 = paymentShift3;
    }

    public void setAdditionalTime(Float additionalTime) {
        this.additionalTime = additionalTime;
    }

    public void setAdditionalPayment(Float additionalPayment) {
        this.additionalPayment = additionalPayment;
    }

    public void setPaymentMonth(Float paymentMonth) {
        this.paymentMonth = paymentMonth;
    }

    public void setVacationPay(Float vacationPay) {
        this.vacationPay = vacationPay;
    }
}
