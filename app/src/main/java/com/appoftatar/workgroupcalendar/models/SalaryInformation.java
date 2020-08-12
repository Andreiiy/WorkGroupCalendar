package com.appoftatar.workgroupcalendar.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.calendar.WorkDay;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class SalaryInformation {

   private ArrayList<SalaryInformationOfMonth> salaryInformationOfYear;

   public SalaryInformation()  {
      salaryInformationOfYear = new ArrayList<>();

   }

   public void createSalaryInformationOfYear(final SalaryInformation.SalaryInformationObserver observer){
      DatabaseReference root = FirebaseDatabase.getInstance().getReference();
      final Date currentDate = new GregorianCalendar().getTime();
      root.child("workdays").child(Integer.toString(currentDate.getYear()+1900))
              .addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            for (DataSnapshot monthSnapshot: dataSnapshot.getChildren()) {
               ArrayList<WorkDay> workDaysList = new ArrayList<>();
            for (DataSnapshot daySnapshot: monthSnapshot.getChildren()) {
               WorkDay workDay = new WorkDay();
               workDay =  daySnapshot.child(Common.currentUser.IdManager).child(Common.currentUser.IdWorkGroup)
                       .child(Common.currentUser.ID).getValue(WorkDay.class);

               if(workDay != null)
                  workDaysList.add(workDay);
            }

               if(workDaysList .size()!=0) {
                 salaryInformationOfYear.add(getSalaryInformationOfMonth(workDaysList));
               }
            }
            if(salaryInformationOfYear.size()!=0)
               observer.ShowChart(salaryInformationOfYear);

            if (getSalaryInformationOfMonth(currentDate.getMonth())!= null)
               observer.ShowInformationSalaryOfMonth(getSalaryInformationOfMonth(currentDate.getMonth()));

         }
         @Override
         public void onCancelled(DatabaseError databaseError) {
            Log.w("Data base", "Failed to read value.", databaseError.toException());
         }
      });
   }
   private SalaryInformationOfMonth getSalaryInformationOfMonth(ArrayList<WorkDay> workDaysList) {

      if (workDaysList.size() != 0 ) {
         Date currentDate = new GregorianCalendar().getTime();
         SalaryInformationOfMonth salaryInformationOfMonth = new SalaryInformationOfMonth();
         salaryInformationOfMonth.setNumberMonth(workDaysList.get(0).getDate().getMonth());
         ///////////////////////////////////////////////////////////////////////////////////////////////
         for (WorkDay workday : workDaysList) {
            if(workday.getDate().before(currentDate) || workday.getDate().compareTo(currentDate)==0) {
               if (workday.getShift() != null && workday.getShift().equals("1")) {
                  if (salaryInformationOfMonth.getTimeShift1() != null) {
                     salaryInformationOfMonth.setTimeShift1(salaryInformationOfMonth.getTimeShift1()
                             + workday.getAmountMinutes());
                     salaryInformationOfMonth.setPaymentShift1(salaryInformationOfMonth.getPaymentShift1()
                             + workday.getAmountMinutes() * workday.getMoneyOfShift());
                  } else {
                     salaryInformationOfMonth.setTimeShift1((workday.getAmountMinutes().floatValue()));
                     salaryInformationOfMonth.setPaymentShift1(workday.getAmountMinutes() * workday.getMoneyOfShift());
                  }
               } else if (workday.getShift() != null && workday.getShift().equals("2")) {
                  if (salaryInformationOfMonth.getTimeShift2() != null) {
                     salaryInformationOfMonth.setTimeShift2(salaryInformationOfMonth.getTimeShift2()
                             + workday.getAmountMinutes());
                     salaryInformationOfMonth.setPaymentShift2(salaryInformationOfMonth.getPaymentShift2()
                             + workday.getAmountMinutes() * workday.getMoneyOfShift());
                  } else {
                     salaryInformationOfMonth.setTimeShift2(workday.getAmountMinutes().floatValue());
                     salaryInformationOfMonth.setPaymentShift2(workday.getAmountMinutes() * workday.getMoneyOfShift());
                  }
               } else if (workday.getShift() != null && workday.getShift().equals("3")) {
                  if (salaryInformationOfMonth.getTimeShift3() != null) {
                     salaryInformationOfMonth.setTimeShift3(salaryInformationOfMonth.getTimeShift3()
                             + workday.getAmountMinutes());
                     salaryInformationOfMonth.setPaymentShift3(salaryInformationOfMonth.getPaymentShift3()
                             + workday.getAmountMinutes() * workday.getMoneyOfShift());
                  } else {
                     salaryInformationOfMonth.setTimeShift3(workday.getAmountMinutes().floatValue());
                     salaryInformationOfMonth.setPaymentShift3(workday.getAmountMinutes() * workday.getMoneyOfShift());
                  }
               } else if (workday.getShift() != null && workday.getShift().equals("Vacation")) {
                  if (salaryInformationOfMonth.getVacationPay() != null) {
                     salaryInformationOfMonth.setVacationPay(salaryInformationOfMonth.getVacationPay()
                             + workday.getAmountMinutes() * workday.getMoneyOfShift());
                  } else {

                     salaryInformationOfMonth.setVacationPay(workday.getAmountMinutes() * workday.getMoneyOfShift());
                  }
               }
               if (salaryInformationOfMonth.getAdditionalTime() != null && workday.getAmountAdditionalMinutes() != null) {
                  salaryInformationOfMonth.setAdditionalTime(salaryInformationOfMonth.getAdditionalTime()
                          + workday.getAmountAdditionalMinutes());
                  salaryInformationOfMonth.setAdditionalPayment(salaryInformationOfMonth.getAdditionalPayment()
                          + workday.getMoneyOfAdditionalTime());
               } else if (workday.getAmountAdditionalMinutes() != null) {
                  salaryInformationOfMonth.setAdditionalTime(workday.getAmountAdditionalMinutes().floatValue());
                  salaryInformationOfMonth.setAdditionalPayment(workday.getMoneyOfAdditionalTime());
               }
            }
         }
         /////////////////////////////////////////////////////////////////////////////////////////////////
         Float paymentMonth = 0f;
         if (salaryInformationOfMonth.getPaymentShift1() != null)
            paymentMonth += salaryInformationOfMonth.getPaymentShift1();
         if (salaryInformationOfMonth.getPaymentShift2() != null)
            paymentMonth += salaryInformationOfMonth.getPaymentShift2();
         if (salaryInformationOfMonth.getPaymentShift3() != null)
            paymentMonth += salaryInformationOfMonth.getPaymentShift3();
         if (salaryInformationOfMonth.getVacationPay() != null)
            paymentMonth += salaryInformationOfMonth.getVacationPay();
         if (salaryInformationOfMonth.getAdditionalPayment() != null)
            paymentMonth += Float.valueOf(salaryInformationOfMonth.getAdditionalPayment());
         salaryInformationOfMonth.setPaymentMonth(paymentMonth);

         return salaryInformationOfMonth;
      }
      return null;
   }




   public SalaryInformationOfMonth getSalaryInformationOfMonth(Integer numberMonth){
      for(SalaryInformationOfMonth month: salaryInformationOfYear)
         if(month.getNumberMonth() == numberMonth)
            return month;
     return null;
   }

   public interface SalaryInformationObserver{
     public void ShowInformationSalaryOfMonth(SalaryInformationOfMonth salaryInformationOfMonth);
     public void ShowChart(ArrayList<SalaryInformationOfMonth> salaryInformationOfYear);
   }
}

