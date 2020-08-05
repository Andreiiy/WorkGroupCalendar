package com.appoftatar.workgroupcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appoftatar.workgroupcalendar.adapters.EmployesFromRedactorAdapter;
import com.appoftatar.workgroupcalendar.adapters.ShiftAdapter;
import com.appoftatar.workgroupcalendar.models.RedactorShedule;
import com.appoftatar.workgroupcalendar.models.Schedule;
import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class ConstructorAdditionalScheduleActivity extends AppCompatActivity {


    private TextView tvDateFrom;
    private TextView linkAddEmployee;
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
    private Button btnNextSchedule;
    private ArrayList<Schedule> listSchedule;
    private ArrayList<User> lEmployes ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constructor_additional_schedule);

        // region ============= I N I T F I E L D S =========================
        listSchedule = new ArrayList<>();
        redactorShedule = new RedactorShedule();
        btnmove = (Button)findViewById(R.id.btnmove);
        btnclear = (Button)findViewById(R.id.btnclear);
        btnSaveSchedule = (Button)findViewById(R.id.btnSaveSchedule);

        btnNextSchedule = (Button)findViewById(R.id.btnNextSchedule);
        int with = btnNextSchedule.getWidth();
        int heigt = btnNextSchedule.getHeight();

        iv_numberShift1 = (ImageView)findViewById(R.id.ivShift1);
        iv_numberShift2 = (ImageView)findViewById(R.id.ivShift2);
        iv_numberShift3 = (ImageView)findViewById(R.id.ivShift3);
        rv_shift1 = (RecyclerView)findViewById(R.id.rv_shift1);
        rv_shift2 = (RecyclerView)findViewById(R.id.rv_shift2);
        rv_shift3 = (RecyclerView)findViewById(R.id.rv_shift3);
        rv_listEmployee = (RecyclerView)findViewById(R.id.rv_listEmployee);
        tvDateFrom = (TextView)findViewById(R.id.tvDateFrom);
        linkAddEmployee = (TextView)findViewById(R.id.linkAddEmployee);
        lEmployes = new ArrayList<>();

        //endregion

        //region ============= S E T    L I S T E N E R S ===================
        linkAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ConstructorAdditionalScheduleActivity.this);

                LayoutInflater inflater = getLayoutInflater();

                View dialogView = inflater.inflate(R.layout.alertdialog_view,null,false);

                // Specify alert dialog is not cancelable/not ignorable
                builder.setCancelable(false);

                // Set the custom layout as alert dialog view
                builder.setView(dialogView);

                // Get the custom alert dialog view widgets reference
                Button btn_positive = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
                Button btn_negative = (Button) dialogView.findViewById(R.id.dialog_negative_btn);
                final EditText et_name = (EditText) dialogView.findViewById(R.id.et_name);
                final EditText et_surName = (EditText) dialogView.findViewById(R.id.et_surName);

                // Create the alert dialog
                final AlertDialog dialog = builder.create();

                // Set positive/yes button click listener
                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dismiss the alert dialog

                        String name = et_name.getText().toString();
                        String surName = et_surName.getText().toString();
                        if(!name.isEmpty() && !surName.isEmpty()) {
                            dialog.cancel();
                            redactorShedule.addNewEmployee(name,surName);
                            initLists();
                        }
                    }
                });

                // Set negative/no button click listener
                btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         dialog.dismiss();

                    }
                });

                // Display the custom alert dialog on interface
                dialog.show();


            }
        });

        btnmove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redactorShedule.moveShifts();
                initLists();
            }
        });

        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redactorShedule.clearSchedule();
                initLists();
            }
        });

        btnSaveSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(redactorShedule.getEditSchedule().getDateFrom() == null){
                    Toast.makeText(getBaseContext(), getString(R.string.choose_dates), Toast.LENGTH_SHORT).show();
                }else if(redactorShedule.getShift1().size() +
                        redactorShedule.getShift2().size() +
                        redactorShedule.getShift3().size() == 0){
                    Toast.makeText(getBaseContext(), getString(R.string.choose_employes), Toast.LENGTH_SHORT).show();
                }else {
                    listSchedule.add(redactorShedule.getEditSchedule());
                    deleteOldAndSaveNewSchedules();
                    Intent intent = new Intent(getBaseContext(), ManagerHomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        });

        btnNextSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listSchedule.add(redactorShedule.getEditSchedule());
                redactorShedule.clearEditSchedule();
                tvDateFrom.setText("Date");
                redactorShedule.clearSchedule();
                redactorShedule.setEmployesListInGroup(lEmployes);
                initLists();
            }
        });

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

        tvDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener pikerListener = new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date = new GregorianCalendar(year,monthOfYear,dayOfMonth).getTime();
                        SimpleDateFormat fromDateFormat =  new SimpleDateFormat("dd/MM/yyyy");
                        tvDateFrom.setText(fromDateFormat.format(date));
                        redactorShedule.setDateFrom(date);
                    }
                };
                showDatePickerDialog(pikerListener);

            }
        });
//endregion


        getListEmployeesFromDataBase();
    }


    private void showDatePickerDialog(DatePickerDialog.OnDateSetListener pikerListener){

        DatePickerDialog piker =    new DatePickerDialog(ConstructorAdditionalScheduleActivity.this, pikerListener,
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

                    lEmployes.clear();
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        if(user.IdWorkGroup.equals(Common.currentGroup))
                        lEmployes.add(user);

                    }
                    if (lEmployes.size() != 0) {
                        redactorShedule.setEmployesListInGroup(lEmployes);

                        initLists();
                    }
                }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void  saveNewSchedules(){
        Integer index = 0;
        for (Schedule item:listSchedule) {
            saveSchedule(index++, item);
        }
  }


    private void  saveSchedule(Integer index, Schedule item){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        root.child("schedules").child(Common.currentUser.ID).child(Common.currentGroup).child("additionalSchedule").child(index.toString()).setValue(item);
    }


    private void  deleteOldAndSaveNewSchedules(){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        root.child("schedules").child(Common.currentUser.ID).child(Common.currentGroup).child("additionalSchedule").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                saveNewSchedules();
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
