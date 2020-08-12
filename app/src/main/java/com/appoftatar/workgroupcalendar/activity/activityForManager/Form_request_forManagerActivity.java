package com.appoftatar.workgroupcalendar.activity.activityForManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.models.HolidayRequest;
import com.appoftatar.workgroupcalendar.models.UserInVacation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Form_request_forManagerActivity extends AppCompatActivity {
  private   TextView tvNameGroup;
  private   TextView tvName;
  private   TextView tvdateStart;
  private   TextView tvdateFinish;
  private   TextView tvAmountDays;
  private   TextView tvReason;
  private   TextView tvCarrentDate;
  private   TextView tv_request_permision;
  private   Button btnNo;
  private   Button btnYes;
  private   String idRequest;
  private EditText etReasonRefusal;
  private boolean reqСonfirmation = false;

    private ArrayList<String> reasons_en  = new ArrayList<String>(Arrays.asList( "Family vacation","Going to the doctor","Planned vacation",
            "Trip abroad","Training","Feeling unwell","Baby sitting","For relax"));
    private ArrayList<String> reasons_ru  = new ArrayList<String>(Arrays.asList( "Семейные обстоятельства","Поход к доктору","Плановый отпуск",
            "Поездка заграницу","Учеба","Плохое самочувствие","Присмотр за детьми","Отдохнуть"));
    private ArrayList<String> reasons_il  = new ArrayList<String>(Arrays.asList( "חופשה משפחתית","הולך לרופא","חופשה מתוכננת",
            "טיול לחוץ לארץ","לימוד","מרגיש לא טוב","ישיבה תינוקת","לנוח"));
    private DatabaseReference rootDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_request_for_manager);


        tv_request_permision =(TextView)findViewById(R.id.tv_request_permision);
        tvNameGroup = (TextView)findViewById(R.id.tvNameGroup);
        tvName = (TextView)findViewById(R.id.tvName);
        tvdateStart = (TextView)findViewById(R.id.tvdateStart);
        tvdateFinish = (TextView)findViewById(R.id.tvdateFinish);
        tvAmountDays = (TextView)findViewById(R.id.tvAmountDays);
        tvReason = (TextView)findViewById(R.id.tvReason);
        tvCarrentDate = (TextView)findViewById(R.id.tvCarrentDate);
        etReasonRefusal = (EditText) findViewById(R.id.etReasonRefusal);
        btnNo = (Button) findViewById(R.id.btnNo);
        btnYes = (Button) findViewById(R.id.btnYes);

        setDataToViews();

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestNotСonfirmation();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestСonfirmation();
            }
        });

    }

    private void setDataToViews(){

        Intent intent = getIntent();
        tvNameGroup.setText(Common.currentGroup);
        idRequest = intent.getStringExtra("REQUEST_ID");
        tvName.setText(intent.getStringExtra("EMPLOYEE_NAME"));
        tvdateStart.setText(intent.getStringExtra("DATE_START"));
        tvdateFinish.setText(intent.getStringExtra("DATE_FINISH"));
        tvAmountDays.setText(intent.getStringExtra("TOTAL_DAYS"));
        tvCarrentDate.setText(intent.getStringExtra("DATE"));

        tvReason.setText(getReason(intent.getStringExtra("REASON")));

    }

    private void requestСonfirmation(){
        rootDataBase = FirebaseDatabase.getInstance().getReference();
                rootDataBase.child("requests").child(Common.currentUser.ID).child(Common.currentGroup).orderByChild("id").equalTo(idRequest).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot requestSnapshot: dataSnapshot.getChildren()) {
                    HolidayRequest request = new HolidayRequest();
                    request.setId(requestSnapshot.child("id").getValue().toString());
                   request.setIdEmployee(requestSnapshot.child("idEmployee").getValue().toString());
                    request.setNameEmployee(requestSnapshot.child("nameEmployee").getValue().toString());
                   request.setReasonRequest(requestSnapshot.child("reasonRequest").getValue().toString());
                    request.setViewEmployee(requestSnapshot.child("viewEmployee").getValue().toString());
                   request.setAnswerRequest(requestSnapshot.child("answerRequest").getValue().toString());
                    request.setRejectionReason(requestSnapshot.child("rejectionReason").getValue().toString());
                    request.setDate(requestSnapshot.child("date").getValue().toString());


                    for(DataSnapshot itemSnapshot:requestSnapshot.child("dateList").getChildren())
                        request.setDateToList(itemSnapshot.getValue(Date.class));

                    if(requestSnapshot.child("id").getValue().toString().equals(idRequest) ) {
                        requestSnapshot.getRef().child("answerRequest").setValue("true");
                        setUserVacation(request);
                    }


                }
                Intent intent = new Intent(getBaseContext(), ManagerHomeActivity.class);
                startActivity(intent);
                finish();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });

    }

    private void setUserVacation(HolidayRequest request) {
        if(!reqСonfirmation) {
        UserInVacation userInVacation = new UserInVacation();
        userInVacation.setUserID(request.getIdEmployee());
        userInVacation.setUserFullName(request.getNameEmployee());
        userInVacation.setDateList(request.getDateList());

        rootDataBase = FirebaseDatabase.getInstance().getReference();
        rootDataBase.child("vacations").child(Common.currentUser.ID).child(Common.currentGroup).push().setValue(userInVacation);
        reqСonfirmation=true;

        }
    }

    private void requestNotСonfirmation(){

        rootDataBase = FirebaseDatabase.getInstance().getReference();


        rootDataBase.child("requests").child(Common.currentUser.ID).child(Common.currentGroup).orderByChild("id").equalTo(idRequest).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot requestSnapshot: dataSnapshot.getChildren()) {

                    if(requestSnapshot.child("id").getValue().toString().equals(idRequest) ) {
                        requestSnapshot.getRef().child("answerRequest").setValue("false");
                        if(etReasonRefusal.getText().toString()!="")
                            requestSnapshot.getRef().child("rejectionReason").setValue(etReasonRefusal.getText().toString());
                    }


                }
                Intent intent = new Intent(getBaseContext(),ManagerHomeActivity.class);
                startActivity(intent);
                finish();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private String getReason(String reason){
      String stringForCheckLanguage = tv_request_permision.getText().toString();

        if(reason.charAt(1)>='a' && reason.charAt(1)<='z'){
            if(stringForCheckLanguage.charAt(1)>='a' && stringForCheckLanguage.charAt(1)<='z')
                return reason;
            if(stringForCheckLanguage.charAt(1)>='а' && stringForCheckLanguage.charAt(1)<='я') {
                Integer index = reasons_en.indexOf(reason);
                return reasons_ru.get(index);
            }
            else  {
                Integer index = reasons_en.indexOf(reason);
                return reasons_il.get(index);
            }
        }
         ////////////////////////////////////////////////////////////////////////////////////////////
        else if(reason.charAt(1)>='а' && reason.charAt(1)<='я'){
            if(stringForCheckLanguage.charAt(1)>='a' && stringForCheckLanguage.charAt(1)<='z'){
                Integer index = reasons_ru.indexOf(reason);
                return reasons_en.get(index);
            }
            if(stringForCheckLanguage.charAt(1)>='а' && stringForCheckLanguage.charAt(1)<='я') {
                return reason;
            }
            else  {
                Integer index = reasons_ru.indexOf(reason);
                return reasons_il.get(index);
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////
        else {
            if(stringForCheckLanguage.charAt(1)>='a' && stringForCheckLanguage.charAt(1)<='z'){
                Integer index = reasons_il.indexOf(reason);
                   return reasons_en.get(index);
            }
            if(stringForCheckLanguage.charAt(1)>='а' && stringForCheckLanguage.charAt(1)<='я') {
                Integer index = reasons_il.indexOf(reason);
                return reasons_ru.get(index);
            }
            else  {
                return reason;
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
