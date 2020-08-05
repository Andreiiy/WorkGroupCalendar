package com.appoftatar.workgroupcalendar.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.appoftatar.workgroupcalendar.ui.employee_inform.EmployeeFragment;
import com.appoftatar.workgroupcalendar.ui.schedule.WorkScheduleFragment;
import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.ui.board.BoardFragment;
import com.appoftatar.workgroupcalendar.ui.calendar.CalendarFragment;
import com.appoftatar.workgroupcalendar.ui.workcalendar.WorkCalendarFragment;

public class TabsPagerEmployeeFragmentAdapter extends FragmentPagerAdapter {

    private String [] tabs;
    public TabsPagerEmployeeFragmentAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);


        tabs = context.getResources().getStringArray ( R.array.tabs_employee );

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:return EmployeeFragment.getInstance();
            case 1:return CalendarFragment.getInstance();
            case 3:return BoardFragment.getInstance();
            case 2:return WorkScheduleFragment.getInstance();
            case 4:return WorkCalendarFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabs.length;
    }

}

