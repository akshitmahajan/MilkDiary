package com.manaksh.milkdiary.adapter;

/**
 * Created by akshmaha on 12/14/2015.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.manaksh.milkdiary.fragments.DiaryFragment;
import com.manaksh.milkdiary.fragments.ReportsFragment;

import java.util.ArrayList;
import java.util.List;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<Fragment>();
        fragments.add(new DiaryFragment());
        fragments.add(new ReportsFragment());
    }

    @Override
    public Fragment getItem(int index) {

        /*switch (index) {
            case 0:
                // Diary fragment activity
                return new DiaryFragment();
            case 1:
                // Reports fragment activity
                return new ReportsFragment();
        }*/

        return fragments.get(index);
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return fragments.size();
    }
}
