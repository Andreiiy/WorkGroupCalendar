package com.appoftatar.workgroupcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.calendar.WorkDayWithRemainder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class InformationDayOfCalendarRemaindesActivity extends AppCompatActivity {
private TextView tv_weekday;
private TextView tv_date;
private TextView tv_titleEvents;
private ListView listReminders;
private Button btnAddRemainder;
private String date;
private String month;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_day_of_calendar_remaindes);

        Intent intent = getIntent();


        tv_weekday = (TextView)findViewById(R.id.tv_weekday);
        tv_date = (TextView)findViewById(R.id.tv_date);
        tv_titleEvents = (TextView)findViewById(R.id.tv_titleEvents);
        listReminders = (ListView) findViewById(R.id.listReminders);
        btnAddRemainder = (Button) findViewById(R.id.btnAddRemainder);
        btnAddRemainder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AddReminderActivity.class);
                intent.putExtra("DAY_WEEK",intent.getStringExtra("DAY_WEEK"));
                intent.putExtra("DATE", date);
                intent.putExtra("MONTH", month);
                startActivity(intent);
                finish();
            }
        });

        tv_weekday.setText(intent.getStringExtra("DAY_WEEK"));

        month = intent.getStringExtra("MONTH");
        date = intent.getStringExtra("DATE");
        tv_date.setText(intent.getStringExtra("DATE"));

        getEvents();
    }


    private void getEvents(){

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        Calendar calendar = Calendar.getInstance();
        root.child("remainders").child(Integer.toString(calendar.getTime().getYear()+1900))
                .child(month).child(date).child(Common.currentUser.ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<WorkDayWithRemainder> monthWithRemainders = new ArrayList<>();
                for (DataSnapshot dateSnapshot: dataSnapshot.getChildren()) {
                    WorkDayWithRemainder workDayWithRemainder = new WorkDayWithRemainder();
                    workDayWithRemainder =  dateSnapshot.getValue(WorkDayWithRemainder.class);


                    if(workDayWithRemainder != null)
                        monthWithRemainders.add(workDayWithRemainder);

                }
                if(monthWithRemainders.size() != 0 && monthWithRemainders != null){
                    ArrayList<String> reminders = new ArrayList<>();
                    String time = "";
                    for(WorkDayWithRemainder day: monthWithRemainders){
                        String hours = Integer.toString(day.getHours());
                        String minutes = Integer.toString(day.getMinutes());
                        if(day.getHours()<10) hours ="0"+day.getHours();
                        if(day.getMinutes()<10) minutes ="0"+day.getMinutes();
                        time = hours+":"+minutes;

                        reminders.add(day.getEvent()+"\n"+time);
                    }
                        createListReminders( reminders, listReminders);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });


    }


    private void createListReminders(ArrayList<String> reminders, ListView rvList){

        //ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,lEmployes);

        ArrayAdapter<String> adapter = new ArrayAdapter(this,R.layout.my_simple_list_item,reminders);
        rvList.setAdapter(adapter);

    }

    @Override
    public void onPause() {
        super.onPause();
        Common.aplicationVisible = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        Common.aplicationVisible = true;
    }
}
