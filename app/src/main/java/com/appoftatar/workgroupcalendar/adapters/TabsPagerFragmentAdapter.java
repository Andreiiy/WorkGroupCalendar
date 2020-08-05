package com.appoftatar.workgroupcalendar.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.appoftatar.workgroupcalendar.ui.schedule.WorkScheduleFragment;
import com.appoftatar.workgroupcalendar.R;
import com.appoftatar.workgroupcalendar.ui.board.BoardFragment;
import com.appoftatar.workgroupcalendar.ui.calendar.CalendarFragment;
import com.appoftatar.workgroupcalendar.ui.employes.EmployesFragment;
import com.appoftatar.workgroupcalendar.ui.requests.RequestsFragment;

public class TabsPagerFragmentAdapter extends FragmentPagerAdapter {

    private String [] tabs;
    public TabsPagerFragmentAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);


        tabs = context.getResources().getStringArray ( R.array.tabs_manager );
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
        case 0:return EmployesFragment.getInstance();
        case 1:return RequestsFragment.getInstance();
        case 2:return CalendarFragment.getInstance();
            case 3:return BoardFragment.getInstance();
            case 4:return WorkScheduleFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabs.length;
    }


}
