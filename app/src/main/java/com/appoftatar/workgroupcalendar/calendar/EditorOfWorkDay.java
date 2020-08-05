package com.appoftatar.workgroupcalendar.calendar;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.SigninActivity;
import com.appoftatar.workgroupcalendar.StatusSelectionActivity;
import com.appoftatar.workgroupcalendar.models.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class EditorOfWorkDay {

        private WorkDay workDay;
        private User user;
        public EditorOfWorkDay(User user , WorkDay workDay) {
            this.workDay = workDay;
            this.user = user;
        }

    public void setSickToWorkDay () {

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(workDay.getDate());
        WorkDay workday = new WorkDay();
        workday = new WorkDay();
        workday.setShift("Sick");
        workday.setIdEmployee(Common.currentUser.ID);
        workday.setDate(workDay.getDate());
        workday.setMoneyOfShift(0f);
        workday.setAmountMinutes(0);
        workday.setAmountAdditionalMinutes(0);
        workday.setMoneyOfAdditionalTime(0f);

        root.child("workdays").child(Integer.toString(calendar.getTime().getYear() + 1900))
                .child(Integer.toString(workDay.getDate().getMonth())).child(Integer.toString(workDay.getDate().getDate()))
                .child(user.IdManager).child(user.IdWorkGroup)
                .child(user.ID)
                .setValue(workday);
    }

    public void setVacationToWorkDay () {

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(workDay.getDate());
        WorkDay workday = new WorkDay();
        workday.setIdEmployee(Common.currentUser.ID);
        workday.setDate(workDay.getDate());
        workday.setShift("Vacation");
        workday.setMoneyOfShift(user.additionalInformation.getPaymentOfMinuteShift1());
        workday.setAmountMinutes(user.additionalInformation.getMinutesShift1());
        workday.setAmountAdditionalMinutes(0);
        workday.setMoneyOfAdditionalTime(0f);

        root.child("workdays").child(Integer.toString(calendar.getTime().getYear() + 1900))
                .child(Integer.toString(workDay.getDate().getMonth())).child(Integer.toString(workDay.getDate().getDate()))
                .child(user.IdManager).child(user.IdWorkGroup)
                .child(Common.currentUser.ID)
                .setValue(workday);
    }
    public void setShiftToWorkDay (final Integer numberShift) {

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(workDay.getDate());

        root.child("workdays").child(Integer.toString(calendar.getTime().getYear() + 1900))
                .child(Integer.toString(workDay.getDate().getMonth())).child(Integer.toString(workDay.getDate().getDate()))
                .child(user.IdManager).child(user.IdWorkGroup)
                .child(user.ID+"/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    WorkDay workday = new WorkDay();
                    workday.setDate(workDay.getDate());
                    workday.setIdEmployee(Common.currentUser.ID);
                    workday.setShift(numberShift.toString());
                    workday.setAmountMinutes(user.additionalInformation.getMinutesShift(numberShift));
                    workday.setMoneyOfShift(Common.currentUser.additionalInformation.getPaymentOfMinuteShift(numberShift));

                   if(dataSnapshot.child("amountAdditionalMinutes").getValue() != null)
                        workday.setAmountAdditionalMinutes(Integer.parseInt(dataSnapshot.child("amountAdditionalMinutes").getValue().toString()));
                    if(dataSnapshot.child("moneyOfAdditionalTime").getValue() != null)
                        workday.setMoneyOfAdditionalTime( Float.parseFloat(dataSnapshot.child("moneyOfAdditionalTime").getValue().toString()));
                    dataSnapshot.getRef().setValue(workday);
                }else {
                    WorkDay workday = new WorkDay();
                    workday.setDate(workDay.getDate());
                    workday.setIdEmployee(Common.currentUser.ID);
                    workday.setShift(numberShift.toString());
                    workday.setAmountMinutes(user.additionalInformation.getMinutesShift(numberShift));
                    workday.setMoneyOfShift(Common.currentUser.additionalInformation.getPaymentOfMinuteShift(numberShift));
                    dataSnapshot.getRef().setValue(workday);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
   }

    public void setAddTimeToWorkDay (final Integer amountAdditionalMinutes, final Float additionalPayment) {

        final DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(workDay.getDate());

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("workdays/"
                +Integer.toString(calendar.getTime().getYear() + 1900)+"/"
                +Integer.toString(workDay.getDate().getMonth())+"/"
                +Integer.toString(workDay.getDate().getDate())+"/"
                +user.IdManager+"/"
                +user.IdWorkGroup+"/"
                +user.ID + "/");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                 dataSnapshot.getRef().child("amountAdditionalMinutes")
                         .setValue(amountAdditionalMinutes);
                    dataSnapshot.getRef().child("moneyOfAdditionalTime")
                            .setValue(additionalPayment);
                }else {
                       WorkDay workday = new WorkDay();
                       workday.setDate(workDay.getDate());
                       workday.setIdEmployee(Common.currentUser.ID);
                       workday.setAmountAdditionalMinutes(amountAdditionalMinutes);
                       workday.setMoneyOfAdditionalTime(additionalPayment);
                    root.child("workdays").child(Integer.toString(calendar.getTime().getYear() + 1900))
                            .child(Integer.toString(workDay.getDate().getMonth())).child(Integer.toString(workDay.getDate().getDate()))
                            .child(user.IdManager).child(user.IdWorkGroup)
                            .child(user.ID).setValue(workday);
                }
                    }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    public void clearAll() {
        final DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(workDay.getDate());

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("workdays/"
                +Integer.toString(calendar.getTime().getYear() + 1900)+"/"
                +Integer.toString(workDay.getDate().getMonth())+"/"
                +Integer.toString(workDay.getDate().getDate())+"/"
                +user.IdManager+"/"
                +user.IdWorkGroup+"/"
                +user.ID + "/");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataSnapshot.getRef().setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
}
