package com.appoftatar.workgroupcalendar.presenters;

import android.util.Log;

import androidx.annotation.NonNull;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.calendar.MonthWorkCalendar;
import com.appoftatar.workgroupcalendar.calendar.WorkDay;
import com.appoftatar.workgroupcalendar.models.User;
import com.appoftatar.workgroupcalendar.ui.workcalendar.WorkCalendarFragment;
import com.appoftatar.workgroupcalendar.views.WorkCalendarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WorkCalendarPresenter {

    private WorkCalendarView view;

    private Integer usedMonth;
    private int numberCurrentMonth;
    private DatabaseReference rootDataBase;
    private MonthWorkCalendar monthWorkCalendar;


    public WorkCalendarPresenter(WorkCalendarView view) {
        this.view = view;
        numberCurrentMonth = new Date().getMonth();
        monthWorkCalendar = new MonthWorkCalendar(numberCurrentMonth);
        usedMonth = numberCurrentMonth;

    }

    public void createCalendar(){
        if(Common.currentUser == null) {
            getCurrentUser();
        }
        else
            getWorkerDays();
    }

    private void getWorkerDays(){
        view.showProgressBar();
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(monthWorkCalendar.getListDatesForWorkMonth().get(15));
        root.child("workdays").child(Integer.toString(calendar.getTime().getYear()+1900))
                .child(Integer.toString(usedMonth)).addValueEventListener(new ValueEventListener() {
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
                monthWorkCalendar.setWorkerDaysToCalendar(workDaysList);
                view.showCalendar(monthWorkCalendar);
                view.showDataSalary(monthWorkCalendar);
                view.hideProgressBar();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });


    }

    private void getCurrentUser(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        rootDataBase = FirebaseDatabase.getInstance().getReference();

        rootDataBase.child("users").orderByChild("ID").equalTo(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if(user!=null) {
                        Common.currentUser = user;
                        getWorkerDays();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });
    }


    public void getPreviosMonth() {
        usedMonth -= 1;
        monthWorkCalendar.setNumberMonth(usedMonth);
        getWorkerDays();

    }


    public void getNextMonth() {
        if (usedMonth + 1 <= numberCurrentMonth) {
            usedMonth += 1;
            monthWorkCalendar.setNumberMonth(usedMonth);
            getWorkerDays();

        }

    }
}
