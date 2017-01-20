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
import pl.edu.pw.ee.overseer.adapters.DetailsTabAdapter;
import pl.edu.pw.ee.overseer.tasks.DetailsTask;
import pl.edu.pw.ee.overseer.tasks.ProfileTask;
import pl.edu.pw.ee.overseer.utilities.ExternalStorageUtility;
import pl.edu.pw.ee.overseer.utilities.SharedPreferencesUtility;
import pl.edu.pw.ee.overseer.utilities.ToastUtility;
import pl.edu.pw.ee.overseer.utilities.URLConnectionUtility;

public class DetailsFragment extends Fragment {
    private Activity mContext;
    private SharedPreferencesUtility mSharedPreferencesUtility;
    private ViewPager mViewPager;

    public JSONObject mSubordinate = new JSONObject();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_viewpager, container, false);

        mContext = getActivity();
        mSharedPreferencesUtility = new SharedPreferencesUtility(mContext);

        try {
            JSONObject jsonObject = new JSONObject(getArguments().getString("subordinate"));
            mSubordinate.put("profile", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DetailsTabAdapter detailsTabAdapter = new DetailsTabAdapter(getChildFragmentManager());

        mViewPager = (ViewPager) v.findViewById(R.id.view_pager);
        mViewPager.setAdapter(detailsTabAdapter);

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);

        try {
            if (ExternalStorageUtility.exists("detail/detail_" + mSubordinate.getJSONObject("profile").get("id") + ".ovs"))
                asyncTaskResponse(ExternalStorageUtility.readJSONObject("detail/detail_" + mSubordinate.getJSONObject("profile").get("id") + ".ovs"));

            if (URLConnectionUtility.isNetworkAvaliable(mContext))
                new DetailsTask(this).execute(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_TOKEN, ""),mSubordinate.getJSONObject("profile").getString("id"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }

    public void asyncTaskResponse(JSONObject response) {
        try {
            response.getJSONObject("profile").put("id", mSubordinate.getJSONObject("profile").getString("id"));
            response.getJSONObject("profile").put("name", mSubordinate.getJSONObject("profile").getString("name"));
            response.getJSONObject("profile").put("email", mSubordinate.getJSONObject("profile").getString("email"));
            mSubordinate = response;
            mViewPager.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}