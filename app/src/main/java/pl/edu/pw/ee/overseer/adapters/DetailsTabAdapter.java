package pl.edu.pw.ee.overseer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import pl.edu.pw.ee.overseer.fragments.DetailsProfileFragment;

public class DetailsTabAdapter extends FragmentPagerAdapter {
    public DetailsTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DetailsProfileFragment();
            case 1:
                return new DetailsProfileFragment();
            case 2:
                return new DetailsProfileFragment();
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
                return "Profile";
            case 1:
                return "Work Time";
            case 2:
                return "Locations";
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}