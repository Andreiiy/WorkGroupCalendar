package com.appoftatar.workgroupcalendar.models;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.calendar.WorkDay;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RedactorShedule {

    private  ArrayList<User> shift3;
    private  ArrayList<User> shift2;
    private  ArrayList<User> shift1;
    private ArrayList<User> employesListInGroup;
    private ArrayList<User> employesList;

    private Schedule oldSchedule;
    private Schedule editSchedule;



    public RedactorShedule() {
        employesList = new ArrayList<>();
        employesListInGroup = new ArrayList<>();
        shift1 = new ArrayList<>();
        shift2 = new ArrayList<>();
        shift3 = new ArrayList<>();
        oldSchedule  = new Schedule();
        editSchedule = new Schedule();
    }

    public void addNewEmployee(String firstName, String lastName){
    User employee = new User();
    employee.FirstName = firstName;
    employee.SurName = lastName;
    employesList.add(employee);
    }

    public void setDateFrom(Date dateFrom) {
        editSchedule.setDateFrom(dateFrom);
    }

    public void setDateTo(Date dateTo) {
        editSchedule.setDateTo(dateTo);
    }

    public void sendEmployeeFromListToShift(Integer numberShift, User user){
        if(employesList != null){
           employesList.remove(user);
          if(numberShift==1)
              shift1.add(user);
          else if(numberShift==2)
                shift2.add(user);
           else if(numberShift==3)
                shift3.add(user);
        }
    }

    public void deleteEmployeeFromShift(Integer numberShift, User user){
            employesList.add(user);
        if(numberShift==1)
            shift1.remove(user);
        else if(numberShift==2)
            shift2.remove(user);
        else if(numberShift==3)
            shift3.remove(user);

    }

    public void saveSchedule(){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        setDataToShifts();
        root.child("schedules").child(Common.currentUser.ID).child(Common.currentGroup).child("oldSchedule").setValue(oldSchedule);
        root.child("schedules").child(Common.currentUser.ID).child(Common.currentGroup).child("newSchedule").setValue(editSchedule);
        saveHouersOfUsers(editSchedule);
    }

    private void saveHouersOfUsers(Schedule schedule){
        if(schedule != null) {

            Date firstDate = schedule.getDateFrom();
            Date lastDate = schedule.getDateTo();
            if(shift1.size() > 0)
            saveHouersOfShift(firstDate,lastDate, shift1,1);
            if(shift2.size() > 0)
            saveHouersOfShift(firstDate,lastDate, shift2,2);
            if(shift3.size() > 0)
            saveHouersOfShift(firstDate,lastDate, shift3,3);
        }

    }
    private void saveHouersOfShift(Date firstDate, Date lastDate, ArrayList<User> shift, int numberShift){

            DatabaseReference root = FirebaseDatabase.getInstance().getReference();
            Calendar calendar = new GregorianCalendar();


            for (User worker:shift) {
                calendar.setTime(firstDate);
                while (calendar.getTime().compareTo(lastDate) != 0) {
                    WorkDay workDay = new WorkDay();
                    workDay.setIdEmployee(worker.ID);
                    workDay.setDate(calendar.getTime());

                    if (worker.additionalInformation != null) {
                        if (numberShift == 1) {
                            workDay.setShift("1");
                            workDay.setAmountMinutes(worker.additionalInformation.getMinutesShift1());
                            workDay.setMoneyOfShift(worker.additionalInformation.getPaymentOfMinuteShift1());
                        } else if (numberShift == 2){
                            workDay.setShift("2");
                            workDay.setAmountMinutes(worker.additionalInformation.getMinutesShift2());
                        workDay.setMoneyOfShift(worker.additionalInformation.getPaymentOfMinuteShift2());
                    } else {
                            workDay.setShift("3");
                        workDay.setAmountMinutes(worker.additionalInformation.getMinutesShift3());
                        workDay.setMoneyOfShift(worker.additionalInformation.getPaymentOfMinuteShift3());
                    }

                }else {
                        workDay.setShift(Integer.toString(numberShift));
                        workDay.setAmountMinutes(0);
                        workDay.setMoneyOfShift(0f);
                    }
                    workDay.setAmountAdditionalMinutes(0);
                    workDay.setMoneyOfAdditionalTime(0f);

                    root.child("workdays").child(Integer.toString(calendar.getTime().getYear()+1900))
                            .child(Integer.toString(calendar.getTime().getMonth()))
                            .child(Integer.toString(calendar.getTime().getDate()))
                            .child(worker.IdManager).child(worker.IdWorkGroup)
                            .child(worker.ID)
                            .setValue(workDay);

                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }
//                WorkDay workDay = new WorkDay();
//                workDay.setIdEmployee(worker.ID);
//                workDay.setDate(calendar.getTime());
//                if (worker.additionalInformation != null) {
//                    if (numberShift == 1) {
//                        workDay.setShift("1");
//                        workDay.setAmountMinutes(worker.additionalInformation.getMinutesShift1());
//                        workDay.setMoneyOfShift(worker.additionalInformation.getPaymentOfMinuteShift1());
//                    } else if (numberShift == 2) {
//                        workDay.setShift("2");
//                        workDay.setAmountMinutes(worker.additionalInformation.getMinutesShift2());
//                        workDay.setMoneyOfShift(worker.additionalInformation.getPaymentOfMinuteShift2());
//                    } else {
//                        workDay.setShift("3");
//                        workDay.setAmountMinutes(worker.additionalInformation.getMinutesShift3());
//                        workDay.setMoneyOfShift(worker.additionalInformation.getPaymentOfMinuteShift3());
//                    }
//                }
//                workDay.setAmountAdditionalMinutes(0);
//                workDay.setMoneyOfAdditionalTime(0f);
//
//                root.child("workdays").child(Integer.toString(calendar.getTime().getYear()+1900))
//                        .child(Integer.toString(calendar.getTime().getMonth()))
//                        .child(Integer.toString(calendar.getTime().getDate()))
//                        .child(worker.IdManager).child(worker.IdWorkGroup)
//                        .child(worker.ID)
//                        .setValue(workDay);
           }


        }




    private void setDataToShifts(){
        clearSchedulesInEditSchudule();
        for(int i = 0;i < 3; i++){
            if(i+1==1){
                for (User employee:shift1) {
                    editSchedule.setEmployeeToShift(1,employee);
                }
            }else if(i+1==2){
                for (User employee:shift2) {
                    editSchedule.setEmployeeToShift(2,employee);
                }
            }else if(i+1==3){
                for (User employee:shift3) {
                    editSchedule.setEmployeeToShift(3,employee);
                }
            }
        }
    }

    public ArrayList<User> getShift3() {
        return shift3;
    }

    public ArrayList<User> getShift2() {
        return shift2;
    }

    public ArrayList<User> getShift1() {
        return shift1;
    }

    public Schedule getOldSchedule() {
        return oldSchedule;
    }

    public Schedule getEditSchedule() {
        setDataToShifts();
        return editSchedule;
    }

    public ArrayList<User> getEmployesList() {
        if(employesList.size()==0 && shift1.size() + shift2.size() + shift3.size()==0)
            employesList.addAll(employesListInGroup);
        return employesList;
    }


    public ArrayList<User> getEmployesListForWorkSchedule() {
        return employesList;
    }


    public void setOldSchedule(Schedule oldSchedule) {
        if(oldSchedule!=null)
            employesList.clear();
        this.oldSchedule = oldSchedule;
        for (int i = 0; i < 3; i++) {
            if (i + 1 == 1) {
                for (Integer j = 0; j < oldSchedule.shift1.size(); j++) {
                    shift1.add(oldSchedule.shift1.get(j.toString()));
                }
            } else if (i + 1 == 2) {
                for (Integer j = 0; j < oldSchedule.shift2.size(); j++) {
                    shift2.add(oldSchedule.shift2.get(j.toString()));
                }
            } else if (i + 1 == 3) {
                for (Integer j = 0; j < oldSchedule.shift3.size(); j++) {
                    shift3.add(oldSchedule.shift3.get(j.toString()));
                }
            }
        }
        checkAmountEmployes();
    }

    private void checkAmountEmployes(){
        if(shift1.size() + shift1.size() + shift1.size() < employesListInGroup.size()){
            ArrayList<User> listEmployesFromOldSchedule = new ArrayList<>();
            listEmployesFromOldSchedule.addAll(shift1);
            listEmployesFromOldSchedule.addAll(shift2);
            listEmployesFromOldSchedule.addAll(shift3);

            for ( User employee :employesListInGroup){
                Integer amount = 0;
                for( User worker :listEmployesFromOldSchedule){
                    if(employee.ID.equals(worker.ID))
                        amount++;
                }
                if(amount == 0){
                    employesList.add(employee);
                }
            }
        }
    }

    public void setEmployesListInGroup(ArrayList<User> employesListInGroup) {
        this.employesListInGroup = employesListInGroup;

    }


    public void moveShifts() {
        ArrayList<User> shift0 = shift1;
        shift1 = shift3;
        shift3 = shift2;
        shift2 = shift0;
    }

    public void clearSchedule() {
        shift1.clear();
        shift2.clear();
        shift3.clear();
        employesList.clear();
        employesList.addAll(employesListInGroup);
    }

    public void clearEditSchedule() {
        editSchedule = new Schedule();
    }

    public void clearSchedulesInEditSchudule() {
       editSchedule.shift1.clear();
        editSchedule.shift2.clear();
        editSchedule.shift3.clear();

    }
}
