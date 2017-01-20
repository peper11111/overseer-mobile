package pl.edu.pw.ee.overseer.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.edu.pw.ee.overseer.R;
import pl.edu.pw.ee.overseer.adapters.SubordinatesListAdapter;
import pl.edu.pw.ee.overseer.tasks.SubordinatesTask;
import pl.edu.pw.ee.overseer.utilities.ExternalStorageUtility;
import pl.edu.pw.ee.overseer.utilities.SharedPreferencesUtility;
import pl.edu.pw.ee.overseer.utilities.ToastUtility;
import pl.edu.pw.ee.overseer.utilities.URLConnectionUtility;

public class SubordinatesFragment extends Fragment {
    private Activity mContext;
    private SharedPreferencesUtility mSharedPreferencesUtility;

    private SubordinatesListAdapter mAdapter;
    private TextView mInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_view, container, false);

        mContext = getActivity();
        mContext.setTitle("Subordinates");
        mSharedPreferencesUtility = new SharedPreferencesUtility(mContext);

        mAdapter = new SubordinatesListAdapter(getContext(), R.layout.list_item_subordinates, new ArrayList<JSONObject>());
        ListView listView = (ListView) v.findViewById(R.id.list_view);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject jsonObject = mAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString("subordinate", jsonObject.toString());

                Fragment fragment = new DetailsFragment();
                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container, fragment).commit();
            }
        });

        mInfo = (TextView) v.findViewById(R.id.empty_list);

        if (mAdapter.getCount() == 0)
            mInfo.setVisibility(View.VISIBLE);
        else
            mInfo.setVisibility(View.GONE);

        try {
            if (URLConnectionUtility.isNetworkAvaliable(mContext))
                new SubordinatesTask(this).execute(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_TOKEN, ""));
            else if (ExternalStorageUtility.exists("subordinates.ovs"))
                asyncTaskResponse(ExternalStorageUtility.readJSONObject("subordinates.ovs"));
            else
                ToastUtility.makeError(mContext, "NETWORK_ERROR");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }

    public void asyncTaskResponse(JSONObject response) {
        try {
            JSONArray array = response.getJSONArray("subordinates");
            for (int i = 0; i < array.length(); i++) {
                mAdapter.add(array.getJSONObject(i));
            }
            mAdapter.notifyDataSetChanged();
            if (mAdapter.getCount() == 0)
                mInfo.setVisibility(View.VISIBLE);
            else
                mInfo.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
