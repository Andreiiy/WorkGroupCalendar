package com.appoftatar.workgroupcalendar.ui.remainder_calendar;

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
import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.adapters.CalendarAdapter;
import com.appoftatar.workgroupcalendar.adapters.RemainderCalendarAdapter;
import com.appoftatar.workgroupcalendar.calendar.MonthWorkCalendar;
import com.appoftatar.workgroupcalendar.calendar.MyCalendar;
import com.appoftatar.workgroupcalendar.calendar.WorkDay;
import com.appoftatar.workgroupcalendar.calendar.WorkDayWithRemainder;
import com.appoftatar.workgroupcalendar.models.User;
import com.appoftatar.workgroupcalendar.models.UserInVacation;
import com.appoftatar.workgroupcalendar.models.UserSick;
import com.appoftatar.workgroupcalendar.ui.calendar.CalendarFragment;
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
import java.util.GregorianCalendar;
import java.util.Locale;


public class FragmentRemainderCalendar extends Fragment {

        private RecyclerView calendarList;
        private RemainderCalendarAdapter calendarAdapter;
        private TextView tvMonth;
        private Button btnPreviosMonth;
        private Button btnNextMonth;
        private Integer usedMonth;
        private int numberCurrentMonth;
        //  private MyCalendar myCalendar;
        private DatabaseReference rootDataBase;
        private ArrayList<WorkDayWithRemainder> monthWithRemainders;
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            View root = inflater.inflate(R.layout.fragment_reminder_calendar, container, false);

            tvMonth = (TextView)root.findViewById(R.id.tvMonth);
            btnPreviosMonth = (Button)root.findViewById(R.id.btnPreviosMonth);
            btnPreviosMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (usedMonth - 1 >= numberCurrentMonth) {
                        usedMonth -= 1;
                        createCalendar();
                        initRecyclerViewCalendar();
                        fillingCalendarWithData();
                    }
                }
            });
            btnNextMonth = (Button)root.findViewById(R.id.btnNextMonth);
            btnNextMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    usedMonth += 1;
                    createCalendar();
                    initRecyclerViewCalendar();
                    fillingCalendarWithData();

                }
            });
            calendarList = root.findViewById(R.id.rv_remainderCalendar);


            Date date = new GregorianCalendar().getTime();
            numberCurrentMonth = date.getMonth();
            usedMonth = date.getMonth();
            createCalendar();
            initRecyclerViewCalendar();
            fillingCalendarWithData();

            return root;
        }


    public static FragmentRemainderCalendar getInstance(){
            Bundle args = new Bundle();
            FragmentRemainderCalendar fragment = new FragmentRemainderCalendar();
            fragment.setArguments(args);

            return fragment;
        }

    private void initRecyclerViewCalendar(){

        Calendar calendar = Calendar.getInstance();
        Date date = new GregorianCalendar(new Date().getYear(), usedMonth, 1).getTime();
        calendar.setTime(date);
        String nameMonth = calendar.getDisplayName(calendar.MONTH,Calendar.LONG, Locale.ENGLISH);

        tvMonth.setText(nameMonth);



            int coutColumsInList = 7;
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),coutColumsInList);

            //put gridManager to recyclerview
            calendarList.setLayoutManager(gridLayoutManager);
            calendarList.setHasFixedSize(true);

            //create new adapter
            calendarAdapter = new RemainderCalendarAdapter(getContext(),monthWithRemainders);

            //put adapter to recyclerview
            calendarList.setAdapter(calendarAdapter);
        }

    private void fillingCalendarWithData(){
            getMonthWithRemainders();
        }

    public void getMonthWithRemainders(){
            DatabaseReference   root = FirebaseDatabase.getInstance().getReference();
            Calendar calendar = Calendar.getInstance();

            root.child("remainders").child(Integer.toString(calendar.getTime().getYear()+1900))
                    .child(Integer.toString(usedMonth)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<WorkDayWithRemainder> monthWithRemainders = new ArrayList<>();
                    for (DataSnapshot dateSnapshot: dataSnapshot.getChildren()) {
                        ArrayList<WorkDayWithRemainder>  listReminders = new ArrayList<>();
                         for (DataSnapshot remaindSnapshot: dateSnapshot.child(Common.currentUser.ID).getChildren()) {
                            WorkDayWithRemainder workDayWithRemainder = remaindSnapshot.getValue(WorkDayWithRemainder.class);
                             listReminders.add(workDayWithRemainder);

                        }
                        if (listReminders.size() != 0)
                            monthWithRemainders.add(listReminders.get(0));
                    }
                    if(monthWithRemainders.size() != 0 && monthWithRemainders != null)
                       setRemindsToCalendar(monthWithRemainders);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("Data base", "Failed to read value.", databaseError.toException());
                }
            });
        }

    private void createCalendar() {
        MyCalendar myCalendar = new MyCalendar();
        myCalendar.getMonth(usedMonth);
        monthWithRemainders = new ArrayList<>();
        ArrayList<Date> month = myCalendar.getMonth(usedMonth);
        for (int i = 0; i < month.size(); i++){
            WorkDayWithRemainder day = new WorkDayWithRemainder();
            day.setDate(month.get(i).getDate());
            day.setMonth(month.get(i).getMonth());
            day.setYear(month.get(i).getYear()+1900);
            monthWithRemainders.add(day);
        }


    }

    private void setRemindsToCalendar(ArrayList<WorkDayWithRemainder> month) {
            MyCalendar myCalendar = new MyCalendar();
            myCalendar.getMonth(usedMonth);

            for (WorkDayWithRemainder dayWithRemainder: monthWithRemainders){

                if(getRemainder(dayWithRemainder.getDate(),month) != null){
                    WorkDayWithRemainder workDayWithRemainder = getRemainder(dayWithRemainder.getDate(),month);
                    dayWithRemainder.setHours(workDayWithRemainder.getHours());
                    dayWithRemainder.setMinutes(workDayWithRemainder.getMinutes());
                    dayWithRemainder.setEvent(workDayWithRemainder.getEvent());
                }

            }

            initRecyclerViewCalendar();
        }

    private WorkDayWithRemainder getRemainder(int date,ArrayList<WorkDayWithRemainder> month){
            if(month != null && month.size() != 0) {
                for (WorkDayWithRemainder day:month){
                    if(day.getDate() == date)
                        return day;
                }
            }
            return null;
        }

//    @Override
//    public void onPause() {
//        super.onPause();
//        Common.aplicationVisible = false;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Common.aplicationVisible = true;
//    }
}
