package pl.edu.pw.ee.overseer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import pl.edu.pw.ee.overseer.R;
import pl.edu.pw.ee.overseer.utilities.ExternalStorageUtility;

public class SubordinatesListAdapter extends ArrayAdapter<JSONObject> {
    int mResource;

    public SubordinatesListAdapter(Context context, int resource, ArrayList<JSONObject> items) {
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
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), ExternalStorageUtility.getFileInputStream("avatar/avatar_" + item.getString("id") + ".ovs"));
            drawable.setCornerRadius(150);

            ImageView avatar = (ImageView) convertView.findViewById(R.id.subordinate_avatar);
            avatar.setImageDrawable(drawable);

            TextView name = (TextView) convertView.findViewById(R.id.subordinate_name);
            name.setText(item.getString("name"));

            TextView email = (TextView) convertView.findViewById(R.id.subordinate_email);
            email.setText(item.getString("email"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}