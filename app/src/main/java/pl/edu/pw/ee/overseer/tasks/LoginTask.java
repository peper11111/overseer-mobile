package pl.edu.pw.ee.overseer.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONObject;

import pl.edu.pw.ee.overseer.activities.LoginActivity;
import pl.edu.pw.ee.overseer.utilities.URLConnectionUtility;

public class LoginTask extends AsyncTask<String, Void, JSONObject> {
    private LoginActivity mContext;
    private ProgressDialog mProgressDialog;

    public LoginTask(LoginActivity context) {
        mContext = context;
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Please wait");
        mProgressDialog.show();
    }

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

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();

        mContext.asyncTaskResponse(jsonObject);
    }
}