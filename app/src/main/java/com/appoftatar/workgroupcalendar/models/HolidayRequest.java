package com.appoftatar.workgroupcalendar.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;


@IgnoreExtraProperties
public class HolidayRequest {

    private String id;
    private String idEmployee;
    private String nameEmployee;
    private String reasonRequest;
    private String viewEmployee;
    private String answerRequest;
    private String rejectionReason;
    private String date;
    private HashMap<String, Date> dateList = new  HashMap<>() ;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public HolidayRequest() {

    }


    public HolidayRequest(String id, String idEmployee, String nameEmployee, String reasonRequest, String viewEmployee, String answerRequest, String rejectionReason) {
        this.id = id;
        this.idEmployee = idEmployee;
        this.nameEmployee = nameEmployee;
        this.reasonRequest = reasonRequest;
        this.viewEmployee = viewEmployee;
        this.answerRequest = answerRequest;
        this.rejectionReason = rejectionReason;

    }

    public void setDateToList(Date date) {
        if(dateList==null)
            dateList = new HashMap<>();
        Integer index = dateList.size();
        this.dateList.put(index.toString(),date);
    }

    public String getId() {
        return id;
    }

    public String getIdEmployee() {
        return idEmployee;
    }

    public String getNameEmployee() {
        return nameEmployee;
    }

    public String getReasonRequest() {
        return reasonRequest;
    }

    public String getViewEmployee() {
        return viewEmployee;
    }

    public String getAnswerRequest() {
        return answerRequest;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public HashMap<String, Date> getDateList() {
        return dateList;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
    }

    public void setNameEmployee(String nameEmployee) {
        this.nameEmployee = nameEmployee;
    }

    public void setReasonRequest(String reasonRequest) {
        this.reasonRequest = reasonRequest;
    }

    public void setViewEmployee(String viewEmployee) {
        this.viewEmployee = viewEmployee;
    }

    public void setAnswerRequest(String answerRequest) {
        this.answerRequest = answerRequest;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public void setDateList(HashMap<String, Date> dateList) {
        this.dateList = dateList;
    }
}
