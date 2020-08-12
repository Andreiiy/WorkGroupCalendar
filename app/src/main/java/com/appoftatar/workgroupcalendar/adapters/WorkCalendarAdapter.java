package com.appoftatar.workgroupcalendar.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.activity.activityForEmployee.WorkDayOfCalendarActivity;
import com.appoftatar.workgroupcalendar.calendar.MonthWorkCalendar;
import com.appoftatar.workgroupcalendar.calendar.WorkDay;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class WorkCalendarAdapter extends RecyclerView.Adapter<WorkCalendarAdapter.WorkCalendarViewHolder>{

    private MonthWorkCalendar monthWorkCalendar;
    private Context context;
    public WorkCalendarAdapter(Context context, MonthWorkCalendar monthWorkCalendar) {
        this.monthWorkCalendar = monthWorkCalendar;
    }

    @NonNull
    @Override
    public WorkCalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.item_list_work_calendar;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem,parent,false);

        WorkCalendarAdapter.WorkCalendarViewHolder viewHolder = new WorkCalendarAdapter.WorkCalendarViewHolder(view,context);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkCalendarViewHolder holder, int position) {
        holder.bind(monthWorkCalendar.getWorkDaysWithShifts().get(position),position);
    }

    @Override
    public int getItemCount() {
        return monthWorkCalendar.getWorkDaysWithShifts().size();
    }


    public class WorkCalendarViewHolder extends RecyclerView.ViewHolder{
        TextView weekDay;
        TextView date;
        TextView tv_titleShift;
        TextView tv_numberShift;
        TextView tv_titleTimeShift;
        TextView tv_timeShift;
        TextView tv_titleAddTime;
        TextView tv_Addtime;
        LinearLayout dateLayaut;
        private WorkDay workDay;
        private Context context;
        public WorkCalendarViewHolder(@NonNull View itemView, final Context context) {
            super(itemView);
            this.context = context;
            weekDay = itemView.findViewById(R.id.tv_weekday);
            date = itemView.findViewById(R.id.tv_date);
            dateLayaut = itemView.findViewById(R.id.dateLayaut);
            tv_titleShift = itemView.findViewById(R.id.tv_titleShift);
            tv_numberShift = itemView.findViewById(R.id.tv_numberShift);
            tv_titleTimeShift = itemView.findViewById(R.id.tv_titleTimeShift);
            tv_timeShift = itemView.findViewById(R.id.tv_timeShift);
            tv_titleAddTime = itemView.findViewById(R.id.tv_titleAddTime);
            tv_Addtime = itemView.findViewById(R.id.tv_Addtime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Common.currentUser.additionalInformation == null || Common.currentUser.additionalInformation.getMinutesShift1()==0
                            && Common.currentUser.additionalInformation.getMinutesShift2()==0
                            && Common.currentUser.additionalInformation.getMinutesShift3()==0){
                        Toast.makeText(context, context.getResources().getText(R.string.fill_salaryData), Toast.LENGTH_LONG).show();
                    }else {
                    Date currentDate = new GregorianCalendar().getTime();
                    if(workDay.getDate().compareTo(currentDate)<=0 && workDay.getDate().getMonth()==monthWorkCalendar.getNumberMonth()) {
                        Intent intent = new Intent(context, WorkDayOfCalendarActivity.class);
                        intent.putExtra(WorkDay.class.getSimpleName(), workDay);
                        context.startActivity(intent);
                    }
                    }
                }
            });
        }

        public void bind(WorkDay workDay, int position) {
            this.workDay = workDay;
            String[] weekDays = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
            if (position < 7)
                weekDay.setText(String.valueOf(weekDays[position]));
            else weekDay.setHeight(0);

            Date curentDate = new Date();


            if(workDay.getDate().getMonth() == monthWorkCalendar.getWorkDays().get(15).getDate().getMonth()) {
                String day = String.valueOf(workDay.getDate().getDate());
                date.setText(String.valueOf(day));

                if(workDay.getShift()!=null){
                    if(workDay.getAmountMinutes()!=null && workDay.getAmountMinutes()!=0)
                    dateLayaut.setBackgroundResource(R.drawable.border);
                   if(workDay.getShift().equals("Sick")){
                       tv_titleShift.setText(context.getResources().getString(R.string.tv_sick));
                       tv_numberShift.setBackgroundColor(Color.RED);
                       tv_titleShift.setTextColor(Color.RED);
                       tv_titleTimeShift.setHeight(0);
                       tv_timeShift .setHeight(0);
                       tv_titleAddTime.setHeight(0);
                       tv_Addtime.setHeight(0);

                   }else if(workDay.getShift().equals("Vacation")){
                       tv_titleShift.setText(context.getString(R.string.tv_vacation));
                       tv_numberShift.setBackgroundColor(Color.YELLOW);
                       tv_timeShift .setText(Integer.toString(workDay.getAmountMinutes()/60)+"."+Integer.toString(workDay.getAmountMinutes()%60));
                       if(workDay.getAmountAdditionalMinutes()==null) {
                           tv_titleAddTime.setHeight(0);
                           tv_Addtime.setHeight(0);
                       }else tv_Addtime.setText(Integer.toString(workDay.getAmountAdditionalMinutes()/60)+"."+Integer.toString(workDay.getAmountAdditionalMinutes()%60));
                   }else {
                       if(workDay.getShift().equals("1"))
                           tv_numberShift.setBackgroundColor(context.getResources().getColor(R.color.colorAccentGreenLite));
                       else if(workDay.getShift().equals("2"))
                           tv_numberShift.setBackgroundColor(context.getResources().getColor(R.color.colorOrange));
                       else tv_numberShift.setBackgroundColor(Color.BLUE);
                       if(workDay.getAmountMinutes()==null || workDay.getAmountMinutes()==0){
                           tv_titleShift.setHeight(0);
                           tv_numberShift.setHeight(0);
                           tv_titleTimeShift.setHeight(0);
                           tv_timeShift .setHeight(0);
                       }else {
                           tv_numberShift.setText(workDay.getShift());
                           tv_timeShift.setText(Integer.toString(workDay.getAmountMinutes() / 60) + "." + Integer.toString(workDay.getAmountMinutes() % 60));
                       }
                       if(workDay.getAmountAdditionalMinutes()!=null)
                       tv_Addtime.setText(Integer.toString(workDay.getAmountAdditionalMinutes()/60)+"."+Integer.toString(workDay.getAmountAdditionalMinutes()%60));
                   }
                }else if(workDay.getAmountAdditionalMinutes()!=null && workDay.getAmountAdditionalMinutes() != 0) {
                    dateLayaut.setBackgroundResource(R.drawable.border);
                    tv_titleShift.setHeight(0);
                    tv_numberShift.setHeight(0);
                    tv_titleTimeShift.setHeight(0);
                    tv_timeShift .setHeight(0);
                    tv_Addtime.setText(Integer.toString(workDay.getAmountAdditionalMinutes()/60)+"."+Integer.toString(workDay.getAmountAdditionalMinutes()%60));
                }else{
                    tv_titleShift.setHeight(0);
                    tv_numberShift.setHeight(0);
                    tv_titleTimeShift.setHeight(0);
                    tv_timeShift .setHeight(0);
                    tv_titleAddTime.setHeight(0);
                    tv_Addtime.setHeight(0);
                }




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
                if(workDay.getAmountAdditionalMinutes()==null || workDay.getAmountAdditionalMinutes() == 0){
                    tv_Addtime.setHeight(0);
                    tv_titleAddTime.setHeight(0);
                }
            }else{
                tv_titleShift.setHeight(0);
                tv_numberShift.setHeight(0);
                tv_titleTimeShift.setHeight(0);
                tv_timeShift .setHeight(0);
                tv_titleAddTime.setHeight(0);
                tv_Addtime.setHeight(0);
            }


        }
    }
}
