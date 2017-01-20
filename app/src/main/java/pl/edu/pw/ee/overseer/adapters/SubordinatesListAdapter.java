package pl.edu.pw.ee.overseer.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import pl.edu.pw.ee.overseer.R;

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
            byte[] image = Base64.decode(item.getString("avatar"), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), bitmap);
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
