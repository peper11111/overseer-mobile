package pl.edu.pw.ee.overseer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import pl.edu.pw.ee.overseer.R;

public class LocationListAdapter extends ArrayAdapter<JSONObject> {
    int mResource;

    public LocationListAdapter(Context context, int resource, ArrayList<JSONObject> items) {
        super(context, 0, items);
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject item = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(mResource, parent, false);

        try {
            TextView date = (TextView) convertView.findViewById(R.id.location_date);
            date.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(item.getLong("date")));

            TextView latitude = (TextView) convertView.findViewById(R.id.location_latitude);
            latitude.setText(item.getString("latitude"));

            TextView longitude = (TextView) convertView.findViewById(R.id.location_longitude);
            longitude.setText(item.getString("longitude"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}