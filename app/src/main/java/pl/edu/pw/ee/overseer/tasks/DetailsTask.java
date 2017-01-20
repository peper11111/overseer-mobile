package pl.edu.pw.ee.overseer.tasks;

import android.os.AsyncTask;

import org.json.JSONObject;

import pl.edu.pw.ee.overseer.fragments.DetailsFragment;
import pl.edu.pw.ee.overseer.utilities.ExternalStorageUtility;
import pl.edu.pw.ee.overseer.utilities.URLConnectionUtility;

public class DetailsTask extends AsyncTask<String, Void, JSONObject> {
    private DetailsFragment mContext;
    private Long mId;

    public DetailsTask(DetailsFragment context) {
        mContext = context;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            JSONObject request = new JSONObject();
            request.put("token", params[0]);
            mId = Long.parseLong(params[1]);
            request.put("id", mId);
            return URLConnectionUtility.doPost("/details", request);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        try {
            ExternalStorageUtility.saveJSONObject("detail/detail_" + mId + ".osv", jsonObject);
            mContext.asyncTaskResponse(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}