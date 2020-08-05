package com.appoftatar.workgroupcalendar.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;

@IgnoreExtraProperties
public class UserSick {

    private String diseaseOnsetDate;
    private String fullName;
    private String idUser;
    private String amountDays;
    private HashMap<String, Date> sickDays;



    public UserSick() {
    }

    public UserSick(String diseaseOnsetDate, String fullName, String idUser) {
        this.diseaseOnsetDate = diseaseOnsetDate;
        this.fullName = fullName;
        this.idUser = idUser;
    }

    public void setAmountDays(String amountDays) {
        this.amountDays = amountDays;
    }

    public String getAmountDays() {
        return amountDays;
    }

    public String getDiseaseOnsetDate() {
        return diseaseOnsetDate;
    }

    public String getFullName() {
        return fullName;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setDiseaseOnsetDate(String diseaseOnsetDate) {
        this.diseaseOnsetDate = diseaseOnsetDate;
    }
    public void setSickDays(HashMap<String, Date> sickDays) {
        this.sickDays = sickDays;
    }

    public HashMap<String, Date> getSickDays() {
        return sickDays;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
