package pl.edu.pw.ee.overseer.fragments;


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

import org.json.JSONException;
import org.json.JSONObject;

import pl.edu.pw.ee.overseer.R;
import pl.edu.pw.ee.overseer.utilities.ExternalStorageUtility;

public class DetailsProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        DetailsFragment parent = (DetailsFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        try {
            if (parent.mSubordinate != null) {
                JSONObject profile = parent.mSubordinate.getJSONObject("profile");
                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), ExternalStorageUtility.getFileInputStream("avatar/avatar_" + profile.getString("id") + ".ovs"));
                drawable.setCornerRadius(150);

                ((ImageView) v.findViewById(R.id.profile_avatar)).setImageDrawable(drawable);
                ((TextView) v.findViewById(R.id.profile_name)).setText(profile.getString("name"));
                ((TextView) v.findViewById(R.id.profile_email)).setText(profile.getString("email"));
                if (profile.has("supervisor")) {
                    ((TextView) v.findViewById(R.id.profile_phone)).setText(profile.getString("phone"));
                    ((TextView) v.findViewById(R.id.profile_mobile)).setText(profile.getString("mobile"));
                    ((TextView) v.findViewById(R.id.profile_address)).setText(profile.getString("address") + "\n" + profile.getString("zip") + " " + profile.getString("city"));
                    ((TextView) v.findViewById(R.id.profile_company)).setText(profile.getString("company"));
                    ((TextView) v.findViewById(R.id.profile_department)).setText(profile.getString("department"));
                    ((TextView) v.findViewById(R.id.profile_team)).setText(profile.getString("team"));
                    ((TextView) v.findViewById(R.id.profile_rank)).setText(profile.getString("rank"));
                    ((TextView) v.findViewById(R.id.profile_joined)).setText(profile.getString("joined"));
                    ((TextView) v.findViewById(R.id.profile_supervisor)).setText(profile.getString("supervisor"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }
}
