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
import pl.edu.pw.ee.overseer.tasks.ProfileTask;
import pl.edu.pw.ee.overseer.tasks.StatisticsTask;
import pl.edu.pw.ee.overseer.utilities.ExternalStorageUtility;
import pl.edu.pw.ee.overseer.utilities.SharedPreferencesUtility;
import pl.edu.pw.ee.overseer.utilities.ToastUtility;
import pl.edu.pw.ee.overseer.utilities.URLConnectionUtility;

public class StatisticsFragment extends Fragment {
    private Activity mContext;
    private SharedPreferencesUtility mSharedPreferencesUtility;
    private ViewPager mViewPager;

    public JSONObject mStatistics = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_viewpager, container, false);

        mContext = getActivity();
        mContext.setTitle("Statistics");
        mSharedPreferencesUtility = new SharedPreferencesUtility(mContext);

        StatisticsTabAdapter statisticsTabAdapter = new StatisticsTabAdapter(getChildFragmentManager());

        mViewPager = (ViewPager) v.findViewById(R.id.view_pager);
        mViewPager.setAdapter(statisticsTabAdapter);

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);

        try {
            if(URLConnectionUtility.isNetworkAvaliable(mContext))
                new StatisticsTask(this).execute(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_TOKEN, ""));
            else if(ExternalStorageUtility.exists("statistics.ovs"))
                asyncTaskResponse(ExternalStorageUtility.readJSONObject("statistics.ovs"));
            else
                ToastUtility.makeError(mContext,"NETWORK_ERROR");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }

    public void asyncTaskResponse(JSONObject response) {
        mStatistics = response;
        mViewPager.getAdapter().notifyDataSetChanged();
    }
}