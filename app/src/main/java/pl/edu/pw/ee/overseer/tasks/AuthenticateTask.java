package pl.edu.pw.ee.overseer.tasks;

import android.os.AsyncTask;

import org.json.JSONObject;

import pl.edu.pw.ee.overseer.utilities.URLConnectionUtility;

public class AuthenticateTask extends AsyncTask<String, Void, JSONObject> {
    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            JSONObject request = new JSONObject();
            request.put("username", params[0]);
            request.put("password", params[1]);
            return URLConnectionUtility.doPost("/authenticate", request);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}