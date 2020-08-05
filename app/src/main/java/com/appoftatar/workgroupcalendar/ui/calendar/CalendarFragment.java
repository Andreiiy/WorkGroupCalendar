package com.appoftatar.workgroupcalendar.ui.calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.adapters.CalendarAdapter;
import com.appoftatar.workgroupcalendar.calendar.MonthWorkCalendar;
import com.appoftatar.workgroupcalendar.calendar.MyCalendar;
import com.appoftatar.workgroupcalendar.models.User;
import com.appoftatar.workgroupcalendar.models.UserInVacation;
import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.models.UserSick;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class CalendarFragment extends Fragment {

    private RecyclerView calendarList;
    private CalendarAdapter calendarAdapter;
    private TextView tvMonth;
    private Button btnPreviosMonth;
    private Button btnNextMonth;
    private Integer usedMonth;
    private int numberCurrentMonth;
  //  private MyCalendar myCalendar;
    private DatabaseReference rootDataBase;
    private MonthWorkCalendar monthWorkCalendar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        tvMonth = (TextView)root.findViewById(R.id.tvMonth);
        btnPreviosMonth = (Button)root.findViewById(R.id.btnPreviosMonth);
        btnPreviosMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (usedMonth - 1 >= numberCurrentMonth) {
                    usedMonth -= 1;
                    monthWorkCalendar = new MonthWorkCalendar(usedMonth);
                    fillingCalendarWithData();
                }
            }
        });
        btnNextMonth = (Button)root.findViewById(R.id.btnNextMonth);
        btnNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usedMonth += 1;
                monthWorkCalendar = new MonthWorkCalendar(usedMonth);
                fillingCalendarWithData();

            }
        });
        calendarList = root.findViewById(R.id.rv_numbers);

        //create list for 7 colums
        //DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        numberCurrentMonth = date.getMonth();
        monthWorkCalendar = new MonthWorkCalendar(date.getMonth());
        usedMonth = date.getMonth();
        initRecyclerViewCalendar();
        if(Common.currentUser == null)
            getCurrentUser();
        else
        fillingCalendarWithData();
        return root;
    }
    public static CalendarFragment getInstance(){
        Bundle args = new Bundle();
        CalendarFragment fragment = new CalendarFragment();
        fragment.setArguments(args);

        return fragment;
    }


    private void initRecyclerViewCalendar(){



        tvMonth.setText(monthWorkCalendar.getName());

        int coutColumsInList = 7;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),coutColumsInList);

        //put gridManager to recyclerview
        calendarList.setLayoutManager(gridLayoutManager);
        calendarList.setHasFixedSize(true);

        //create new adapter
        calendarAdapter = new CalendarAdapter(getContext(),monthWorkCalendar);

        //put adapter to recyclerview
        calendarList.setAdapter(calendarAdapter);
    }


    private void fillingCalendarWithData(){
        getWorkerOnVacationFromDataBase();
        getWorkerSickFromDataBase();
    }

    private void getWorkerOnVacationFromDataBase(){

        rootDataBase = FirebaseDatabase.getInstance().getReference();
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
                initRecyclerViewCalendar();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });

    }

    private void getWorkerSickFromDataBase(){

        rootDataBase = FirebaseDatabase.getInstance().getReference();
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
                initRecyclerViewCalendar();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });

    }

    private void getCurrentUser(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

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
