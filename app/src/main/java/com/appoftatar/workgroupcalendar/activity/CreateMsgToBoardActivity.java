package com.appoftatar.workgroupcalendar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.models.MsgOnBoard;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateMsgToBoardActivity extends AppCompatActivity {

    TextView tvMsgOnBoard;
    EditText etInputMsg;
    private DatabaseReference rootDataBase;
    private FirebaseAuth mAuth;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_msg_to_board);

        tvMsgOnBoard = (TextView)findViewById(R.id.tvMsgOnBoard);
        etInputMsg = (EditText) findViewById(R.id.etInputText);

        etInputMsg.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                tvMsgOnBoard.setText(s);
            }
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!etInputMsg.getText().toString().equals("")) {
                    String IdManager = Common.currentUser.IdManager;
                    String currentGroup = Common.currentUser.IdWorkGroup;
                    String statusManager = "false";
                    rootDataBase = FirebaseDatabase.getInstance().getReference();
                    if (Common.manager) {
                        IdManager = Common.currentUser.ID;
                        currentGroup = Common.currentGroup;
                        statusManager = "true";
                    }
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    String currentDate = dateFormat.format(date);
                    MsgOnBoard message = new MsgOnBoard(tvMsgOnBoard.getText().toString(), currentDate, currentGroup, statusManager,
                            Common.currentUser.FirstName + " " + Common.currentUser.SurName);
                    //add message to database
                    rootDataBase.child("boards").child(IdManager).push().setValue(message);
                    onBackPressed();
                        finish();

                }
            }
        });
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
