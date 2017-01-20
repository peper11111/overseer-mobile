package pl.edu.pw.ee.overseer.fragments;

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
import pl.edu.pw.ee.overseer.adapters.LocationListAdapter;

public class DetailsLocationFragment extends Fragment {
    private LocationListAdapter mAdapter;
    private TextView mInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_view, container, false);

        DetailsFragment parent = (DetailsFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        ArrayList<JSONObject> locations = new ArrayList<>();

        try {
            if (parent.mSubordinate != null && parent.mSubordinate.has("locations")) {
                JSONArray array = parent.mSubordinate.getJSONArray("locations");
                for (int i = 0; i < array.length(); i++) {
                    locations.add(array.getJSONObject(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mAdapter = new LocationListAdapter(getContext(), R.layout.list_item_location, locations);
        ListView listView = (ListView) v.findViewById(R.id.list_view);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject jsonObject = mAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString("location", jsonObject.toString());

                Fragment fragment = new DetailsPositionFragment();
                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container, fragment).commit();

            }
        });

        mInfo = (TextView) v.findViewById(R.id.empty_list);

        if (mAdapter.getCount() == 0)
            mInfo.setVisibility(View.VISIBLE);
        else
            mInfo.setVisibility(View.GONE);

        return v;
    }
}