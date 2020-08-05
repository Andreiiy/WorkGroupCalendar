package com.appoftatar.workgroupcalendar.calendar;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.models.UserInVacation;
import com.appoftatar.workgroupcalendar.models.UserSick;
import com.appoftatar.workgroupcalendar.ui.workcalendar.WorkCalendarFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MonthWorkCalendar {

    private final MyCalendar myCalendar;
    private final ArrayList<Date> listDatesForWorkMonth;
    private String name;
    private Integer numberMonth;
    private ArrayList<ItemWorkCalendar> workDays;
    private ArrayList<WorkDay> workDaysWithShifts;

    public MonthWorkCalendar(Integer numberMonth) {
        this.numberMonth = numberMonth;
        Calendar calendar = Calendar.getInstance();
        myCalendar = new MyCalendar();
        listDatesForWorkMonth = myCalendar.getMonth(numberMonth);
        calendar.setTime(listDatesForWorkMonth.get(15));
        name =calendar.getDisplayName(calendar.MONTH,Calendar.LONG, Locale.ENGLISH);
        createListDays();
    }

    private void createListDays(){
        workDays = new ArrayList<>();

        for(int i = 0; i < listDatesForWorkMonth.size() ; i++){
            ItemWorkCalendar workDay = new ItemWorkCalendar();
           workDay.setDate(listDatesForWorkMonth.get(i));
           workDays.add(workDay);
        }

    }

    public Integer getNumberMonth() {
        return numberMonth;
    }

    public ArrayList<WorkDay> getWorkDaysWithShifts() {
        return workDaysWithShifts;
    }

    public ArrayList<ItemWorkCalendar> getWorkDays() {
        return workDays;
    }

    public void setWorkerSick(UserSick workerSick){
        Date currentDate = new Date();

        String[] tokens = workerSick.getDiseaseOnsetDate().split("/");

        Date diseaseOnsetDate = new GregorianCalendar(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[1])-1, Integer.parseInt(tokens[0])).getTime();

       for (ItemWorkCalendar item :workDays){
           if(item.getDate().getDate() >= diseaseOnsetDate.getDate() &&
            item.getDate().getDate() <= currentDate.getDate() &&
                   item.getDate().getMonth() == diseaseOnsetDate.getMonth()){
               item.setWorkerSick(workerSick.getFullName());
           }
       }
    }

    public void clearListEmployesInVacation(){
        for (ItemWorkCalendar item :workDays){
            item.clearWorkersOnVacation();
        }
    }

    public void clearListWorkersSick(){
        for (ItemWorkCalendar item :workDays){
            item.getWorkersSick();
        }
    }

    public void setWorkerOnVacation(UserInVacation workerInVacation){
       for(Integer i = 0;i < workerInVacation.getDateList().size();i++) {
           Date dateVacation = workerInVacation.getDateList().get(i.toString());

        for (ItemWorkCalendar item :workDays){
            if(item.getDate().getDate()== dateVacation.getDate() &&
                    item.getDate().getMonth() == dateVacation.getMonth()){
                item.setWorkerOnVacation(workerInVacation.getUserFullName());
            }
        }
    }

    }

    public String getName(){
       return name;
   }

    public void setName(String name){
        this.name = name;
    }

    public void putDayToMonth(ItemWorkCalendar day){
        workDays.add(day);

    }

    public ItemWorkCalendar getDay(int numberDay){
        return workDays.get(numberDay);
    }


    //////////////////////////////////////////////////////////////////////////////////

    public void createWorkCalendar(final WorkCalendarFragment act){
                 createWorkDaysList();
                 getWorkerDays(act);
    }


    private void setWorkerDaysToCalendar(WorkCalendarFragment act, ArrayList<WorkDay> workDaysList){
        for (WorkDay item:workDaysList) {
            Date currentDate = new GregorianCalendar().getTime();
            if(currentDate.compareTo(item.getDate())>=0)
            for (int i = 0; i < workDaysWithShifts.size(); i++) {
                 if (workDaysWithShifts.get(i).getDate().compareTo(item.getDate()) == 0)
                    if(workDaysWithShifts.get(i).getShift()==null)
                        workDaysWithShifts.set(i, item);
                    else if(!workDaysWithShifts.get(i).getShift().equals("Sick") && !workDaysWithShifts.get(i).getShift().equals("Vacation")) {
                        workDaysWithShifts.set(i, item);
                    }
            }
        }

        act.initRecyclerViewCalendar();
        act.showDataSalary();
    }



    private void getWorkerDays(final WorkCalendarFragment act){

        DatabaseReference   root = FirebaseDatabase.getInstance().getReference();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(listDatesForWorkMonth.get(15));
        root.child("workdays").child(Integer.toString(calendar.getTime().getYear()+1900))
                .child(Integer.toString(numberMonth)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<WorkDay> workDaysList = new ArrayList<>();
                for (DataSnapshot dateSnapshot: dataSnapshot.getChildren()) {
                    WorkDay workDay = new WorkDay();
                    workDay =  dateSnapshot.child(Common.currentUser.IdManager).child(Common.currentUser.IdWorkGroup)
                            .child(Common.currentUser.ID).getValue(WorkDay.class);


                    if(workDay != null)
                        workDaysList.add(workDay);

                }
               setWorkerDaysToCalendar(act,workDaysList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });


    }


    private void createWorkDaysList() {
        workDaysWithShifts = new ArrayList<>();
        for(Date data:listDatesForWorkMonth){
            WorkDay workday = new WorkDay();
            workday.setDate(data);
            workDaysWithShifts.add(workday);
           }
    }


}
