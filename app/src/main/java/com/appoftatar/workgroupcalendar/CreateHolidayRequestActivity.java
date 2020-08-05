package com.appoftatar.workgroupcalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appoftatar.workgroupcalendar.models.HolidayRequest;
import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.adapters.CalendarRequestsAdapter;
import com.appoftatar.workgroupcalendar.calendar.MyCalendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class CreateHolidayRequestActivity extends AppCompatActivity implements View.OnClickListener {
private RecyclerView rvCalendarCreateRequest;
private TextView tvMonth;
    private Button btnPreviosMonth;
    private Button btnNextMonth;
    private Button btnCreateRequest;
    private MyCalendar myCalendar;
    private ArrayList<Date> usedMonth;
    private Integer numberCurrentMonth;
    private CalendarRequestsAdapter calendarAdapter;
    private Spinner  sReasonsHoliday;
    private HolidayRequest holidayRequest;

    //private String [] reasons  = getBaseContext().getResources().getStringArray(R.array.spiner_reasons);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_holiday_request);



        holidayRequest = new HolidayRequest();
        holidayRequest.setId(UUID.randomUUID().toString());
        holidayRequest.setIdEmployee(Common.currentUser.ID);
        holidayRequest.setNameEmployee(Common.currentUser.FirstName.charAt(0)+"."+Common.currentUser.SurName);
        holidayRequest.setViewEmployee("false");
        holidayRequest.setAnswerRequest("0");
        holidayRequest.setRejectionReason("0");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        holidayRequest.setDate(dateFormat.format(date));
        Common.holidayRequest = holidayRequest;

        ArrayAdapter<String> reasonsHolidayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.spiner_reasons));
        reasonsHolidayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sReasonsHoliday = (Spinner) findViewById(R.id.sReasons);
        sReasonsHoliday.setAdapter(reasonsHolidayAdapter);
        sReasonsHoliday.setOnItemSelectedListener(onItemSelectedListener());
        sReasonsHoliday.setSelection(0);

        btnPreviosMonth = (Button)findViewById(R.id.btnPreviosMonth);
        btnPreviosMonth.setOnClickListener(this);
        btnNextMonth = (Button)findViewById(R.id.btnNextMonth);
        btnNextMonth.setOnClickListener(this);
        btnCreateRequest = (Button)findViewById(R.id.btnCreateRequest);
        Bitmap bitmap = Common.decodeSampledBitmapFromResource(getResources(),R.drawable.btnred,150,50);
        if(android.os.Build.VERSION.SDK_INT < 16) {
            btnCreateRequest.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
        }
        else {
            btnCreateRequest.setBackground(new BitmapDrawable(getResources(),bitmap));
        }
        btnCreateRequest.setOnClickListener(this);
        rvCalendarCreateRequest = (RecyclerView)findViewById(R.id.rvCalendarCreateRequest);
        tvMonth = (TextView)findViewById(R.id.tvMonth);


        myCalendar = new MyCalendar();
        usedMonth = myCalendar.getCurrentMonth();
        numberCurrentMonth = usedMonth.get(15).getMonth();
        initRecyclerViewCalendar(usedMonth);

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Bitmap bitmap = Common.decodeSampledBitmapFromResource(getResources(),R.drawable.btnred,btnCreateRequest.getWidth(),btnCreateRequest.getHeight());
        if(android.os.Build.VERSION.SDK_INT < 16) {
            btnCreateRequest.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
        }
        else {
            bitmap = Common.decodeSampledBitmapFromResource(getResources(), R.drawable.btnred, btnCreateRequest.getWidth(), btnCreateRequest.getHeight());
            btnCreateRequest.setBackground(new BitmapDrawable(getResources(), bitmap));
        }

    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnNextMonth){
            Date usedDate = usedMonth.get(15);
            usedMonth = myCalendar.getMonth(usedDate.getMonth() + 1);
            initRecyclerViewCalendar(usedMonth);


        }else if(v.getId() == R.id.btnPreviosMonth) {
            Date usedDate = usedMonth.get(15);
            if (usedDate.getMonth() - 1 >= numberCurrentMonth){
                usedMonth = myCalendar.getMonth(usedDate.getMonth() - 1);
                initRecyclerViewCalendar(usedMonth);
        }

        }else if(v.getId() == R.id.btnCreateRequest){
         if( Common.holidayRequest.getDateList().size() != 0) {
             sortingOfReceivedDates();
             Intent intent = new Intent(this, FormHolidayRequestActivity.class);
             startActivity(intent);
             finish();
         }else {
             Toast.makeText(this,"You have to choose the days on the calendar",Toast.LENGTH_LONG).show();
         }
        }
    }

    private void initRecyclerViewCalendar(ArrayList<Date> month){
        usedMonth = month;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(usedMonth.get(15));
        String nameMonth =calendar.getDisplayName(calendar.MONTH,Calendar.LONG, Locale.ENGLISH);
        //String month = new SimpleDateFormat("MMM").format(curentMonth.get(15));
        tvMonth.setText(nameMonth);

        int coutColumsInList = 7;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,coutColumsInList);

        //put gridManager to recyclerview
        rvCalendarCreateRequest.setLayoutManager(gridLayoutManager);
        rvCalendarCreateRequest.setHasFixedSize(true);

        //create new adapter
        calendarAdapter = new CalendarRequestsAdapter(usedMonth,
                myCalendar.getDayOfWeekOfFirstDayOfMonth(usedMonth.get(15).getMonth()));

        //put adapter to recyclerview
        rvCalendarCreateRequest.setAdapter(calendarAdapter);
    }

    private AdapterView.OnItemSelectedListener onItemSelectedListener(){

        return  new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Common.holidayRequest.setReasonRequest(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
}

    private void sortingOfReceivedDates(){
    ArrayList<Date> dates =new ArrayList<>();
            for(Date item :Common.holidayRequest.getDateList().values())
                dates.add(item);

        ArrayList<Date> datesToRequest =new ArrayList<>();
            if(dates.size()!=1) {
                Collections.sort(dates);
                Date firstDate = dates.get(0);
                Date lastDate = dates.get(dates.size() - 1);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dates.get(0));
                datesToRequest.add(firstDate);
                calendar.add(Calendar.DAY_OF_YEAR, 1);

                while (calendar.getTime().compareTo(lastDate) != 0) {
                    datesToRequest.add(calendar.getTime());
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }
                datesToRequest.add(lastDate);
            }else datesToRequest.add(dates.get(0));

        HashMap<String,Date> datesHoliday = new HashMap<>();
      Integer  index = 0;
        for(Date date: datesToRequest){
            datesHoliday.put(index.toString(),date);
            index++;
        }

        Common.holidayRequest.setDateList(datesHoliday);

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
