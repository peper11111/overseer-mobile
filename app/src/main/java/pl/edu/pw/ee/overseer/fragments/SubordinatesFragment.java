package pl.edu.pw.ee.overseer.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.edu.pw.ee.overseer.R;
import pl.edu.pw.ee.overseer.adapters.SubordinatesListAdapter;
import pl.edu.pw.ee.overseer.tasks.SubordinatesTask;
import pl.edu.pw.ee.overseer.utilities.SharedPreferencesUtility;

public class SubordinatesFragment extends Fragment {
    private Activity mContext;
    private SharedPreferencesUtility mSharedPreferencesUtility;

    private SubordinatesListAdapter mAdapter;
    private TextView mInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_subordinates, container, false);

        mContext = getActivity();
        mContext.setTitle("Subordinates");
        mSharedPreferencesUtility = new SharedPreferencesUtility(mContext);

        mAdapter = new SubordinatesListAdapter(getContext(), R.layout.list_item_subordinates, new ArrayList<JSONObject>());
        ListView listView = (ListView) v.findViewById(R.id.subordinates_list);
        listView.setAdapter(mAdapter);

        mInfo = (TextView) v.findViewById(R.id.subordinates_info);

        if (mAdapter.getCount() == 0)
            mInfo.setVisibility(View.VISIBLE);
        else
            mInfo.setVisibility(View.GONE);

        //TODO jeżeli nie ma neta to wczytac z pliku
        new SubordinatesTask(this).execute(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_TOKEN, ""));

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
