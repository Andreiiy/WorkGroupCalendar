package com.appoftatar.workgroupcalendar.models;

import java.util.Date;
import java.util.HashMap;

public class Schedule {

    private Date dateFrom;
    private Date dateTo;

    HashMap<String, User> shift1;
    HashMap<String, User> shift2;
    HashMap<String, User> shift3;

    public Schedule() {
        shift1 = new HashMap<>();
        shift2 = new HashMap<>();
        shift3 = new HashMap<>();
    }

    public void setEmployeeToShift(Integer numberShift ,User user) {
        switch (numberShift){
            case 1:{
                if(shift1==null)
                    shift1 = new HashMap<>();
                Integer index = shift1.size();
                this.shift1.put(index.toString(),user);
            }break;
            case 2:{
                if(shift2==null)
                    shift2 = new HashMap<>();
                Integer index = shift2.size();
                this.shift2.put(index.toString(),user);
            }break;
            case 3:{
                if(shift3==null)
                    shift3 = new HashMap<>();
                Integer index = shift3.size();
                this.shift3.put(index.toString(),user);
            }break;
        }

    }

    public void deleteDateFrom(){
        dateFrom = null;
    }
    public void deleteDateTo(){
        dateTo = null;
    }

    public void deleteEmployeeFromShift(Integer numberShift ,User user) {
        switch (numberShift) {
            case 1: {
                  deleteEmployee(shift1,  user);
                    }
            break;
            case 2: {
                  deleteEmployee(shift2,  user);
                     }
            break;
            case 3: {
                  deleteEmployee(shift3,  user);

            }break;
        }
    }

    private void deleteEmployee(HashMap<String, User> shift,User user){
        if (shift == null)
            return;
        else {
            for (Integer i = 0; i < shift.size() ; i++) {
                if (shift.get(i.toString()) == user)
                    shift.remove(i.toString());
            }
        }
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public HashMap<String, User> getShift1() {
        return shift1;
    }

    public HashMap<String, User> getShift2() {
        return shift2;
    }

    public HashMap<String, User> getShift3() {
        return shift3;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public void setShift1(HashMap<String, User> shift1) {
        this.shift1 = shift1;
    }

    public void setShift2(HashMap<String, User> shift2) {
        this.shift2 = shift2;
    }

    public void setShift3(HashMap<String, User> shift3) {
        this.shift3 = shift3;
    }
}
