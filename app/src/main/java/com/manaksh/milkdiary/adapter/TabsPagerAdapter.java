package com.manaksh.milkdiary.adapter;

/**
 * Created by akshmaha on 12/14/2015.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.manaksh.milkdiary.fragments.DiaryFragment;
import com.manaksh.milkdiary.fragments.ReportsFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Diary fragment activity
                return new DiaryFragment();
            case 1:
                // Reports fragment activity
                return new ReportsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }

}