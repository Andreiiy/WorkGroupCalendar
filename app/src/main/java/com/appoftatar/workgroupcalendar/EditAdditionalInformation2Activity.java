package com.appoftatar.workgroupcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.models.AdditionalInformation;
import com.appoftatar.workgroupcalendar.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditAdditionalInformation2Activity extends AppCompatActivity {
private EditText ethour1,ethour2,ethour3;
private EditText etMinute1,etMinute2,etMinute3;
private EditText etPay1,etPay2,etPay3;
private Button btnSave;
private User currentUser;
    private String userId;
    private DatabaseReference rootDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_additional_information2);

        //region================ I N I T     V I E W =========================
        ethour1 = (EditText)findViewById(R.id.etHour1);
        ethour2 = (EditText)findViewById(R.id.etHour2);
        ethour3 = (EditText)findViewById(R.id.etHour3);

        etMinute1 = (EditText)findViewById(R.id.etMinute1);
        etMinute2 = (EditText)findViewById(R.id.etMinute2);
        etMinute3 = (EditText)findViewById(R.id.etMinute3);

        etPay1 = (EditText)findViewById(R.id.etPay1);
        etPay2 = (EditText)findViewById(R.id.etPay2);
        etPay3 = (EditText)findViewById(R.id.etPay3);

         btnSave = (Button) findViewById(R.id.btnSave);

        //endregion


        Intent intent = getIntent();
        userId = intent.getStringExtra("USER_ID");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdditionalInformation additionalInformation = new AdditionalInformation();
                Integer minutes1 = Integer.parseInt(ethour1.getText().toString())*60;
                Integer minutes2 = Integer.parseInt(etMinute1.getText().toString());


                additionalInformation.setMinutesShift1(Integer.parseInt(ethour1.getText().toString())*60 + Integer.parseInt(etMinute1.getText().toString()));
                additionalInformation.setMinutesShift2(Integer.parseInt(ethour2.getText().toString())*60 + Integer.parseInt(etMinute2.getText().toString()));
                additionalInformation.setMinutesShift3(Integer.parseInt(ethour3.getText().toString())*60 + Integer.parseInt(etMinute3.getText().toString()));

                additionalInformation.setPaymentOfMinuteShift1(Float.parseFloat(etPay1.getText().toString())/60);
                additionalInformation.setPaymentOfMinuteShift2(Float.parseFloat(etPay2.getText().toString())/60);
                additionalInformation.setPaymentOfMinuteShift3(Float.parseFloat(etPay3.getText().toString())/60);

                saveInformation(additionalInformation);
            }
        });

        getInformation();
    }


    private void saveInformation(final AdditionalInformation additionalInformation){
        rootDataBase = FirebaseDatabase.getInstance().getReference();
        rootDataBase.child("users").orderByChild("ID").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = new User();
                    user.ID = userSnapshot.child("ID").getValue().toString();
                    user.Email = userSnapshot.child("Email").getValue().toString();
                    user.FirstName = userSnapshot.child("FirstName").getValue().toString();
                    user.IdManager = userSnapshot.child("IdManager").getValue().toString();
                    user.IdWorkGroup = userSnapshot.child("IdWorkGroup").getValue().toString();
                    user.Manager = userSnapshot.child("Manager").getValue().toString();
                    user.Password = userSnapshot.child("Password").getValue().toString();
                    user.SurName = userSnapshot.child("SurName").getValue().toString();
                    user.Telefon = userSnapshot.child("Telefon").getValue().toString();
                    user.UserSik = userSnapshot.child("UserSik").getValue().toString();
                    user.additionalInformation = userSnapshot.child("additionalInformation").getValue(AdditionalInformation.class);
                    user.additionalInformation = additionalInformation;

                    dataSnapshot.getRef().child(userId).setValue(user);
                }
                Intent intent;
                if(Common.manager) {
                     intent = new Intent(getApplicationContext(),ManagerHomeActivity.class);
                } else {
                     intent = new Intent(getApplicationContext(),EmployeeHomeActivity.class);
                }
                startActivity(intent);
                finish();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void getInformation(){
        rootDataBase = FirebaseDatabase.getInstance().getReference();
        rootDataBase.child("users").orderByChild("ID").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = new User();
                    user.ID = userSnapshot.child("ID").getValue().toString();
                    user.Email = userSnapshot.child("Email").getValue().toString();
                    user.FirstName = userSnapshot.child("FirstName").getValue().toString();
                    user.IdManager = userSnapshot.child("IdManager").getValue().toString();
                    user.IdWorkGroup = userSnapshot.child("IdWorkGroup").getValue().toString();
                    user.Manager = userSnapshot.child("Manager").getValue().toString();
                    user.Password = userSnapshot.child("Password").getValue().toString();
                    user.SurName = userSnapshot.child("SurName").getValue().toString();
                    user.Telefon = userSnapshot.child("Telefon").getValue().toString();
                    user.UserSik = userSnapshot.child("UserSik").getValue().toString();
                    user.additionalInformation = userSnapshot.child("additionalInformation").getValue(AdditionalInformation.class);
                    currentUser =  user;

                    setDataToView(currentUser.additionalInformation);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void setDataToView(AdditionalInformation additionalInformation){
        if(additionalInformation!=null) {
            ethour1.setText(Integer.toString(additionalInformation.getMinutesShift1() / 60));
            ethour2.setText(Integer.toString(additionalInformation.getMinutesShift2() / 60));
            ethour3.setText(Integer.toString(additionalInformation.getMinutesShift3() / 60));

            etMinute1.setText(Integer.toString(additionalInformation.getMinutesShift1() % 60));
            etMinute2.setText(Integer.toString(additionalInformation.getMinutesShift2() % 60));
            etMinute3.setText(Integer.toString(additionalInformation.getMinutesShift3() % 60));

            etPay1.setText(Float.toString(additionalInformation.getPaymentOfMinuteShift1() * 60));
            etPay2.setText(Float.toString(additionalInformation.getPaymentOfMinuteShift2() * 60));
            etPay3.setText(Float.toString(additionalInformation.getPaymentOfMinuteShift3() * 60));
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
