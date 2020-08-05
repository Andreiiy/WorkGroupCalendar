package com.appoftatar.workgroupcalendar.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.appoftatar.workgroupcalendar.AdditionalScheduleFragment;

public class TabsPagerAdditionalScheduleAdapter extends FragmentPagerAdapter {

    private int countTabs;
    public TabsPagerAdditionalScheduleAdapter(@NonNull FragmentManager fm, int countTabs) {
        super(fm);
        this.countTabs = countTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return AdditionalScheduleFragment.getInstance(position);
    }

    @Override
    public int getCount() {
        return countTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "day"+Integer.toString(position+1);
    }
}
