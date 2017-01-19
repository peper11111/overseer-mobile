package pl.edu.pw.ee.overseer.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import pl.edu.pw.ee.overseer.R;
import pl.edu.pw.ee.overseer.adapters.StatisticsTabAdapter;
import pl.edu.pw.ee.overseer.tasks.HistoryTask;
import pl.edu.pw.ee.overseer.tasks.StatisticsTask;
import pl.edu.pw.ee.overseer.utilities.ExternalStorageUtility;
import pl.edu.pw.ee.overseer.utilities.SharedPreferencesUtility;

public class StatisticsFragment extends Fragment {
    private Activity mContext;
    private SharedPreferencesUtility mSharedPreferencesUtility;

    public JSONObject mStatistics;
    public JSONObject mHistory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_statistics, container, false);

        mContext = getActivity();
        mContext.setTitle("Statistics");
        mSharedPreferencesUtility = new SharedPreferencesUtility(mContext);

        try {
            mStatistics = new StatisticsTask().execute(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_TOKEN, "")).get();
            mHistory = new HistoryTask().execute(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_TOKEN, "")).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        StatisticsTabAdapter statisticsTabAdapter = new StatisticsTabAdapter(getChildFragmentManager());

        ViewPager viewPager = (ViewPager) v.findViewById(R.id.view_pager);
        viewPager.setAdapter(statisticsTabAdapter);

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        return v;
    }
}