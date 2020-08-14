package com.appoftatar.workgroupcalendar.presenters;

import android.util.Log;

import androidx.annotation.NonNull;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.calendar.MonthWorkCalendar;
import com.appoftatar.workgroupcalendar.models.User;
import com.appoftatar.workgroupcalendar.models.UserInVacation;
import com.appoftatar.workgroupcalendar.models.UserSick;
import com.appoftatar.workgroupcalendar.views.CalendarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import javax.inject.Inject;

public class CalendarPresenter {

    private CalendarView view;

    @Inject
     DatabaseReference rootDataBase;
    @Inject
    FirebaseAuth mAuth;

    private Integer usedMonth;
    private int numberCurrentMonth;
    private MonthWorkCalendar monthWorkCalendar;

    @Inject
    public CalendarPresenter(CalendarView view) {
        this.view = view;

        numberCurrentMonth = new Date().getMonth();
        monthWorkCalendar = new MonthWorkCalendar(numberCurrentMonth);
        usedMonth = numberCurrentMonth;
    }

    public void createNewCalendar(){
        view.showProgressBar();
        if(Common.currentUser == null)
            getCurrentUser();
        else
            fillingCalendarWithData();
    }

    public void showPreviosMonth() {
        if (usedMonth - 1 >= numberCurrentMonth) {
            usedMonth -= 1;
            monthWorkCalendar = new MonthWorkCalendar(usedMonth);
            fillingCalendarWithData();
        }
    }

    public void showNextMonth() {
        usedMonth += 1;
        monthWorkCalendar = new MonthWorkCalendar(usedMonth);
        fillingCalendarWithData();
    }

    private void fillingCalendarWithData(){
        getWorkerOnVacationFromDataBase();
        getWorkerSickFromDataBase();
    }

    private void getWorkerOnVacationFromDataBase(){

        //rootDataBase = FirebaseDatabase.getInstance().getReference();
        String managerID,currentGroup;
        if(Common.manager){
            managerID = Common.currentUser.ID;
            currentGroup = Common.currentGroup;
        }else {
            managerID = Common.currentUser.IdManager;
            currentGroup = Common.currentUser.IdWorkGroup;
        }

        rootDataBase.child("vacations").child(managerID).child(currentGroup).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                monthWorkCalendar.clearListEmployesInVacation();
                for (DataSnapshot requestSnapshot: dataSnapshot.getChildren()) {

                    UserInVacation userInVacation = new UserInVacation();
                    userInVacation.setUserID(requestSnapshot.child("userID").getValue().toString());
                    userInVacation.setUserFullName(requestSnapshot.child("userFullName").getValue().toString());


                    for (DataSnapshot itemSnapshot : requestSnapshot.child("dateList").getChildren())
                        userInVacation.setDateToList(itemSnapshot.getValue(Date.class));


                    monthWorkCalendar.setWorkerOnVacation(userInVacation);

                }
                view.showCalendar(monthWorkCalendar);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });

    }

    private void getWorkerSickFromDataBase(){

        //rootDataBase = FirebaseDatabase.getInstance().getReference();
        String managerID,currentGroup;
        if(Common.manager){
            managerID = Common.currentUser.ID;
            currentGroup = Common.currentGroup;
        }else {
            managerID = Common.currentUser.IdManager;
            currentGroup = Common.currentUser.IdWorkGroup;
        }

        rootDataBase.child("sick").child(managerID).child(currentGroup).orderByChild("amountDays").equalTo("0").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                monthWorkCalendar.clearListWorkersSick();
                for (DataSnapshot requestSnapshot: dataSnapshot.getChildren()) {

                    UserSick userSick = requestSnapshot.getValue(UserSick.class);

                    if(userSick != null)
                        monthWorkCalendar.setWorkerSick(userSick);

                }
                view.showCalendar(monthWorkCalendar);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });

    }

    private void getCurrentUser(){
       // FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = mAuth.getInstance().getCurrentUser();
        rootDataBase = FirebaseDatabase.getInstance().getReference();

        rootDataBase.child("users").orderByChild("ID").equalTo(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if(user!=null) {
                        Common.currentUser = user;
                        fillingCalendarWithData();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });
    }

}
