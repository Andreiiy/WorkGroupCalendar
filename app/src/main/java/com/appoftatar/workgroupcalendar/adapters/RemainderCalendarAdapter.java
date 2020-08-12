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

import com.appoftatar.workgroupcalendar.activity.InformationDayOfCalendarRemaindesActivity;
import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.calendar.WorkDayWithRemainder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


    public class RemainderCalendarAdapter extends RecyclerView.Adapter<RemainderCalendarAdapter.RemainderCalendarViewHolder> {


        private ArrayList<WorkDayWithRemainder> monthWithRemainders;
        private Context context;

        public RemainderCalendarAdapter(Context context, ArrayList<WorkDayWithRemainder> monthWithRemainders) {
            this.context = context;
            this.monthWithRemainders = monthWithRemainders;

        }

        @NonNull
        @Override
        public RemainderCalendarAdapter.RemainderCalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();

            int layoutIdForListItem = R.layout.item_remainder_calendar;

            LayoutInflater inflater = LayoutInflater.from(context);

            View view = inflater.inflate(layoutIdForListItem, parent, false);

            RemainderCalendarAdapter.RemainderCalendarViewHolder viewHolder = new RemainderCalendarAdapter.RemainderCalendarViewHolder(view, context);

            //viewHolder.listItemNumberView.setText();

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RemainderCalendarAdapter.RemainderCalendarViewHolder holder, int position) {
            holder.bind(monthWithRemainders.get(position), position);
        }

        @Override
        public int getItemCount() {
            return monthWithRemainders.size();
        }


        //////////////////////////////////////////////////////////

        class RemainderCalendarViewHolder extends RecyclerView.ViewHolder {

            TextView weekDay;
            TextView date;
            TextView tv_titleEvent;
            TextView tv_time;
            WorkDayWithRemainder workDayWithRemainder;
            LinearLayout dateLayaut;

            public RemainderCalendarViewHolder(@NonNull View itemView, final Context context) {
                super(itemView);
                final Date currentDate = new GregorianCalendar().getTime();
                weekDay = itemView.findViewById(R.id.tv_weekday);
                date = itemView.findViewById(R.id.tv_date);
                dateLayaut = itemView.findViewById(R.id.dateLayaut);
                tv_titleEvent = itemView.findViewById(R.id.tv_titleEvent);
                tv_time = itemView.findViewById(R.id.tv_time);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (workDayWithRemainder.getMonth() == monthWithRemainders.get(15).getMonth() &&
                                workDayWithRemainder.getDate() >= currentDate.getDate()) {
                            Intent intent = new Intent(context, InformationDayOfCalendarRemaindesActivity.class);
                            SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week abbreviated
                            intent.putExtra("DAY_WEEK", simpleDateformat.format(new Date(workDayWithRemainder.getYear(), workDayWithRemainder.getMonth(), workDayWithRemainder.getDate())));
                            intent.putExtra("DATE", Integer.toString(workDayWithRemainder.getDate()));
                            intent.putExtra("MONTH", Integer.toString(workDayWithRemainder.getMonth()));
                            context.startActivity(intent);
                        }
                    }
                });
            }

            void bind(WorkDayWithRemainder workDayWithRemainder, Integer position) {
                this.workDayWithRemainder = workDayWithRemainder;
                String[] weekDays = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
                if (position < 7)
                    weekDay.setText(String.valueOf(weekDays[position]));
                else weekDay.setHeight(0);

                Date curentDate = new GregorianCalendar().getTime();


                if (workDayWithRemainder.getMonth() == monthWithRemainders.get(15).getMonth() ) {
                    String day = String.valueOf(workDayWithRemainder.getDate());

                    if (workDayWithRemainder.getEvent() == null ||
                    workDayWithRemainder.getDate() < curentDate.getDate()) {
                        tv_time.setHeight(0);
                        tv_titleEvent.setHeight(0);
                    }

                        Calendar calendar = Calendar.getInstance();
                        Date date2 = new Date(workDayWithRemainder.getYear(), workDayWithRemainder.getMonth(), workDayWithRemainder.getDate());
                        calendar.setTime(date2);
                        Integer numberWeekDay = calendar.get(Calendar.DAY_OF_WEEK);
                        if (numberWeekDay == 7) {
                            date.setTextColor(Color.RED);
                        }
                        if (workDayWithRemainder.getDate() == curentDate.getDate() &&
                                workDayWithRemainder.getMonth() == curentDate.getMonth()) {

                            date.setBackgroundResource(R.drawable.rounded_textview);
                            dateLayaut.setBackgroundResource(R.drawable.border);
                        }

                        date.setText(String.valueOf(day));


                    } else {
                        tv_time.setHeight(0);
                        tv_titleEvent.setHeight(0);
                    }
                }


            }
        }


