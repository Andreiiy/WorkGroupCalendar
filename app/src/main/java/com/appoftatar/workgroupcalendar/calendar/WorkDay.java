package com.appoftatar.workgroupcalendar.calendar;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Date;


@IgnoreExtraProperties
public class WorkDay implements Serializable {
   private String IdEmployee;
   private Date date;
   private String shift;
   private Integer amountMinutes;
   private Float moneyOfShift;
   private Integer amountAdditionalMinutes;
   private Float moneyOfAdditionalTime;


    public WorkDay() {
    }

    public String getIdEmployee() {
        return IdEmployee;
    }

    public void setIdEmployee(String idEmployee) {
        IdEmployee = idEmployee;
    }

    public Date getDate() {
        return date;
    }

    public String getShift() {
        return shift;
    }

    public Integer getAmountMinutes() {
        return amountMinutes;
    }

    public Float getMoneyOfShift() {
        return moneyOfShift;
    }

    public Integer getAmountAdditionalMinutes() {
        return amountAdditionalMinutes;
    }

    public Float getMoneyOfAdditionalTime() {
        return moneyOfAdditionalTime;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public void setAmountMinutes(Integer amountMinutes) {
        this.amountMinutes = amountMinutes;
    }

    public void setMoneyOfShift(Float moneyOfShift) {
        this.moneyOfShift = moneyOfShift;
    }

    public void setAmountAdditionalMinutes(Integer amountAdditionalMinutes) {
        this.amountAdditionalMinutes = amountAdditionalMinutes;
    }

    public void setMoneyOfAdditionalTime(Float moneyOfAdditionalTime) {
        this.moneyOfAdditionalTime = moneyOfAdditionalTime;
    }
}
