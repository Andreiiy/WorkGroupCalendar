package com.appoftatar.workgroupcalendar.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appoftatar.workgroupcalendar.InforrmationDayOfCalendar;
import com.appoftatar.workgroupcalendar.calendar.ItemWorkCalendar;
import com.appoftatar.workgroupcalendar.calendar.MonthWorkCalendar;
import com.appoftatar.workgroupcalendar.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.NumberViewHolder>{


    private MonthWorkCalendar monthWorkCalendar;
    private Context context;

    public CalendarAdapter(Context context,MonthWorkCalendar monthWorkCalendar){
        this.context = context;
        this.monthWorkCalendar = monthWorkCalendar;

    }

    @NonNull
    @Override
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.number_list_item;

        LayoutInflater inflater = LayoutInflater.from(context);

       View view = inflater.inflate(layoutIdForListItem,parent,false);

       NumberViewHolder viewHolder = new NumberViewHolder(view,context);

       //viewHolder.listItemNumberView.setText();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder holder, int position) {
    holder.bind(monthWorkCalendar.getWorkDays().get(position),position);
    }

    @Override
    public int getItemCount() {
        return monthWorkCalendar.getWorkDays().size();
    }



    //////////////////////////////////////////////////////////

    class  NumberViewHolder extends RecyclerView.ViewHolder {

        TextView weekDay;
        TextView date;
        TextView tv_titleOnVvacation;
        TextView tv_titleSick;
        TextView tv_employesOnVacation;
        TextView tv_employesSick;
        ItemWorkCalendar workDay;
        LinearLayout dateLayaut;
        public NumberViewHolder(@NonNull View itemView, final Context context) {
            super(itemView);


            weekDay = itemView.findViewById(R.id.tv_weekday);
            date = itemView.findViewById(R.id.tv_date);
            dateLayaut = itemView.findViewById(R.id.dateLayaut);
            tv_employesOnVacation = itemView.findViewById(R.id.tv_employesOnVacation);
            tv_employesSick = itemView.findViewById(R.id.tv_employesSick);
            tv_titleOnVvacation = itemView.findViewById(R.id.tv_titleOnVvacation);
            tv_titleSick = itemView.findViewById(R.id.tv_titleSick);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(workDay.getWorkersOnVacation().size()!=0 || workDay.getWorkersSick().size()!=0) {
                        Intent intent = new Intent(context, InforrmationDayOfCalendar.class);
                        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week abbreviated
                        intent.putExtra("DAY_WEEK", simpleDateformat.format(workDay.getDate()));
                        intent.putExtra("DATE", Integer.toString(workDay.getDate().getDate()));
                        intent.putExtra("VacationList", workDay.getWorkersOnVacation());
                        intent.putExtra("SickList", workDay.getWorkersSick());
                        context.startActivity(intent);
                    }
                }
            });
        }

        void bind(ItemWorkCalendar workDay,Integer position) {
            this.workDay = workDay;
            String[] weekDays = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
            if (position < 7)
                weekDay.setText(String.valueOf(weekDays[position]));
            else weekDay.setHeight(0);

            Date curentDate = new GregorianCalendar().getTime();


             if(workDay.getDate().getMonth() == monthWorkCalendar.getWorkDays().get(15).getDate().getMonth()) {
                 String day = String.valueOf(workDay.getDate().getDate());

                 if(workDay.getWorkersOnVacation().size() != 0) {
                     String employee = workDay.getWorkersOnVacation().get(0);
                     for (int i = 1; i < workDay.getWorkersOnVacation().size();i++){
                         employee = employee+"\n"+workDay.getWorkersOnVacation().get(i);
                     }
                     tv_employesOnVacation.setText(employee);
                     dateLayaut.setBackgroundResource(R.drawable.border_lite);
                 }else
                     tv_titleOnVvacation.setHeight(0);


                 if(workDay.getWorkersSick().size() != 0) {
                     String employeeSick = workDay.getWorkersSick().get(0);
                     for (int i = 1; i < workDay.getWorkersSick().size();i++){
                         employeeSick = employeeSick+"\n"+workDay.getWorkersSick().get(i);
                     }
                     tv_employesSick.setText(employeeSick);
                     dateLayaut.setBackgroundResource(R.drawable.border_lite);
                 }else
                     tv_titleSick.setHeight(0);


                 Calendar calendar = Calendar.getInstance();
                 calendar.setTime(workDay.getDate());
                 Integer numberWeekDay = calendar.get(Calendar.DAY_OF_WEEK);
                 if(numberWeekDay == 7 ) {
                     date.setTextColor(Color.RED);
                 }
                 if(workDay.getDate().getDate() == curentDate.getDate() &&
                         workDay.getDate().getMonth() == curentDate.getMonth()) {

                     date.setBackgroundResource(R.drawable.rounded_textview);
                     dateLayaut.setBackgroundResource(R.drawable.border);
                 }

                 date.setText(String.valueOf(day));


             }else{  tv_titleOnVvacation.setHeight(0);
                 tv_titleSick.setHeight(0);}
        }


    }

}
