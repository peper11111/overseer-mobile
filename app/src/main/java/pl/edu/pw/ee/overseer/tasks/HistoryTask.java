package pl.edu.pw.ee.overseer.tasks;

import android.os.AsyncTask;

import org.json.JSONObject;

import pl.edu.pw.ee.overseer.utilities.URLConnectionUtility;

public class HistoryTask extends AsyncTask<String, Void, JSONObject> {
    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            JSONObject request = new JSONObject();
            request.put("token", params[0]);
            return URLConnectionUtility.doPost("/history", request);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}