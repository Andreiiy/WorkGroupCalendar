package com.appoftatar.workgroupcalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.appoftatar.workgroupcalendar.Common.Common;

import java.util.ArrayList;

public class InforrmationDayOfCalendar extends AppCompatActivity {
private ListView listEmployesSick;
private ListView listEmployesOnVacation;
private TextView tv_weekday;
private TextView tv_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inforrmation_day_of_calendar);

        listEmployesSick = (ListView)findViewById(R.id.listEmployesSick);
        listEmployesOnVacation = (ListView)findViewById(R.id.listEmployesOnVacation);
        tv_weekday = (TextView) findViewById(R.id.tv_weekday);
        tv_date = (TextView) findViewById(R.id.tv_date);

        Intent intent = getIntent();
       String weekDay = intent.getStringExtra("DAY_WEEK");
       String date = intent.getStringExtra("DATE");
        ArrayList<String>  vacationList =  (ArrayList<String>) getIntent().getSerializableExtra("VacationList");
        ArrayList<String>  sickList =  (ArrayList<String>) getIntent().getSerializableExtra("SickList");

        tv_weekday.setText(weekDay);
        tv_date.setText(date);

      if(vacationList!= null)
          createListEmployes(vacationList,listEmployesOnVacation);
      if(sickList!=null)
          createListEmployes(sickList,listEmployesSick);

    }

    private void createListEmployes(ArrayList<String> lEmployes, ListView rvList){

        //ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,lEmployes);

        ArrayAdapter<String> adapter = new ArrayAdapter(this,R.layout.my_simple_list_item,lEmployes);
        rvList.setAdapter(adapter);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
