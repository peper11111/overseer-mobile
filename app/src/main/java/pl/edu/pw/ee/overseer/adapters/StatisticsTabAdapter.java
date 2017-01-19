package pl.edu.pw.ee.overseer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import pl.edu.pw.ee.overseer.fragments.MonthFragment;
import pl.edu.pw.ee.overseer.fragments.WeekFragment;
import pl.edu.pw.ee.overseer.fragments.YearFragment;

public class StatisticsTabAdapter extends FragmentPagerAdapter {
    public StatisticsTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new WeekFragment();
            case 1:
                return new MonthFragment();
            case 2:
                return new YearFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Week";
            case 1:
                return "Month";
            case 2:
                return "Year";
            default:
                return null;
        }
    }
}