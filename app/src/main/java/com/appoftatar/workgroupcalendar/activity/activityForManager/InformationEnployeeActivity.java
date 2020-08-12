package com.appoftatar.workgroupcalendar.activity.activityForManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.activity.activityForEmployee.AdditionalInformationActivity;
import com.appoftatar.workgroupcalendar.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InformationEnployeeActivity extends AppCompatActivity {
    private DatabaseReference rootDataBase;
    FloatingActionButton fabDellEmployee;
    TextView tvFirstName;
    TextView tvSURName;
    TextView tvEmail;
    TextView tvPassword;
    TextView tvTelefon;
    TextView tvAditional;
    String userId;
    private ImageView ivTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_enployee);
        fabDellEmployee = findViewById(R.id.fab);
        fabDellEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dellUserFromDataBase();
            }
        });

        tvFirstName = (TextView) findViewById(R.id.tvFirstname);
        tvSURName = (TextView) findViewById(R.id.tvSurname);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvPassword = (TextView) findViewById(R.id.tvPassword);
        tvTelefon = (TextView) findViewById(R.id.tvTelefon);
        tvAditional = (TextView) findViewById(R.id.tvAditional);

        ivTitle = (ImageView) findViewById(R.id.ivTitle);

        Intent intent = getIntent();
        userId = intent.getStringExtra("USER_ID");
        final String FIRST_NAME = intent.getStringExtra("FIRST_NAME");
        final String SUR_NAME = intent.getStringExtra("SUR_NAME");
        final String TELEFON = intent.getStringExtra("TELEFON");
        final String EMAIL = intent.getStringExtra("EMAIL");
        final String PASSWORD = intent.getStringExtra("PASSWORD");

        tvFirstName.setText("Firstname: " + FIRST_NAME);
        tvSURName.setText("Lastname: " + SUR_NAME);
        tvEmail.setText("Email: " + EMAIL);
        tvPassword.setText("Password: " + PASSWORD);
        tvTelefon.setText("Telefon: " + TELEFON);

        tvAditional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AdditionalInformationActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ivTitle.setImageBitmap(Common.decodeSampledBitmapFromResource(getResources(),R.drawable.title2,ivTitle.getWidth(),ivTitle.getHeight()));

    }
    private void dellUserFromDataBase() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

        builder

                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                rootDataBase = FirebaseDatabase.getInstance().getReference();
                rootDataBase.child("users").orderByChild("ID").equalTo(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            User user = userSnapshot.getValue(User.class);
                            if (user.ID.equals(userId))
                                userSnapshot.getRef().setValue(null);
                        }
                        Intent intent = new Intent(getApplicationContext(), ManagerHomeActivity.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("Data base", "Failed to read value.", databaseError.toException());
                    }
                });
                // Do nothing, but close the dialog
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
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