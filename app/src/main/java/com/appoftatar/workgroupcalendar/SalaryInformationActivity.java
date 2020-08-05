package com.appoftatar.workgroupcalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.appoftatar.workgroupcalendar.Common.Common;
import com.appoftatar.workgroupcalendar.calendar.MyCalendar;
import com.appoftatar.workgroupcalendar.models.SalaryInformation;
import com.appoftatar.workgroupcalendar.models.SalaryInformationOfMonth;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class SalaryInformationActivity extends AppCompatActivity implements SalaryInformation.SalaryInformationObserver {
    private TextView tvMonth;
    private TextView tvTime1,tvTime2,tvTime3;
    private TextView tvPay1,tvPay2,tvPay3;
    private TextView tvAddTime;
    private TextView tvAddPayment;
    private TextView tvPaymentMonth;
    private TextView tvVacationPay;
    private Button btnPreviosMonth;
    private Button btnNextMonth;
    private BarChart chart;
    private Integer usedMonth;
    private int numberCurrentMonth;
    SalaryInformation salaryInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_information);
        salaryInformation = new SalaryInformation();

                tvMonth = (TextView)findViewById(R.id.tvMonth);
                tvTime1 = (TextView)findViewById(R.id.tvTime1);
                tvTime2 = (TextView)findViewById(R.id.tvTime2);
                tvTime3 = (TextView)findViewById(R.id.tvTime3);
                tvPay1 = (TextView)findViewById(R.id.tvPay1);
                tvPay2 = (TextView)findViewById(R.id.tvPay2);
                tvPay3 = (TextView)findViewById(R.id.tvPay3);
                tvAddTime = (TextView)findViewById(R.id.tvAddTime);
                tvVacationPay = (TextView)findViewById(R.id.tvVacationPay);
                tvAddPayment = (TextView)findViewById(R.id.tvAddPayment);
                tvPaymentMonth = (TextView)findViewById(R.id.tvPaymentMonth);

                btnPreviosMonth = (Button)findViewById(R.id.btnPreviosMonth);
                btnPreviosMonth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            usedMonth -= 1;
                            showSalaryInformation(salaryInformation.getSalaryInformationOfMonth(usedMonth));

                    }
                });
                btnNextMonth = (Button)findViewById(R.id.btnNextMonth);
                btnNextMonth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    if(usedMonth+1 <= numberCurrentMonth){
                        usedMonth += 1;
                        showSalaryInformation(salaryInformation.getSalaryInformationOfMonth(usedMonth));

                    }
                    }
                });
                chart = findViewById(R.id.barchart);


                Date currentDate = new GregorianCalendar().getTime();
                numberCurrentMonth = currentDate.getMonth();
                usedMonth = currentDate.getMonth();
                salaryInformation.createSalaryInformationOfYear(this);
    }

    @Override
    public void ShowInformationSalaryOfMonth(SalaryInformationOfMonth salaryInformationOfMonth) {

        showSalaryInformation(salaryInformationOfMonth);

    }

    @Override
    public void ShowChart(ArrayList<SalaryInformationOfMonth> salaryInformationOfYear) {
        ArrayList workYear = new ArrayList();

        for(int i = 1; i < 13; i++) {
            if(findMonth(i,salaryInformationOfYear)!=null) {
                    workYear.add(new BarEntry(i, findMonth(i,salaryInformationOfYear).getPaymentMonth()));
            } else {
                workYear.add(new BarEntry(i,0f));
            }

        }


        BarDataSet bardataset = new BarDataSet(workYear, "");
        chart.animateY(5000);
        chart.animateX(5000);
        BarData data = new BarData( bardataset);

        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        bardataset.setValueTextSize(8);
        chart.setData(data);
    }

    private SalaryInformationOfMonth findMonth(int numberMonth, ArrayList<SalaryInformationOfMonth> salaryInformationOfYear){
        Date currentDate = new GregorianCalendar().getTime();
        for(SalaryInformationOfMonth month:salaryInformationOfYear)
            if (month.getNumberMonth()+1 == numberMonth && month.getNumberMonth() <= currentDate.getMonth())
                return month;
        return null;
    }

    private void showSalaryInformation(SalaryInformationOfMonth salaryInformationOfMonth){
        Date currentDate = new GregorianCalendar().getTime();

        if(salaryInformationOfMonth != null && salaryInformationOfMonth.getNumberMonth() <= currentDate.getMonth()) {
            Calendar calendar = Calendar.getInstance();
            Date date = new GregorianCalendar(new Date().getYear(), salaryInformationOfMonth.getNumberMonth(), 1).getTime();
            calendar.setTime(date);
            String nameMonth = calendar.getDisplayName(calendar.MONTH,Calendar.LONG, Locale.ENGLISH);

            tvMonth.setText(nameMonth);

            if (salaryInformationOfMonth.getTimeShift1() != null)
                tvTime1.setText(salaryInformationOfMonth.getTimeShift1().intValue() / 60+"h. "+(salaryInformationOfMonth.getTimeShift1().intValue() % 60)+"m");
            else tvTime1.setText("0");
            if (salaryInformationOfMonth.getTimeShift2() != null)
                tvTime2.setText(salaryInformationOfMonth.getTimeShift2().intValue() / 60+"h. "+(salaryInformationOfMonth.getTimeShift2().intValue() % 60)+"m");
            else tvTime2.setText("0");
            if (salaryInformationOfMonth.getTimeShift3() != null)
                tvTime3.setText(salaryInformationOfMonth.getTimeShift3().intValue() / 60+"h. "+(salaryInformationOfMonth.getTimeShift3().intValue() % 60)+"m");
            else tvTime3.setText("0");


            if (salaryInformationOfMonth.getPaymentShift1() != null)
                tvPay1.setText(salaryInformationOfMonth.getPaymentShift1().toString());
            else tvPay1.setText("0.0");
            if (salaryInformationOfMonth.getPaymentShift2() != null)
                tvPay2.setText(salaryInformationOfMonth.getPaymentShift2().toString());
            else tvPay2.setText("0.0");
            if (salaryInformationOfMonth.getPaymentShift3() != null)
                tvPay3.setText(salaryInformationOfMonth.getPaymentShift3().toString());
            else tvPay3.setText("0.0");


            if (salaryInformationOfMonth.getAdditionalTime() != null)
                tvAddTime.setText(salaryInformationOfMonth.getAdditionalTime().intValue() / 60+"h. "+(salaryInformationOfMonth.getAdditionalTime().intValue() % 60)+"m");
            else tvAddTime.setText("0");
            if (salaryInformationOfMonth.getAdditionalPayment() != null)
                tvAddPayment.setText(salaryInformationOfMonth.getAdditionalPayment().toString());
            else tvAddPayment.setText("0");
            if (salaryInformationOfMonth.getPaymentMonth() != null)
                tvPaymentMonth.setText(salaryInformationOfMonth.getPaymentMonth().toString());
            else tvPaymentMonth.setText("0");
            if (salaryInformationOfMonth.getVacationPay() != null)
                tvVacationPay.setText(salaryInformationOfMonth.getVacationPay().toString());
            else tvVacationPay.setText("0");
        }else {
            Calendar calendar = Calendar.getInstance();
            Date date = new GregorianCalendar(new Date().getYear(), usedMonth, 1).getTime();
            calendar.setTime(date);
            String nameMonth = calendar.getDisplayName(calendar.MONTH,Calendar.LONG, Locale.ENGLISH);
            tvMonth.setText(nameMonth);

            tvTime1 .setText("0");
            tvTime2.setText("0");
            tvTime3.setText("0");
            tvPay1.setText("0");
            tvPay2.setText("0");
            tvPay3.setText("0");
            tvAddTime.setText("0");
            tvVacationPay.setText("0");
            tvAddPayment.setText("0");
            tvPaymentMonth .setText("0");

        }
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
