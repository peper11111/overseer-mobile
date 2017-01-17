package pl.edu.pw.ee.overseer.tasks;

import android.os.AsyncTask;

import org.json.JSONObject;

import pl.edu.pw.ee.overseer.utilities.URLConnectionUtility;

public class AuthenticateTask extends AsyncTask<JSONObject, Void, JSONObject> {
    @Override
    protected JSONObject doInBackground(JSONObject... params) {
        try {
            return URLConnectionUtility.doPost("/authenticate", params[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}