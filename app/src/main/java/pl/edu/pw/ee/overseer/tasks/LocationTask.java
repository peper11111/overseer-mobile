package pl.edu.pw.ee.overseer.tasks;


import android.os.AsyncTask;

import org.json.JSONObject;

import pl.edu.pw.ee.overseer.utilities.URLConnectionUtility;

public class LocationTask extends AsyncTask<String, Void, JSONObject> {
    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            JSONObject request = new JSONObject();
            request.put("token", params[0]);
            request.put("latitude", Double.parseDouble(params[1]));
            request.put("longitude", Double.parseDouble(params[2]));
            request.put("date", Long.parseLong(params[3]));
            return URLConnectionUtility.doPost("/location", request);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
