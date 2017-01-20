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

import org.json.JSONObject;

import pl.edu.pw.ee.overseer.R;
import pl.edu.pw.ee.overseer.tasks.ProfileTask;
import pl.edu.pw.ee.overseer.utilities.ExternalStorageUtility;
import pl.edu.pw.ee.overseer.utilities.SharedPreferencesUtility;
import pl.edu.pw.ee.overseer.utilities.ToastUtility;
import pl.edu.pw.ee.overseer.utilities.URLConnectionUtility;

public class ProfileFragment extends Fragment {
    private Activity mContext;
    private SharedPreferencesUtility mSharedPreferencesUtility;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile, container, false);

        mContext = getActivity();
        mContext.setTitle("Profile");
        mSharedPreferencesUtility = new SharedPreferencesUtility(mContext);

        try {
            if(URLConnectionUtility.isNetworkAvaliable(mContext))
                new ProfileTask(this).execute(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_TOKEN, ""));
            else if(ExternalStorageUtility.exists("profile.ovs"))
                asyncTaskResponse(ExternalStorageUtility.readJSONObject("profile.ovs"));
            else
                ToastUtility.makeError(mContext,"NETWORK_ERROR");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mView;
    }

    public void asyncTaskResponse(JSONObject response) {
        try {
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), ExternalStorageUtility.getFileInputStream("avatar/avatar_0.ovs"));
            drawable.setCornerRadius(150);
            ((ImageView) mView.findViewById(R.id.profile_avatar)).setImageDrawable(drawable);
            ((TextView) mView.findViewById(R.id.profile_name)).setText(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_NAME, "-"));
            ((TextView) mView.findViewById(R.id.profile_email)).setText(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_EMAIL, "-"));

            ((TextView) mView.findViewById(R.id.profile_phone)).setText(response.getString("phone"));
            ((TextView) mView.findViewById(R.id.profile_mobile)).setText(response.getString("mobile"));
            ((TextView) mView.findViewById(R.id.profile_address)).setText(response.getString("address") + "\n" + response.getString("zip") + " " + response.getString("city"));
            ((TextView) mView.findViewById(R.id.profile_company)).setText(response.getString("company"));
            ((TextView) mView.findViewById(R.id.profile_department)).setText(response.getString("department"));
            ((TextView) mView.findViewById(R.id.profile_team)).setText(response.getString("team"));
            ((TextView) mView.findViewById(R.id.profile_rank)).setText(response.getString("rank"));
            ((TextView) mView.findViewById(R.id.profile_joined)).setText(response.getString("joined"));
            ((TextView) mView.findViewById(R.id.profile_supervisor)).setText(response.getString("supervisor"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}