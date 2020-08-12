package com.appoftatar.workgroupcalendar.ui.employee_inform;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.appoftatar.workgroupcalendar.activity.activityForEmployee.AdditionalInformationActivity;
import com.appoftatar.workgroupcalendar.activity.activityForEmployee.CreateHolidayRequestActivity;
import com.appoftatar.workgroupcalendar.activity.StatusSelectionActivity;
import com.appoftatar.workgroupcalendar.models.HolidayRequest;
import com.appoftatar.workgroupcalendar.activity.activityForEmployee.AnsverToRequestActivity;
import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.SigninActivity;
import com.appoftatar.workgroupcalendar.models.User;
import com.appoftatar.workgroupcalendar.models.UserSick;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class EmployeeFragment extends Fragment {


    private Button btnHolidayRequest;
    private Button btnSalary;
    private TextView tvnameEmployee;
    private TextView  tvCurrentDate;
    private TextView  tvVacation;
    private TextView  tvNumberDaysVacation;
    private TextView  tvSick;
    private TextView  tvNumberDaysSick;
    private TextView tvAnsverToRequest;
    private TextView tvTitleNumberOfDays;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference rootDataBase;
    private Switch switchSick;
    private LinearLayout llDisplay;
    private ProgressDialog progressDialog;
    private ObjectAnimator colorAnimForAnsver;
    private boolean startActivity;

    @SuppressLint("WrongConstant")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_employee, container, false);

        //region ================== Initialisation Views ==========================================
        llDisplay = root.findViewById(R.id.llDisplay);
        switchSick = (Switch)root.findViewById(R.id.switchSik);
        tvnameEmployee = (TextView) root.findViewById(R.id.tvnameEmployee);
        tvTitleNumberOfDays = (TextView) root.findViewById(R.id.tvTitleNumberOfDays);
        tvCurrentDate = (TextView) root.findViewById(R.id.tvCurrentDate);
        tvVacation = (TextView) root.findViewById(R.id.tvVacation);
        tvNumberDaysVacation = (TextView) root.findViewById(R.id.tvNumberDaysVacation);
        tvSick = (TextView) root.findViewById(R.id.tvSick);
        tvNumberDaysSick = (TextView) root.findViewById(R.id.tvNumberDaysSick);
        btnHolidayRequest = (Button)root.findViewById(R.id.btnHolidayRequest);
        btnSalary = (Button)root.findViewById(R.id.btnSalary);
        tvAnsverToRequest = root.findViewById(R.id.tvAnsverToRequest);
        tvAnsverToRequest.setVisibility(LinearLayout.INVISIBLE);

        colorAnimForAnsver = ObjectAnimator.ofInt(tvAnsverToRequest, "textColor",
                Color.RED, Color.BLUE);
        colorAnimForAnsver.setEvaluator(new ArgbEvaluator());
        colorAnimForAnsver.setDuration(700);
        colorAnimForAnsver.setRepeatMode(Animation.REVERSE);
        colorAnimForAnsver.setRepeatCount(Animation.INFINITE);
        //endregion

        tvAnsverToRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getContext(), AnsverToRequestActivity.class);
               startActivity(intent);
            }
        });



        getCurrentUser();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        tvCurrentDate.setText(dateFormat.format(currentDate));
        btnHolidayRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateHolidayRequestActivity.class);
                startActivity(intent);
            }
        });
        btnSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AdditionalInformationActivity.class);
                intent.putExtra("USER_ID", Common.currentUser.ID);
                startActivity(intent);
            }
        });
        switchSick.setOnCheckedChangeListener(createSwithListener());

        return root;
    }

    private CompoundButton.OnCheckedChangeListener createSwithListener(){
        CompoundButton.OnCheckedChangeListener listener =  new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switchSick.isChecked()) {
                    setUserSickToDataBase();
                }else {
                    dellUserSickFromDataBase();
                }
            }
        };
        return listener;
    }



    public static Fragment getInstance() {
        Bundle args = new Bundle();
        EmployeeFragment fragment = new EmployeeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private void getCurrentUser(){

        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Get data...");
        progressDialog.show();

        mAuth = FirebaseAuth.getInstance();

        firebaseUser = mAuth.getInstance().getCurrentUser();
        rootDataBase = FirebaseDatabase.getInstance().getReference("users");

        DatabaseReference userRef = rootDataBase.child(mAuth.getUid() + "/");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if(!Common.manager && user.Manager.equals("false")) {
                            Common.currentUser = user;
                            tvnameEmployee.setText(Common.currentUser.FirstName + " " + Common.currentUser.SurName);
                            checkStatusSick();
                            checkRequest();
                        getAmountDaysSickOfYear();
                        getNumberDaysInVacation();
                        }else {
                            Intent intent = new Intent(getContext() , StatusSelectionActivity.class);
                            startActivity(intent);
                            Toast.makeText(getContext(), "You need create your profile .", Toast.LENGTH_LONG).show();
                        }
                } else {
                    FirebaseUser user = mAuth.getCurrentUser();
                    user.delete();
                    Intent intent = new Intent(getContext() , SigninActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }


    private void checkRequest(){
        rootDataBase = FirebaseDatabase.getInstance().getReference();


        rootDataBase.child("requests").child(Common.currentUser.IdManager).child(Common.currentUser.IdWorkGroup)
                .orderByChild("idEmployee").equalTo(Common.currentUser.ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot requestSnapshot: dataSnapshot.getChildren()) {
                    if(requestSnapshot.child("viewEmployee").getValue().toString().equals("false")){
                         if(requestSnapshot.child("answerRequest").getValue().toString().equals("true") ||
                            requestSnapshot.child("answerRequest").getValue().toString().equals("false") ) {
                            count++;
                        }
                    }

                }
                if(count>0){
                    tvAnsverToRequest.setVisibility(LinearLayout.VISIBLE);
                    colorAnimForAnsver.start();
                }else tvAnsverToRequest.setVisibility(LinearLayout.INVISIBLE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void getNumberDaysInVacation(){
        rootDataBase = FirebaseDatabase.getInstance().getReference();


        rootDataBase.child("requests").child(Common.currentUser.IdManager).child(Common.currentUser.IdWorkGroup)
                .orderByChild("idEmployee").equalTo(Common.currentUser.ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer amountDays = 0;
                Date currentDate = new Date();
                for (DataSnapshot requestSnapshot: dataSnapshot.getChildren()) {
                    if(requestSnapshot.child("viewEmployee").getValue().toString().equals("true")){

                        HolidayRequest request = new HolidayRequest();
                        for (DataSnapshot itemSnapshot : requestSnapshot.child("dateList").getChildren())
                            request.setDateToList(itemSnapshot.getValue(Date.class));

                        if(currentDate.getYear() == request.getDateList().get("0").getYear())
                            amountDays += request.getDateList().size();

                    }
                }
                tvNumberDaysVacation.setText(amountDays.toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Data base", "Failed to read value.", databaseError.toException());
            }
        });
    }


    private void setUserSickToDataBase() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Sick");
        builder.setMessage("Are you sure?");

        builder

                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        Date currentDate = new GregorianCalendar().getTime();
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String userFullName = Common.currentUser.FirstName.charAt(0)+"."+Common.currentUser.SurName;
                        UserSick userSick = new UserSick(dateFormat.format(currentDate),userFullName,Common.currentUser.ID);
                         userSick.setAmountDays("0");
                        rootDataBase = FirebaseDatabase.getInstance().getReference();
                        rootDataBase.child("sick").child(Common.currentUser.IdManager).child(Common.currentUser.IdWorkGroup).push().setValue(userSick)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        changeUiIfStatusSick();

                                    }
                                });
                        // Do nothing, but close the dialog
                        dialog.dismiss();

                        progressDialog = new ProgressDialog(getContext(),
                                R.style.AppTheme_Dark_Dialog);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Change status...");
                        progressDialog.show();
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
        if(startActivity) {
            startActivity = false;
       }else alert.show();
    }

    private void dellUserSickFromDataBase() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AppTheme_Dark_Dialog);

        builder.setTitle("Sick");
        builder.setMessage("Are you sure?");
        builder .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        rootDataBase = FirebaseDatabase.getInstance().getReference();
                        rootDataBase.child("sick").child(Common.currentUser.IdManager).child(Common.currentUser.IdWorkGroup)
                                .orderByChild("idUser").equalTo(Common.currentUser.ID)
                                .addValueEventListener(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                                    if (userSnapshot.child("amountDays").getValue().toString().equals("0")) {

                                        String[] tokens = userSnapshot.child("diseaseOnsetDate").getValue().toString().split("/");
                                        Date diseaseOnsetDate = new GregorianCalendar(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[1])-1, Integer.parseInt(tokens[0])).getTime();
                                        userSnapshot.getRef().child("amountDays").setValue(Integer.toString(getNumberDaysSick(diseaseOnsetDate).size()));
                                        userSnapshot.getRef().child("sickDays").setValue(getNumberDaysSick(diseaseOnsetDate));
                                    }
                                }
                                changeUiIfStatusNotSick();
                                progressDialog.dismiss();
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("Data base", "Failed to read value.", databaseError.toException());
                            }
                        });
                        // Do nothing, but close the dialog
                        dialog.dismiss();

                        progressDialog = new ProgressDialog(getContext(),
                                R.style.AppTheme_Dark_Dialog);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Change status...");
                        progressDialog.show();
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

    private HashMap<String,Date> getNumberDaysSick(Date diseaseOnsetDate) {
        HashMap<String, Date> sickDays = new HashMap<>();
        Date currentDate = new Date();
        Date date = diseaseOnsetDate;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        boolean checkAmountDays = true;

        while (checkAmountDays) {
            if (date.getDate() != currentDate.getDate() || date.getMonth() != currentDate.getMonth()) {
                sickDays.put(Integer.toString(sickDays.size()), date);
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                date = calendar.getTime();

            } else {
                checkAmountDays = false;
                sickDays.put(Integer.toString(sickDays.size()), date);
            }


        }
        return sickDays;
    }

    private void changeUiIfStatusSick(){
        tvVacation.setVisibility(View.INVISIBLE);
        tvTitleNumberOfDays.setVisibility(View.INVISIBLE);
        tvNumberDaysVacation.setVisibility(View.INVISIBLE);
        tvSick .setVisibility(View.INVISIBLE);
        tvNumberDaysSick.setVisibility(View.INVISIBLE);
        switchSick.setText(R.string.tv_ifYouNotSick);
        llDisplay.setBackgroundResource(R.drawable.sick);
        btnHolidayRequest.setVisibility(Button.INVISIBLE);
        switchSick.setChecked(true);
    }

    private void changeUiIfStatusNotSick(){
        tvVacation.setVisibility(View.VISIBLE);
        tvTitleNumberOfDays.setVisibility(View.VISIBLE);
        tvNumberDaysVacation.setVisibility(View.VISIBLE);
        tvSick.setVisibility(View.VISIBLE);
        tvNumberDaysSick.setVisibility(View.VISIBLE);
        switchSick.setText(R.string.tv_ifyousick);
        llDisplay.setBackgroundColor(Color.TRANSPARENT);
        btnHolidayRequest.setVisibility(Button.VISIBLE);
    }

    private  void checkStatusSick(){

        rootDataBase = FirebaseDatabase.getInstance().getReference();
        rootDataBase.child("sick").child(Common.currentUser.IdManager).child(Common.currentUser.IdWorkGroup)
                .orderByChild("idUser").equalTo(Common.currentUser.ID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                            if (userSnapshot.child("amountDays").getValue().toString().equals("0")) {
                                startActivity = true;
                               changeUiIfStatusSick();
                            }

                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        }
                });
    }

    private  void getAmountDaysSickOfYear(){

        rootDataBase = FirebaseDatabase.getInstance().getReference();
        rootDataBase.child("sick").child(Common.currentUser.IdManager).child(Common.currentUser.IdWorkGroup)
                .orderByChild("idUser").equalTo(Common.currentUser.ID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Date curentDate = new Date();
                        Integer amountDays = 0;
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                            String[] tokens = userSnapshot.child("diseaseOnsetDate").getValue().toString().split("/");
                            if(curentDate.getYear()+1900 == Integer.parseInt(tokens[2]))
                            amountDays += Integer.parseInt(userSnapshot.child("amountDays").getValue().toString());
                        }
                      tvNumberDaysSick.setText(amountDays.toString());
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }


}
