package com.appoftatar.workgroupcalendar.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;



@IgnoreExtraProperties
public class UserInVacation {

    private String userID;
    private String userFullName;
    private HashMap<String, Date> dateList = new  HashMap<>();

    public UserInVacation() {
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public void setDateList(HashMap<String, Date> dateList) {
        this.dateList = dateList;
    }


    public String getUserID() {
        return userID;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public HashMap<String, Date> getDateList() {
        return dateList;
    }

    public void setDateToList(Date date) {
        if(dateList==null)
            dateList = new HashMap<>();
        Integer index = dateList.size();
        this.dateList.put(index.toString(),date);
    }
}
