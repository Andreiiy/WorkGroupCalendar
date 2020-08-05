package com.appoftatar.workgroupcalendar.ui.manhourses;

import android.app.NotificationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.models.ManHoursInformationOfMonth;
import com.appoftatar.workgroupcalendar.models.ManHoursesCalculator;
import com.appoftatar.workgroupcalendar.models.SalaryInformationOfMonth;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ManHoursesFragment extends Fragment implements ManHoursesCalculator.ManHoursInformationObserver{


    View root;
    private FirebaseAuth mAuth;
    private boolean actionActivity;


    private TextView tvMonth;
    private TextView tvHoursShift,tvAddHours,tvTotalHours;
    private Button btnPreviosMonth;
    private Button btnNextMonth;
    private BarChart chart;
    private LineChart chart2;
    private Integer usedMonth;
    ManHoursesCalculator manHoursesCalculator;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_man_hourses, container, false);

        initViews();

        usedMonth = new GregorianCalendar().getTime().getMonth();

        manHoursesCalculator = new ManHoursesCalculator(this);
        manHoursesCalculator.createSalaryInformationOfYear();




        btnPreviosMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usedMonth--;
                ShowManHoursInformationOfMonth(manHoursesCalculator.getInformationOfMonth(usedMonth));
            }
        });
        btnNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usedMonth++;
                ShowManHoursInformationOfMonth(manHoursesCalculator.getInformationOfMonth(usedMonth));
            }
        });
        return root;
    }

    private void initViews(){
        chart = root.findViewById(R.id.barchart);
        chart2 = root.findViewById(R.id.barchart2);
        tvMonth = (TextView)root.findViewById(R.id.tvMonth);
        tvHoursShift = (TextView)root.findViewById(R.id.tvHoursShift);
        tvAddHours = (TextView)root.findViewById(R.id.tvAddHours);
        tvTotalHours = (TextView)root.findViewById(R.id.tvTotalHours);

        btnPreviosMonth = (Button) root.findViewById(R.id.btnPreviosMonth);
        btnNextMonth = (Button) root.findViewById(R.id.btnNextMonth);
    }

    @Override
    public void ShowManHoursInformationOfMonth(ManHoursInformationOfMonth manHoursInformationOfMonth) {

            Calendar calendar = Calendar.getInstance();
            Date date = new GregorianCalendar(new Date().getYear(), usedMonth, 1).getTime();
            calendar.setTime(date);
            String nameMonth = calendar.getDisplayName(calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
            tvMonth.setText(nameMonth);
        if(manHoursInformationOfMonth!=null) {
            Integer hoursShift = manHoursInformationOfMonth.getTimeShifts();
            hoursShift = hoursShift != null ? hoursShift : 0;
            Integer hoursAdditional = manHoursInformationOfMonth.getAdditionalTime();
            hoursAdditional = hoursAdditional != null ? hoursAdditional : 0;


            if (hoursShift != 0)
                tvHoursShift.setText(new StringBuilder()
                        .append(hoursShift / 60)
                        .append("h ")
                        .append(hoursShift % 60)
                        .append("m"));
            if (hoursAdditional != 0) {
                tvAddHours.setText(new StringBuilder()
                        .append(hoursAdditional / 60)
                        .append("h ")
                        .append(hoursAdditional % 60)
                        .append("m"));
            }
            tvTotalHours.setText(new StringBuilder()
                    .append((hoursShift + hoursAdditional) / 60)
                    .append("h ")
                    .append((hoursShift + hoursAdditional) % 60)
                    .append("m"));
        }else {
            tvHoursShift.setText("0");
            tvAddHours.setText("0");
            tvTotalHours.setText("0");
        }
    }

    @Override
    public void ShowCharts(ArrayList<ManHoursInformationOfMonth> manHoursInformationOfYear) {
        ArrayList workYear = new ArrayList();

        for(int i = 1; i < 13; i++) {
            if(findMonth(i,manHoursInformationOfYear)!=null) {
                ManHoursInformationOfMonth month = findMonth(i,manHoursInformationOfYear);
                if(month.getAdditionalTime()==null)
                    month.setAdditionalTime(0);
                if(month.getTimeShifts()==null)
                    month.setTimeShifts(0);
                float manHourses = (month.getTimeShifts()+month.getAdditionalTime())/60;
                float minutes = (float) (((month.getTimeShifts()+month.getAdditionalTime())%60))/100;
                manHourses =manHourses + minutes;
                workYear.add(new BarEntry(i, manHourses));
            } else {
                workYear.add(new BarEntry(i,0f));
            }

        }

        BarDataSet bardataset = new BarDataSet(workYear, "");
        ILineDataSet lineDataSet = new LineDataSet(workYear,"");
        chart.animateY(5000);
        chart.animateX(5000);
        chart2.animateY(5000);
        chart2.animateX(5000);
        BarData data = new BarData( bardataset);
        LineData lineData = new LineData( lineDataSet);

        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        bardataset.setValueTextSize(8);
        chart.setData(data);
        chart2.setData(lineData);

    }


    private ManHoursInformationOfMonth findMonth(int numberMonth, ArrayList<ManHoursInformationOfMonth> salaryInformationOfYear){
        Date currentDate = new GregorianCalendar().getTime();
        for(ManHoursInformationOfMonth month:salaryInformationOfYear)
            if (month.getNumberMonth()+1 == numberMonth && month.getNumberMonth() <= currentDate.getMonth())
                return month;
        return null;
    }
}