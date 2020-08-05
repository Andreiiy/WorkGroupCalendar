package com.appoftatar.workgroupcalendar.calendar;

import java.util.ArrayList;
import java.util.Date;

public class ItemWorkCalendar {

    private   ArrayList<String> workersOnVacation;
    private   ArrayList<String> workersSick;
    private   Date date;

    public ItemWorkCalendar() {
        workersOnVacation = new ArrayList<>();
        workersSick = new ArrayList<>();
    }

    public void clearWorkersOnVacation(){
        workersOnVacation.clear();
    }
    public void clearWorkersSick(){
        workersSick.clear();
    }

    public void setWorkerSick(String workerName){
        workersSick.add(workerName);
    }
    public void setWorkerOnVacation(String workerName){
        workersOnVacation.add(workerName);
    }

    public ArrayList<String> getWorkersOnVacation() {
        return workersOnVacation;
    }

    public ArrayList<String> getWorkersSick() {
        return workersSick;
    }

    public Date getDate() {
        return date;
    }


    public void setWorkersOnVacation(ArrayList<String> workersOnVacation) {
        this.workersOnVacation = workersOnVacation;
    }

    public void setWorkersSick(ArrayList<String> workersSick) {
        this.workersSick = workersSick;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
