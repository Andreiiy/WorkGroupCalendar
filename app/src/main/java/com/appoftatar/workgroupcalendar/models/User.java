package com.appoftatar.workgroupcalendar.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;


@IgnoreExtraProperties
public class User {

    public String ID;
    public String FirstName;
    public  String SurName;
    public  String Email;
    public String Telefon;
    public String IdWorkGroup;
    public String IdManager;
    public String UserSik;
    public String Password;
    public String Manager;
    public AdditionalInformation  additionalInformation;
    //public HashMap<String,AdditionalInformation>  additionalInformationMAP;



    public User() {
    }

    public User(String ID, String firstName, String surName, String email, String telefon, String idWorkGroup, String idManager, String userSik, String password, String manager) {
        this.ID = ID;
        FirstName = firstName;
        SurName = surName;
        Email = email;
        Telefon = telefon;
        IdWorkGroup = idWorkGroup;
        IdManager = idManager;
        UserSik = userSik;
        Password = password;
        Manager = manager;
    }

//    public void setAdditionalInformation( AdditionalInformation additionalInformationMAP) {
//        if(this.additionalInformationMAP == null)
//        this.additionalInformationMAP = new HashMap<>();
//        this.additionalInformationMAP.put("0",additionalInformationMAP);
//    }
}
