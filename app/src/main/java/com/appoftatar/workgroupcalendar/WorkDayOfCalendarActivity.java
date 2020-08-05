package com.appoftatar.workgroupcalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.calendar.WorkDay;

import java.text.SimpleDateFormat;

public class WorkDayOfCalendarActivity extends AppCompatActivity {
    private WorkDay workDay;
    private TextView tv_weekday;
    private TextView tv_date;
    private TextView tv_numberShift;
    private TextView tv_titleShift;
    private TextView tv_timeShift;
    private TextView tv_paymentShift;
    private TextView tv_Addtime;
    private TextView tv_addPayment;
    private Button btnEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_day_of_calendar);
        tv_weekday = (TextView) findViewById(R.id.tv_weekday);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_numberShift = (TextView)findViewById(R.id.tv_numberShift);
        tv_timeShift = (TextView)findViewById(R.id.tv_timeShift);
        tv_paymentShift = (TextView)findViewById(R.id.tv_paymentShift);
        tv_Addtime = (TextView)findViewById(R.id.tv_Addtime);
        tv_addPayment = (TextView)findViewById(R.id.tv_addPayment);
        tv_titleShift = (TextView)findViewById(R.id.tv_titleShift);

        btnEdit = (Button)findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), FormForEditionalWorkDayActivity.class);
                intent.putExtra(WorkDay.class.getSimpleName(), workDay);
                startActivity(intent);
                finish();
            }
        });

        Bundle arguments = getIntent().getExtras();
        if(arguments!=null) {
            workDay = (WorkDay) arguments.getSerializable(WorkDay.class.getSimpleName());
            SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week abbreviated
            tv_weekday.setText(simpleDateformat.format(workDay.getDate()));
            tv_date.setText(Integer.toString(workDay.getDate().getDate()));

            if(workDay.getShift()!= null && !workDay.getShift().equals("Vacation"))
            tv_numberShift.setText(workDay.getShift());
            else if(workDay.getShift()!= null && workDay.getShift().equals("Vacation")){
                tv_numberShift.setHeight(0);
                tv_titleShift.setText(getString(R.string.tv_vacation));}

            if(workDay.getAmountMinutes()!=null) {
                tv_timeShift.setText(Integer.toString(workDay.getAmountMinutes() / 60) + "." + Integer.toString(workDay.getAmountMinutes() % 60));
                tv_paymentShift.setText(Float.toString(workDay.getAmountMinutes() * workDay.getMoneyOfShift()));
            }
            if(workDay.getAmountAdditionalMinutes()!=null) {
                tv_Addtime.setText(Integer.toString(workDay.getAmountAdditionalMinutes() / 60) + "." + Integer.toString(workDay.getAmountAdditionalMinutes() % 60));
                tv_addPayment.setText(Float.toString(workDay.getMoneyOfAdditionalTime()));
            }
        }

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
