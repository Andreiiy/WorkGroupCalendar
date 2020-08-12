package com.appoftatar.workgroupcalendar.activity.activityForEmployee;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.calendar.EditorOfWorkDay;
import com.appoftatar.workgroupcalendar.calendar.WorkDay;

public class FormForEditionalWorkDayActivity extends AppCompatActivity {
    private Switch switchSick;
    private Switch switchVacation;
    private Switch switchShift;
    private Switch switchAdditionalTime;
    private Switch switchClear;
    private EditorOfWorkDay editor;
    private WorkDay workDay;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_for_editional_work_day);

        switchSick = (Switch) findViewById(R.id.switchSick);
        switchVacation = (Switch) findViewById(R.id.switchVacation);
        switchShift = (Switch) findViewById(R.id.switchShift);
        switchAdditionalTime = (Switch) findViewById(R.id.switchAdditionalTime);
        switchClear = (Switch) findViewById(R.id.switchClear);


        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            workDay = (WorkDay) arguments.getSerializable(WorkDay.class.getSimpleName());
            editor = new EditorOfWorkDay(Common.currentUser,workDay);
        }else {
            onBackPressed();
            finish();
        }

            switchSick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){

                            AlertDialog.Builder builder = new AlertDialog.Builder(FormForEditionalWorkDayActivity.this);
                            builder.setTitle("Sick");
                            builder.setMessage("Are you sure?");
                            builder.setCancelable(false)

                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {

                                            editor.setSickToWorkDay();
                                            dialog.dismiss();
                                            onBackPressed();
                                            finish();
                                        }
                                    });

                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do nothing
                                    dialog.dismiss();
                                    onBackPressed();
                                    finish();
                                }
                            });

                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }



            });

            switchVacation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        if (isChecked){

                            AlertDialog.Builder builder = new AlertDialog.Builder(FormForEditionalWorkDayActivity.this);
                            builder.setTitle("On vacation");
                            builder.setMessage("Are you sure?");
                            builder.setCancelable(false)

                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {

                                            if (Common.currentUser.additionalInformation == null ||
                                                    Common.currentUser.additionalInformation.getMinutesShift1() == 0) {
                                                dialog.cancel();
                                                Toast.makeText(getBaseContext(), getResources().getText(R.string.fill_salaryData), Toast.LENGTH_LONG).show();
                                                onBackPressed();
                                                finish();
                                            } else{
                                                editor.setVacationToWorkDay();
                                                dialog.dismiss();
                                                onBackPressed();
                                                finish();
                                        }
                                    }
                                    });

                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // Do nothing
                                    dialog.dismiss();
                                    onBackPressed();
                                    finish();
                                }
                            });

                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }

                }

            });

            switchShift.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        AlertDialog.Builder builder = new AlertDialog.Builder(FormForEditionalWorkDayActivity.this);

                        LayoutInflater inflater = getLayoutInflater();

                        View dialogView = inflater.inflate(R.layout.alert_dialog_view_shift,null,false);

                        // Specify alert dialog is not cancelable/not ignorable
                        builder.setCancelable(false);

                        // Set the custom layout as alert dialog view
                        builder.setView(dialogView);

                        // Get the custom alert dialog view widgets reference
                        Button btn_positive = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
                        Button btn_negative = (Button) dialogView.findViewById(R.id.dialog_negative_btn);
                        final EditText et_shift = (EditText) dialogView.findViewById(R.id.et_shift);



                        // Create the alert dialog
                        final AlertDialog dialog = builder.create();

                        // Set positive/yes button click listener
                        btn_positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Dismiss the alert dialog

                                String shift = et_shift.getText().toString();

                                if(!shift.isEmpty() && shift.equals("1") || shift.equals("2") || shift.equals("3"))
                                {
                                    if(Common.currentUser.additionalInformation !=null) {
                                        if (shift.equals("1") && Common.currentUser.additionalInformation.getMinutesShift1() == 0) {
                                            dialog.cancel();
                                            Toast.makeText(getBaseContext(), getResources().getText(R.string.fill_salaryData), Toast.LENGTH_LONG).show();
                                        } else if (shift.equals("2") && Common.currentUser.additionalInformation.getMinutesShift2() == 0) {
                                            dialog.cancel();
                                            Toast.makeText(getBaseContext(), getResources().getText(R.string.fill_salaryData), Toast.LENGTH_LONG).show();
                                        } else if (shift.equals("3") &&  Common.currentUser.additionalInformation.getMinutesShift3() == 0) {
                                            dialog.cancel();
                                            Toast.makeText(getBaseContext(), getResources().getText(R.string.fill_salaryData), Toast.LENGTH_LONG).show();
                                        } else {
                                            Integer numberShift = Integer.parseInt(shift);
                                            editor.setShiftToWorkDay(numberShift);
                                            dialog.cancel();
                                            onBackPressed();
                                            finish();
                                        }
                                    }else {
                                        dialog.cancel();
                                        Toast.makeText(getBaseContext(), getResources().getText(R.string.fill_salaryData), Toast.LENGTH_LONG).show();
                                    }


                                }
                            }
                        });

                        // Set negative/no button click listener
                        btn_negative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                onBackPressed();
                                finish();
                            }
                        });

                        // Display the custom alert dialog on interface
                        dialog.show();
                    }

                }
            });

            switchAdditionalTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FormForEditionalWorkDayActivity.this);

                    LayoutInflater inflater = getLayoutInflater();

                    View dialogView = inflater.inflate(R.layout.alert_dialog_for_editor_workday,null,false);

                    // Specify alert dialog is not cancelable/not ignorable
                    builder.setCancelable(false);

                    // Set the custom layout as alert dialog view
                    builder.setView(dialogView);

                    // Get the custom alert dialog view widgets reference
                    Button btn_positive = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
                    Button btn_negative = (Button) dialogView.findViewById(R.id.dialog_negative_btn);
                    final EditText et_payment = (EditText) dialogView.findViewById(R.id.et_payment);
                    final EditText et_hours = (EditText) dialogView.findViewById(R.id.et_hours);
                    final EditText et_minutes = (EditText) dialogView.findViewById(R.id.et_minutes);


                    // Create the alert dialog
                    final AlertDialog dialog = builder.create();

                    // Set positive/yes button click listener
                    btn_positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Dismiss the alert dialog

                            String hours = et_hours.getText().toString();
                            String minutes = et_minutes.getText().toString();
                            String payment = et_payment.getText().toString();
                            if(!hours.isEmpty() && !minutes.isEmpty() && !payment.isEmpty()) {
                                Integer addhours = Integer.parseInt(hours);
                                Integer addminutes = Integer.parseInt(minutes);
                                Integer amountMinutes = (addhours*60) + addminutes;
                                Float addPayment = Float.valueOf(payment);
                                editor.setAddTimeToWorkDay(amountMinutes,addPayment);
                                dialog.cancel();
                                onBackPressed();
                                finish();
                            }
                        }
                    });

                    // Set negative/no button click listener
                    btn_negative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            onBackPressed();
                            finish();
                        }
                    });

                    // Display the custom alert dialog on interface
                    dialog.show();
                }
            });

        switchClear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                    AlertDialog.Builder builder = new AlertDialog.Builder(FormForEditionalWorkDayActivity.this);
                    builder.setTitle("Clear all");
                    builder.setMessage("Are you sure?");
                    builder.setCancelable(false)

                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {

                                    editor.clearAll();
                                    dialog.dismiss();
                                    onBackPressed();
                                    finish();
                                }
                            });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                            onBackPressed();
                            finish();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }



        });

        switchVacation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (isChecked){

                        AlertDialog.Builder builder = new AlertDialog.Builder(FormForEditionalWorkDayActivity.this);
                        builder.setTitle("On vacation");
                        builder.setMessage("Are you sure?");
                        builder.setCancelable(false)

                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {

                                        editor.setVacationToWorkDay();
                                        dialog.dismiss();
                                        onBackPressed();
                                        finish();
                                    }
                                });

                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Do nothing
                                dialog.dismiss();
                                onBackPressed();
                                finish();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
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
