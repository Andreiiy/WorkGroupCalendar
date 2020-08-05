package com.appoftatar.workgroupcalendar.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MyCalendar {

    private ArrayList<Date> curentMonth;
    public Date curentDate;

    private int countDaysInManth;

    public MyCalendar(){
        curentDate = new GregorianCalendar().getTime();
        curentMonth = new ArrayList<>();

    }

    public int getNumberCurrentMonth()
    {
        return curentDate.getMonth();
    }

    public int getDayOfWeekOfFirstDayOfMonth(int numberMonth){

        Date firstDay = new Date(curentDate.getYear(), numberMonth, 1);

        //Get number day of week
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstDay);
        int weekDayOfFirstDay = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDayOfFirstDay;
    }

    public ArrayList<Date> getCurrentMonth(){

            curentMonth = getMonth(curentDate.getMonth());
            return curentMonth;
    }


    public ArrayList<Date> getMonth(int numberMonth){

        ArrayList<Date> month = new ArrayList<>();

        int dayOfWeekOfFirstDayOfMonth =  getDayOfWeekOfFirstDayOfMonth(numberMonth);
        for(int i = 0;i < dayOfWeekOfFirstDayOfMonth-1;i++)
            month.add(new Date(curentDate.getYear(), numberMonth-1, 1));


        Date firstDay = new Date(curentDate.getYear(), numberMonth, 1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstDay);

        //Get count days at month
        countDaysInManth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);


        Date date;
        for (int i = 0; i < countDaysInManth; i++) {
            date = calendar.getTime();
            month.add(date);
            calendar.add(Calendar.DAY_OF_YEAR, 1);


        }
        return month;
    }
}