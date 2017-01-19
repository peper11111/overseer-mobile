package pl.edu.pw.ee.overseer.tasks;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.Date;

import pl.edu.pw.ee.overseer.utilities.URLConnectionUtility;

public class StartTask extends AsyncTask<String, Void, JSONObject> {
    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            JSONObject request = new JSONObject();
            request.put("token", params[0]);
            request.put("start", new Date().getTime());
            return URLConnectionUtility.doPost("/start", request);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
