package pl.edu.pw.ee.overseer.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import pl.edu.pw.ee.overseer.R;
import pl.edu.pw.ee.overseer.utilities.ExternalStorageUtility;
import pl.edu.pw.ee.overseer.utilities.SharedPreferencesUtility;

public class ProfileFragment extends Fragment {
    private Activity mContext;
    private SharedPreferencesUtility mSharedPreferencesUtility;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        mContext = getActivity();
        mContext.setTitle("Profile");
        mSharedPreferencesUtility = new SharedPreferencesUtility(mContext);

        try {
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), ExternalStorageUtility.getFileInputStream("/avatar.png"));
            drawable.setCornerRadius(150);
            ((ImageView) v.findViewById(R.id.profile_avatar)).setImageDrawable(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ((TextView) v.findViewById(R.id.profile_name)).setText(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_NAME, "-"));
        ((TextView) v.findViewById(R.id.profile_email)).setText(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_EMAIL, "-"));
        ((TextView) v.findViewById(R.id.profile_mobile)).setText(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_MOBILE, "-"));
        ((TextView) v.findViewById(R.id.profile_rank)).setText(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_RANK, "-"));
        ((TextView) v.findViewById(R.id.profile_team)).setText(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_TEAM, "-"));
        ((TextView) v.findViewById(R.id.profile_department)).setText(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_DEPARTMENT, "-"));
        ((TextView) v.findViewById(R.id.profile_company)).setText(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_COMPANY, "-"));
        ((TextView) v.findViewById(R.id.profile_supervisor)).setText(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_SUPERVISOR, "-"));

        return v;
    }
}
