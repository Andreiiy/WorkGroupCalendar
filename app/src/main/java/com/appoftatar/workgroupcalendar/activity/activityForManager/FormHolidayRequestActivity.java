package com.appoftatar.workgroupcalendar.activity.activityForManager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.activity.activityForEmployee.EmployeeHomeActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormHolidayRequestActivity extends AppCompatActivity {

    TextView tvNameGroup;
    TextView tvName;
    TextView tvdateStart;
    TextView tvdateFinish;
    TextView tvAmountDays;
    TextView tvReason;
    TextView tvCarrentDate;
    TextView linkBack;
    TextView linkSendRequest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_holiday_request);

        tvNameGroup = (TextView)findViewById(R.id.tvNameGroup);
        tvName = (TextView)findViewById(R.id.tvName);
        tvdateStart = (TextView)findViewById(R.id.tvdateStart);
        tvdateFinish = (TextView)findViewById(R.id.tvdateFinish);
        tvAmountDays = (TextView)findViewById(R.id.tvAmountDays);
        tvReason = (TextView)findViewById(R.id.tvReason);
        tvCarrentDate = (TextView)findViewById(R.id.tvCarrentDate);
        linkBack = (TextView)findViewById(R.id.linkBack);
        linkSendRequest = (TextView)findViewById(R.id.linkSendRequest);

        if(Common.holidayRequest!=null){
            tvNameGroup.setText(Common.currentUser.IdWorkGroup);
            tvName.setText(Common.currentUser.FirstName +"  "+Common.currentUser.SurName);

            DateFormat dateFormatStart = new SimpleDateFormat("dd/MM");
            tvdateStart.setText(dateFormatStart.format(Common.holidayRequest.getDateList().get("0")));

            DateFormat dateFormatFinish = new SimpleDateFormat("dd/MM/yyyy");
            tvdateFinish.setText(dateFormatFinish.format(Common.holidayRequest.getDateList().get(Integer.toString(Common.holidayRequest.getDateList().size()-1))));

            tvAmountDays.setText(Integer.toString(Common.holidayRequest.getDateList().size()));

            tvReason.setText(Common.holidayRequest.getReasonRequest());

            Date date = new Date();
            tvCarrentDate.setText(dateFormatFinish.format(date));


        }

        linkBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.holidayRequest = null;
                Intent intent = new Intent(getBaseContext(), EmployeeHomeActivity.class);
                startActivity(intent);
            }
        });

        linkSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestToManager();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Common.holidayRequest=null;
    }

    private void sendRequestToManager(){


        DatabaseReference root = FirebaseDatabase.getInstance().getReference();

        root.child("requests").child(Common.currentUser.IdManager).child(Common.currentUser.IdWorkGroup).push()
                              .setValue(Common.holidayRequest)
                              .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(FormHolidayRequestActivity.this, "Request sent", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getBaseContext(),EmployeeHomeActivity.class);
                startActivity(intent);
                finish();
            }


        });
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
