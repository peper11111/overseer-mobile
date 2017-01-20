package pl.edu.pw.ee.overseer.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
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
    protected void onPostExecute(JSONObject response) {
        try {
            JSONArray jsonArray = new JSONArray();
            JSONArray responseArray = response.getJSONArray("subordinates");
            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject subordinate = responseArray.getJSONObject(i);
                ExternalStorageUtility.writeImage("avatar/avatar_" + subordinate.getString("id") + ".ovs", subordinate.getString("avatar"));
                subordinate.remove("avatar");
                jsonArray.put(subordinate);
            }
            response.put("subordinates", jsonArray);
            ExternalStorageUtility.saveJSONObject("subordinates.ovs", response);
            mContext.asyncTaskResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}