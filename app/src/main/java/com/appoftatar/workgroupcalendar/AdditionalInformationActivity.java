package com.appoftatar.workgroupcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.models.AdditionalInformation;
import com.appoftatar.workgroupcalendar.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdditionalInformationActivity extends AppCompatActivity {

    private Button btnEdit;
    private String userId;
    private TextView tvtime1,tvtime2,tvtime3;
    private TextView tvpay1,tvpay2,tvpay3;
    private DatabaseReference rootDataBase;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_information);

        tvtime1 = (TextView)findViewById(R.id.tvtime1);
        tvtime2 = (TextView)findViewById(R.id.tvtime2);
        tvtime3 = (TextView)findViewById(R.id.tvtime3);

        tvpay1 = (TextView)findViewById(R.id.tvpay1);
        tvpay2 = (TextView)findViewById(R.id.tvpay2);
        tvpay3 = (TextView)findViewById(R.id.tvpay3);



        Intent intent = getIntent();
        userId = intent.getStringExtra("USER_ID");

        btnEdit=(Button)findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), EditAdditionalInformation2Activity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });

        getInformation();
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
            tvtime1.setText(Integer.toString(additionalInformation.getMinutesShift1() / 60)+"."+additionalInformation.getMinutesShift1() % 60);
            tvtime2.setText(Integer.toString(additionalInformation.getMinutesShift2() / 60)+"."+additionalInformation.getMinutesShift2() % 60);
            tvtime3.setText(Integer.toString(additionalInformation.getMinutesShift3() / 60)+"."+additionalInformation.getMinutesShift3() % 60);

            tvpay1.setText(Float.toString(additionalInformation.getPaymentOfMinuteShift1() * 60));
            tvpay2.setText(Float.toString(additionalInformation.getPaymentOfMinuteShift2() * 60));
            tvpay3.setText(Float.toString(additionalInformation.getPaymentOfMinuteShift3() * 60));
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
