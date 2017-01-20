package pl.edu.pw.ee.overseer.tasks;

import android.os.AsyncTask;

import org.json.JSONObject;

import pl.edu.pw.ee.overseer.fragments.SubordinatesFragment;
import pl.edu.pw.ee.overseer.utilities.ExternalStorageUtility;
import pl.edu.pw.ee.overseer.utilities.URLConnectionUtility;

public class SubordinatesTask extends AsyncTask<String, Void, JSONObject> {
    private SubordinatesFragment mContext;

    public SubordinatesTask(SubordinatesFragment context) {
        mContext = context;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            JSONObject request = new JSONObject();
            request.put("token", params[0]);
            return URLConnectionUtility.doPost("/subordinates", request);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        try {
            ExternalStorageUtility.saveJSONObject("subordinates.ovs", jsonObject);
            mContext.asyncTaskResponse(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}