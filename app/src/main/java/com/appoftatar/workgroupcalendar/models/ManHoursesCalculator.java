package com.appoftatar.workgroupcalendar.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.calendar.WorkDay;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ManHoursesCalculator {

    private Integer timeOfShift1;
    private Integer timeOfShift2;
    private Integer timeOfShift3;
    private ArrayList<ManHoursInformationOfMonth> workDaysInformationOfYear;
    private ManHoursesCalculator.ManHoursInformationObserver observer;

    public ManHoursesCalculator(ManHoursesCalculator.ManHoursInformationObserver observer) {
        this.observer = observer;

        getDurationOfShifts();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void getDurationOfShifts() {

        DatabaseReference rootDataBase = FirebaseDatabase.getInstance().getReference();

        rootDataBase.child("users").orderByChild("IdWorkGroup").equalTo(Common.currentGroup).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<AdditionalInformation> additionalInformationList = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user.additionalInformation != null)
                        additionalInformationList.add(user.additionalInformation);

                }
                if (additionalInformationList.size() != 0) {
                    findTimeOfShifts(additionalInformationList);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void findTimeOfShifts(ArrayList<AdditionalInformation> additionalInformationList) {
        if (additionalInformationList.size() == 1) {
            timeOfShift1 = additionalInformationList.get(0).getMinutesShift1();
            timeOfShift2 = additionalInformationList.get(0).getMinutesShift2();
            timeOfShift3 = additionalInformationList.get(0).getMinutesShift3();
        } else {

            findTimeOfShift(1, additionalInformationList);
            findTimeOfShift(2, additionalInformationList);
            findTimeOfShift(3, additionalInformationList);
        }
    }

    private void findTimeOfShift(int numberShift, ArrayList<AdditionalInformation> additionalInformationList) {
        HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
        Integer am;
        for (AdditionalInformation item : additionalInformationList) {

            if (numberShift == 1 && item.minutesShift1 != 0) {
                am = hm.get(item.minutesShift1);
                hm.put(item.minutesShift1, am == null ? 1 : am + 1);
            } else if (numberShift == 2 && item.minutesShift2 != 0) {
                am = hm.get(item.minutesShift2);
                hm.put(item.minutesShift2, am == null ? 1 : am + 1);
            } else if (numberShift == 3 && item.minutesShift3 != 0) {
                am = hm.get(item.minutesShift3);
                hm.put(item.minutesShift3, am == null ? 1 : am + 1);
            }

        }
        Map.Entry<Integer, Integer> checkValue = null;
        for (Map.Entry<Integer, Integer> entry : hm.entrySet()) {
            if (checkValue == null || entry.getValue() > checkValue.getValue()) {
                checkValue = entry;
            }

        }
        if (numberShift == 1) {
            timeOfShift1 = checkValue.getKey();
        } else if (numberShift == 2) {
            timeOfShift2 = checkValue.getKey();
        } else {
            timeOfShift3 = checkValue.getKey();
        }

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////

    public void createSalaryInformationOfYear() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        final Date currentDate = new GregorianCalendar().getTime();
        root.child("workdays").child(Integer.toString(currentDate.getYear() + 1900))
                .addValueEventListener(new ValueEventListener() {


                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        workDaysInformationOfYear = new ArrayList<>();
                        for (DataSnapshot monthSnapshot : dataSnapshot.getChildren()) {
                            ArrayList<WorkDay> workDaysList = new ArrayList<>();

                            for (DataSnapshot daySnapshot : monthSnapshot.getChildren()) {

                                for (DataSnapshot userdaySnapshot : daySnapshot.child(Common.currentUser.ID).child(Common.currentGroup).getChildren()) {
                                     WorkDay   workDay = userdaySnapshot.getValue(WorkDay.class);
                                        if (workDay != null)
                                            workDaysList.add(workDay);
                                }
                            }

                            if (workDaysList.size() != 0) {
                                if(getManHoursInformationOfMonth(workDaysList)!=null)
                                workDaysInformationOfYear.add(getManHoursInformationOfMonth(workDaysList));
                            }
                        }
                        if (workDaysInformationOfYear.size() != 0) {

                            observer.ShowCharts(workDaysInformationOfYear);
                        }

                        if (getInformationOfMonth(currentDate.getMonth()) != null)
                            observer.ShowManHoursInformationOfMonth(getInformationOfMonth(currentDate.getMonth()));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Data base", "Failed to read value.", databaseError.toException());
                    }
                });
    }

    public ManHoursInformationOfMonth getInformationOfMonth(int numberMonth) {
        ManHoursInformationOfMonth month=null;
        for (ManHoursInformationOfMonth item:workDaysInformationOfYear) {
            if(item.getNumberMonth() == numberMonth){
               month = item;
                return item;
            }

        }

        return month;
    }


    private ManHoursInformationOfMonth getManHoursInformationOfMonth(ArrayList<WorkDay> workDaysList) {
        Date currentDate = new GregorianCalendar().getTime();
        ManHoursInformationOfMonth manHoursInformationOfMonth=null;
        if(workDaysList.get(0).getDate().before(currentDate) || workDaysList.get(0).getDate().compareTo(currentDate) == 0) {
            manHoursInformationOfMonth = new ManHoursInformationOfMonth();
            manHoursInformationOfMonth.setNumberMonth(workDaysList.get(0).getDate().getMonth());

            for (WorkDay workday : workDaysList) {
                if (workday.getDate().before(currentDate) || workday.getDate().compareTo(currentDate) == 0) {

                    if (workday.getShift() != null && !workday.getShift().equals("Vacation") && !workday.getShift().equals("Sick")) {
                        if (manHoursInformationOfMonth.getTimeShifts() == null)
                            manHoursInformationOfMonth.setTimeShifts(getMinutesShift(workday));
                        else
                            manHoursInformationOfMonth.setTimeShifts(manHoursInformationOfMonth.getTimeShifts()
                                    + getMinutesShift(workday));
                    }
                    if (workday.getAmountAdditionalMinutes() != null) {
                        if (manHoursInformationOfMonth.getAdditionalTime() == null)
                            manHoursInformationOfMonth.setAdditionalTime(workday.getAmountAdditionalMinutes());
                        else
                            manHoursInformationOfMonth.setAdditionalTime((manHoursInformationOfMonth.getAdditionalTime()
                                    + workday.getAmountAdditionalMinutes()));
                    }
                }
            }
        }
       return manHoursInformationOfMonth;
    }

    private Integer getMinutesShift(WorkDay workDay){

        switch (Integer.parseInt(workDay.getShift())){
            case 1:
                if(workDay.getAmountMinutes()!=null && workDay.getAmountMinutes()!=0)
                    return workDay.getAmountMinutes();
                else
                return timeOfShift1;

            case 2:
                if(workDay.getAmountMinutes()!=null && workDay.getAmountMinutes()!=0)
                    return workDay.getAmountMinutes();
                else
                    return timeOfShift2;
            case 3:
                if(workDay.getAmountMinutes()!=null && workDay.getAmountMinutes()!=0)
                    return workDay.getAmountMinutes();
                else
                    return timeOfShift3;

        }
        return 0;
    }

    public interface ManHoursInformationObserver{
        public void ShowManHoursInformationOfMonth(ManHoursInformationOfMonth manHoursInformationOfMonth);
        public void ShowCharts(ArrayList<ManHoursInformationOfMonth> manHoursInformationOfYear);
    }
}
