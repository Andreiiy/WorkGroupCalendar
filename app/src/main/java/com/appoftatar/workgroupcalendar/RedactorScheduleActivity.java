package com.appoftatar.workgroupcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.adapters.EmployesFromRedactorAdapter;
import com.appoftatar.workgroupcalendar.adapters.ShiftAdapter;
import com.appoftatar.workgroupcalendar.models.RedactorShedule;
import com.appoftatar.workgroupcalendar.models.Schedule;
import com.appoftatar.workgroupcalendar.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RedactorScheduleActivity extends AppCompatActivity {

   private TextView tvDateTo;
   private TextView tvDateFrom;
   private Calendar calendar = Calendar.getInstance();
   private Date date;
   private RedactorShedule redactorShedule;
   private RecyclerView rv_shift1;
   private RecyclerView rv_shift2;
   private RecyclerView rv_shift3;
   private RecyclerView rv_listEmployee;
   private Integer shift = 0;
   private ImageView iv_numberShift1;
   private ImageView iv_numberShift2;
   private ImageView iv_numberShift3;
   private Button btnmove;
   private Button btnclear;
   private Button btnSaveSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redactor_schedule);

        redactorShedule = new RedactorShedule();


        btnmove = (Button)findViewById(R.id.btnmove);
        btnmove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redactorShedule.moveShifts();
                initLists();
            }
        });
        btnclear = (Button)findViewById(R.id.btnclear);
        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redactorShedule.clearSchedule();
                initLists();
            }
        });
        btnSaveSchedule = (Button)findViewById(R.id.btnSaveSchedule);
        Bitmap bitmap = Common.decodeSampledBitmapFromResource(getResources(),R.drawable.btnred,100,50);
        if(android.os.Build.VERSION.SDK_INT < 16) {
            btnSaveSchedule.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
        }
        else {
            btnSaveSchedule.setBackground(new BitmapDrawable(getResources(),bitmap));
        }
        btnSaveSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(redactorShedule.getEditSchedule().getDateFrom() == null ||
                        redactorShedule.getEditSchedule().getDateTo() == null){
                    Toast.makeText(getBaseContext(), getString(R.string.choose_dates), Toast.LENGTH_SHORT).show();
                }else if(redactorShedule.getShift1().size() +
                             redactorShedule.getShift2().size() +
                                 redactorShedule.getShift3().size() == 0){
                    Toast.makeText(getBaseContext(), getString(R.string.choose_employes), Toast.LENGTH_SHORT).show();
                }else {
                    redactorShedule.saveSchedule();
                    Intent intent = new Intent(getBaseContext(), ManagerHomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        iv_numberShift1 = (ImageView)findViewById(R.id.ivShift1);
        iv_numberShift2 = (ImageView)findViewById(R.id.ivShift2);
        iv_numberShift3 = (ImageView)findViewById(R.id.ivShift3);
        iv_numberShift1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shift = 1;
                iv_numberShift1.setBackgroundColor(Color.RED);
                iv_numberShift2.setBackgroundColor(Color.TRANSPARENT);
                iv_numberShift3.setBackgroundColor(Color.TRANSPARENT);

            }
        });
        iv_numberShift2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shift = 2;
                iv_numberShift2.setBackgroundColor(Color.RED);
                iv_numberShift1.setBackgroundColor(Color.TRANSPARENT);
                iv_numberShift3.setBackgroundColor(Color.TRANSPARENT);

            }
        });
        iv_numberShift3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shift = 3;
                iv_numberShift3.setBackgroundColor(Color.RED);
                iv_numberShift2.setBackgroundColor(Color.TRANSPARENT);
                iv_numberShift1.setBackgroundColor(Color.TRANSPARENT);

            }
        });
        rv_shift1 = (RecyclerView)findViewById(R.id.rv_shift1);
        rv_shift2 = (RecyclerView)findViewById(R.id.rv_shift2);
        rv_shift3 = (RecyclerView)findViewById(R.id.rv_shift3);
        rv_listEmployee = (RecyclerView)findViewById(R.id.rv_listEmployee);


        tvDateTo = (TextView)findViewById(R.id.tvDateTo);
        tvDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener pikerListener = new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date = new GregorianCalendar(year,monthOfYear,dayOfMonth).getTime();
                        SimpleDateFormat fromDateFormat =  new SimpleDateFormat("dd/MM/yyyy");
                        tvDateTo.setText(fromDateFormat.format(date));
                        redactorShedule.setDateTo(date);
                    }
                };


                showDatePickerDialog(pikerListener);
            }
        });
        tvDateFrom = (TextView)findViewById(R.id.tvDateFrom);
        tvDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener pikerListener = new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date = new GregorianCalendar(year,monthOfYear,dayOfMonth).getTime();
                        SimpleDateFormat fromDateFormat =  new SimpleDateFormat("dd/MM");
                        tvDateFrom.setText(fromDateFormat.format(date));
                        redactorShedule.setDateFrom(date);
                    }
                };
                showDatePickerDialog(pikerListener);

            }
        });

        getListEmployeesFromDataBase();
    }

    private void showDatePickerDialog(DatePickerDialog.OnDateSetListener pikerListener){

        DatePickerDialog piker =    new DatePickerDialog(RedactorScheduleActivity.this, pikerListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                ;
        piker.show();
    }

    private void initLists(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this);
        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(this);

        //put gridManager to recyclerview
        rv_shift1.setLayoutManager(linearLayoutManager);
        rv_shift2.setLayoutManager(linearLayoutManager2);
        rv_shift3.setLayoutManager(linearLayoutManager3);
        rv_listEmployee.setLayoutManager(linearLayoutManager4);

        rv_shift1.setHasFixedSize(true);
        rv_shift2.setHasFixedSize(true);
        rv_shift3.setHasFixedSize(true);
        rv_listEmployee.setHasFixedSize(true);

        ShiftAdapter.OnEmployeeClickListener onEmployeeShift1ClickListener =new  ShiftAdapter.OnEmployeeClickListener(){
            @Override
            public void onEmployeeClick(User user) {
                redactorShedule.deleteEmployeeFromShift(1,user);
                initLists();
            }
        };

        ShiftAdapter.OnEmployeeClickListener onEmployeeShift2ClickListener =new  ShiftAdapter.OnEmployeeClickListener(){
            @Override
            public void onEmployeeClick(User user) {
                redactorShedule.deleteEmployeeFromShift(2,user);
                initLists();
            }
        };

        ShiftAdapter.OnEmployeeClickListener onEmployeeShift3ClickListener =new  ShiftAdapter.OnEmployeeClickListener(){
            @Override
            public void onEmployeeClick(User user) {
                redactorShedule.deleteEmployeeFromShift(3,user);
                initLists();
            }
        };
        EmployesFromRedactorAdapter.OnEmployeeClickListener employeeClickListener =new  EmployesFromRedactorAdapter.OnEmployeeClickListener(){
            @Override
            public void onEmployeeClick(User user) {
                if(shift != 0) {
                    redactorShedule.sendEmployeeFromListToShift(shift, user);
                    initLists();
                }else
                    Toast.makeText(getBaseContext(), "Choose a shift", Toast.LENGTH_SHORT).show();
            }
        };


        //create new adapters
       ShiftAdapter shift1Adapter = new ShiftAdapter(redactorShedule.getShift1(),true,onEmployeeShift1ClickListener);
       ShiftAdapter shift2Adapter = new ShiftAdapter(redactorShedule.getShift2(),true,onEmployeeShift2ClickListener);
       ShiftAdapter shift3Adapter = new ShiftAdapter(redactorShedule.getShift3(),true,onEmployeeShift3ClickListener);
       EmployesFromRedactorAdapter employesFromRedactorAdapter = new EmployesFromRedactorAdapter(redactorShedule.getEmployesList(),employeeClickListener);

        //put adapter to recyclerview
        rv_shift1.setAdapter(shift1Adapter);
        rv_shift2.setAdapter(shift2Adapter);
        rv_shift3.setAdapter(shift3Adapter);
        rv_listEmployee.setAdapter(employesFromRedactorAdapter);
    }



    private void getListEmployeesFromDataBase(){

        DatabaseReference rootDataBase = FirebaseDatabase.getInstance().getReference();


        rootDataBase.child("users").orderByChild("IdManager").equalTo(Common.currentUser.ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     ArrayList<User> lEmployes = new ArrayList<>();
                   for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        if(user.IdWorkGroup.equals(Common.currentGroup))
                        lEmployes.add(user);

                    }
                    if (lEmployes.size() != 0) {
                        redactorShedule.setEmployesListInGroup(lEmployes);
                        getSchedale();
                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void getSchedale(){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        String idManager = Common.currentUser.ID;
        String group = Common.currentGroup;
        if(!Common.manager){
            idManager = Common.currentUser.IdManager;
            group = Common.currentUser.IdWorkGroup;
        }
        root.child("schedules").child(idManager).child(group).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot scheduleSnapshot: dataSnapshot.getChildren()) {
                    if(scheduleSnapshot.getKey().equals("newSchedule")) {
                        Schedule schedule = new Schedule();
                        schedule.setDateFrom(scheduleSnapshot.child("dateFrom").getValue(Date.class));
                        schedule.setDateTo(scheduleSnapshot.child("dateTo").getValue(Date.class));


                        for (DataSnapshot itemSnapshot : scheduleSnapshot.child("shift1").getChildren()) {
                            User user = itemSnapshot.getValue(User.class);
                            schedule.setEmployeeToShift(1,checkAdditionalInformation(user));
                        }
                        for (DataSnapshot itemSnapshot : scheduleSnapshot.child("shift2").getChildren())
                            schedule.setEmployeeToShift(2,checkAdditionalInformation(itemSnapshot.getValue(User.class)));

                        for (DataSnapshot itemSnapshot : scheduleSnapshot.child("shift3").getChildren())
                            schedule.setEmployeeToShift(3,checkAdditionalInformation(itemSnapshot.getValue(User.class)));


                        redactorShedule.setOldSchedule(schedule);

                        initLists();
                    }

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });
        initLists();
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

    private User checkAdditionalInformation(User userCheck){
        for(User user:redactorShedule.getEmployesList()){
            if(user.ID.equals(userCheck.ID))
                userCheck.additionalInformation = user.additionalInformation;
        }
        return userCheck;
    }
}
