package com.appoftatar.workgroupcalendar.adapters;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.Date;

public class CalendarRequestsAdapter extends RecyclerView.Adapter<CalendarRequestsAdapter.NumberViewHolder>{


private ArrayList<Date>  month;
private int weekDayOfFirstDay;
public CalendarRequestsAdapter(ArrayList<Date>  month,int weekDayOfFirstDay){
        this.month = month;
        this.weekDayOfFirstDay = weekDayOfFirstDay;
        }

@NonNull
@Override
public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.item_list_calendar_requests;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem,parent,false);

        NumberViewHolder viewHolder = new NumberViewHolder(view,context);

        //viewHolder.listItemNumberView.setText();

        return viewHolder;
        }

@Override
public void onBindViewHolder(@NonNull NumberViewHolder holder, int position) {
        holder.bind(position,month.get(position));
        }

@Override
public int getItemCount() {
        return month.size();
        }



//////////////////////////////////////////////////////////

class  NumberViewHolder extends RecyclerView.ViewHolder {

    TextView weekDay;
    TextView tvDate;
    Date date;
    Date curentDate;
    LinearLayout dateLayaut;
    public NumberViewHolder(@NonNull View itemView, final Context context) {
        super(itemView);


        weekDay = itemView.findViewById(R.id.tv_weekday);
        tvDate = itemView.findViewById(R.id.tv_date);
        dateLayaut = itemView.findViewById(R.id.dateLayaut);
        curentDate = new Date();
        date = new Date();

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(date.compareTo(curentDate) == 0 || date.compareTo(curentDate) < 0)
                    Toast.makeText(context, "You can not choose this date", Toast.LENGTH_SHORT).show();
                else {
                    dateLayaut.setBackgroundResource(R.color.colorRedLight);
                    Common.holidayRequest.setDateToList(date);
                }
            }
        });

    }

    void bind(int position, Date date) {
        this.date = date;
        String[] weekDays = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        if (position < 7)
            weekDay.setText(String.valueOf(weekDays[position]));
        else weekDay.setHeight(0);

        if(position >= weekDayOfFirstDay-1 && position<=month.size()-1) {

            String day = String.valueOf(date.getDate());
            if(date.getDay()==6 ) {
                tvDate.setTextColor(Color.RED);
            }
            if(date.getDate() == curentDate.getDate() && date.getMonth() == curentDate.getMonth()) {
                tvDate.setBackgroundResource(R.drawable.rounded_textview);
                dateLayaut.setBackgroundResource(R.drawable.border);

            }

            tvDate.setText(String.valueOf(day));


        }
    }

}

}
