package com.appoftatar.workgroupcalendar.ui.workcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.SalaryInformationActivity;
import com.appoftatar.workgroupcalendar.adapters.CalendarAdapter;
import com.appoftatar.workgroupcalendar.adapters.WorkCalendarAdapter;
import com.appoftatar.workgroupcalendar.calendar.MonthWorkCalendar;
import com.appoftatar.workgroupcalendar.calendar.WorkDay;
import com.appoftatar.workgroupcalendar.models.User;
import com.appoftatar.workgroupcalendar.ui.calendar.CalendarFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class WorkCalendarFragment extends Fragment {

    private RecyclerView calendarList;
    private WorkCalendarAdapter workCalendarAdapter;
    private TextView tvMonth;
    private TextView tvAmountHours;
    private TextView tvPayment;
    private TextView tvTitlePay;
    private Button btnPreviosMonth;
    private Button btnNextMonth;
    private Button btnShowDetails;
    private Integer usedMonth;
    private int numberCurrentMonth;
    private DatabaseReference rootDataBase;
    private MonthWorkCalendar monthWorkCalendar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_workcalendar, container, false);
        calendarList = root.findViewById(R.id.rv_workdays);
        tvMonth = root.findViewById(R.id.tvMonth);
        tvAmountHours = root.findViewById(R.id.tvAmountHours);
        tvPayment = root.findViewById(R.id.tvPayment);
        tvTitlePay = root.findViewById(R.id.tvTitlePay);

        btnShowDetails = root.findViewById(R.id.btnShowDetails);
        btnShowDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getContext(), SalaryInformationActivity.class);
               startActivity(intent);

            }
        });

        btnPreviosMonth = root.findViewById(R.id.btnPreviosMonth);
        btnPreviosMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    usedMonth -= 1;
                    monthWorkCalendar = new MonthWorkCalendar(usedMonth);
                    monthWorkCalendar.createWorkCalendar(WorkCalendarFragment.this);

            }
        });
        btnNextMonth = root.findViewById(R.id.btnNextMonth);
        btnNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (usedMonth + 1 <= numberCurrentMonth) {
                usedMonth += 1;
                monthWorkCalendar = new MonthWorkCalendar(usedMonth);
                monthWorkCalendar.createWorkCalendar(WorkCalendarFragment.this);
            }
            }
        });


        //DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        numberCurrentMonth = date.getMonth();
        monthWorkCalendar = new MonthWorkCalendar(date.getMonth());
        usedMonth = date.getMonth();

        if(Common.currentUser == null) {
            getCurrentUser();
        }
        else
            monthWorkCalendar.createWorkCalendar(WorkCalendarFragment.this);

        if(Common.currentUser.additionalInformation == null || Common.currentUser.additionalInformation.getMinutesShift1()==0
                                                                && Common.currentUser.additionalInformation.getMinutesShift2()==0
                                                                && Common.currentUser.additionalInformation.getMinutesShift3()==0){
           LinearLayout llTime = (LinearLayout)root.findViewById(R.id.llTime);
           LinearLayout llPayment = (LinearLayout)root.findViewById(R.id.llPayment);
           LinearLayout llMessage = (LinearLayout)root.findViewById(R.id.llMessage);

            llPayment.setVisibility(View.GONE);
            llTime.setVisibility(View.GONE);
            llMessage.setVisibility(View.VISIBLE);
           TextView tvMessage = root.findViewById(R.id.tvMessage);
            tvMessage.setText(getString(R.string.tv_enterdata));
        }

        return root;
    }
    public static WorkCalendarFragment getInstance(){
        Bundle args = new Bundle();
        WorkCalendarFragment fragment = new WorkCalendarFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public void initRecyclerViewCalendar(){

        tvMonth.setText(monthWorkCalendar.getName());
         //create list for 7 colums
        int coutColumsInList = 7;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),coutColumsInList);

        //put gridManager to recyclerview
        calendarList.setLayoutManager(gridLayoutManager);
        calendarList.setHasFixedSize(true);

        //create new adapter
        workCalendarAdapter = new WorkCalendarAdapter(getContext(),monthWorkCalendar);

        //put adapter to recyclerview
        calendarList.setAdapter(workCalendarAdapter);
    }

    public void showDataSalary(){
        Integer minutes = 0;
        Integer addminutes = 0;
        Float money = 0f;
        Float addMoney = 0f;

      for(WorkDay workDay: monthWorkCalendar.getWorkDaysWithShifts()){
          if(workDay.getAmountMinutes()!=null) {
             if(workDay.getShift()!= null && !workDay.getShift().equals("Vacation")) {
                  minutes += workDay.getAmountMinutes();
             }
          }
          if(workDay.getAmountAdditionalMinutes()!=null)
              addminutes += workDay.getAmountAdditionalMinutes();
          if(workDay.getMoneyOfShift()!=null)
              money +=workDay.getAmountMinutes() * workDay.getMoneyOfShift();
          if(workDay.getMoneyOfAdditionalTime()!=null)
              addMoney += workDay.getMoneyOfAdditionalTime();
      }
       tvAmountHours.setText((minutes + addminutes) / 60 +"h. "+ (minutes + addminutes) % 60+"m.");
       tvPayment.setText(Float.toString(money+ addMoney));
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
                        monthWorkCalendar.createWorkCalendar(WorkCalendarFragment.this);
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
