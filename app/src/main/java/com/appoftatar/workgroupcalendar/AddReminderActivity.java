package com.appoftatar.workgroupcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.calendar.WorkDayWithRemainder;
import com.appoftatar.workgroupcalendar.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddReminderActivity extends AppCompatActivity {
    private TextView tv_weekday;
    private TextView tv_date;
    private TextView tv_time;
    private EditText et_event;
    private ImageView iv_clock;
    private RadioGroup radioGroup;
    private RadioButton rb_forYou;
    private RadioButton rb_forEmployee;
    private LinearLayout ll_Employes;
    private Spinner spinerEmployes;
    private Button btnAddRemainder;
    private String month;
    private String date;
    private User toUser;
    private WorkDayWithRemainder workDayWithRemainder;
    private ArrayList<User> lEmployes;
    private boolean remainderForEmplloy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        workDayWithRemainder = new WorkDayWithRemainder();
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        ll_Employes = (LinearLayout)findViewById(R.id.ll_Employes);
        if(!Common.manager) {
            ll_Employes.setVisibility(View.INVISIBLE);
             radioGroup.setVisibility(View.INVISIBLE);
        }
        spinerEmployes = (Spinner) findViewById(R.id.spinerEmployes);
        tv_weekday = (TextView)findViewById(R.id.tv_weekday);
        tv_date = (TextView)findViewById(R.id.tv_date);
        tv_time = (TextView)findViewById(R.id.tv_time);

        et_event = (EditText)findViewById(R.id.et_event);

        iv_clock = (ImageView) findViewById(R.id.iv_clock);
        iv_clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener pikerListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        workDayWithRemainder.setHours(hourOfDay);
                        workDayWithRemainder.setMinutes(minute);
                        String hours="";
                        String minutes="";
                        if(hourOfDay<10)
                            hours = "0"+hourOfDay;
                        else  hours = Integer.toString(hourOfDay);
                        if(minute<10)
                            minutes = "0"+minute;
                        else  minutes = Integer.toString(minute);

                        tv_time.setText(hours+":"+minutes+":00");
                    }

                };
                showDatePickerDialog(pikerListener);
            }
        });
        btnAddRemainder = (Button) findViewById(R.id.btnAddRemainder);
        btnAddRemainder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReminder();
            }
        });



        rb_forYou = (RadioButton) findViewById(R.id.rb_forYou);
        rb_forYou.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true) {
                    ll_Employes.setVisibility(View.INVISIBLE);
                    remainderForEmplloy = false;
                }
            }
        });
        rb_forYou.setChecked(true);

        rb_forEmployee = (RadioButton) findViewById(R.id.rb_forEmployee);
        rb_forEmployee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true) {
                    ll_Employes.setVisibility(View.VISIBLE);
                    remainderForEmplloy = true;
                }

            }
        });


        Intent intent = getIntent();

        tv_weekday.setText(intent.getStringExtra("DAY_WEEK"));

        month = intent.getStringExtra("MONTH");


        date = intent.getStringExtra("DATE");
        workDayWithRemainder.setDate(Integer.valueOf(date));
        workDayWithRemainder.setMonth(Integer.valueOf(month));
        tv_date.setText(date);
        ll_Employes.setVisibility(View.INVISIBLE);

        if(Common.manager)
           getListEmployeesFromDataBase();
    }

    private void showDatePickerDialog(TimePickerDialog.OnTimeSetListener pikerListener){

        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog piker =    new TimePickerDialog(this,  pikerListener,hours,minute, DateFormat.is24HourFormat(getBaseContext()));
        piker.show();
    }


    private void getListEmployeesFromDataBase(){

        DatabaseReference rootDataBase = FirebaseDatabase.getInstance().getReference();
        rootDataBase.child("users").orderByChild("IdManager").equalTo(Common.currentUser.ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                lEmployes = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if(user.IdWorkGroup.equals(Common.currentGroup))
                        lEmployes.add(user);

                }
                if (lEmployes.size() != 0) {
                   createSpinerEmployees();
                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void createSpinerEmployees() {

        if(lEmployes != null || lEmployes.size() != 0) {
            ArrayList<String> listUsers = new ArrayList<>();
            for (User user: lEmployes){
                listUsers.add(user.FirstName+" "+user.SurName);
            }


            ArrayAdapter<String> usersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listUsers);
            usersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinerEmployes.setAdapter(usersAdapter);
            spinerEmployes.setOnItemSelectedListener(onItemSelectedListener());
            spinerEmployes.setSelection(0);
        }
    }

    private AdapterView.OnItemSelectedListener onItemSelectedListener(){

        return  new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toUser = lEmployes.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }


    private boolean validateRemainder(){
        if(et_event.getText().toString().isEmpty()) {
            Toast.makeText(AddReminderActivity.this, getString(R.string.hint_enterevent),
                    Toast.LENGTH_LONG).show();
            return false;
        }
        else if(workDayWithRemainder.getHours()==null || workDayWithRemainder.getMinutes()==null) {
            Toast.makeText(AddReminderActivity.this, getString(R.string.toast_picktime),
                    Toast.LENGTH_LONG).show();
            return false;
        }
        else if(remainderForEmplloy && toUser==null) {
            Toast.makeText(AddReminderActivity.this, getString(R.string.tv_choose_employee),
                    Toast.LENGTH_LONG).show();
            return false;
        }else return true;


    }


    private void saveReminder(){

        if(validateRemainder()){
            workDayWithRemainder.setEvent(et_event.getText().toString());
            String idEmployee = "";
            if(remainderForEmplloy)
                idEmployee = toUser.ID;
            else idEmployee = Common.currentUser.ID;

            DatabaseReference   root = FirebaseDatabase.getInstance().getReference();
            Calendar calendar = Calendar.getInstance();

            root.child("remainders").child(Integer.toString(calendar.getTime().getYear()+1900))
                    .child(month).child(date).child(idEmployee).push().setValue(workDayWithRemainder);


            onBackPressed();
            finish();

        }
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
