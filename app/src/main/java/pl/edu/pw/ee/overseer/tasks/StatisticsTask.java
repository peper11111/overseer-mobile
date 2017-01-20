package pl.edu.pw.ee.overseer.tasks;

import android.os.AsyncTask;

import org.json.JSONObject;

import pl.edu.pw.ee.overseer.fragments.ProfileFragment;
import pl.edu.pw.ee.overseer.fragments.StatisticsFragment;
import pl.edu.pw.ee.overseer.utilities.ExternalStorageUtility;
import pl.edu.pw.ee.overseer.utilities.URLConnectionUtility;

public class StatisticsTask extends AsyncTask<String, Void, JSONObject> {
    private StatisticsFragment mContext;

    public StatisticsTask(StatisticsFragment context) {
        mContext = context;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            JSONObject request = new JSONObject();
            request.put("token", params[0]);
            return URLConnectionUtility.doPost("/statistics", request);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        try {
            ExternalStorageUtility.saveJSONObject("statistics.ovs", jsonObject);
            mContext.asyncTaskResponse(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}