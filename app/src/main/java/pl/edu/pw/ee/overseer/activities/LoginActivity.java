package pl.edu.pw.ee.overseer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import pl.edu.pw.ee.overseer.R;
import pl.edu.pw.ee.overseer.tasks.LoginTask;
import pl.edu.pw.ee.overseer.utilities.ExternalStorageUtility;
import pl.edu.pw.ee.overseer.utilities.SharedPreferencesUtility;
import pl.edu.pw.ee.overseer.utilities.ToastUtility;
import pl.edu.pw.ee.overseer.utilities.URLConnectionUtility;

public class LoginActivity extends AppCompatActivity {
    private LoginActivity mContext;
    private SharedPreferencesUtility mSharedPreferencesUtility;

    private EditText mLoginUsername;
    private EditText mLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;
        mSharedPreferencesUtility = new SharedPreferencesUtility(mContext);

        mLoginUsername = (EditText) findViewById(R.id.login_username);
        mLoginPassword = (EditText) findViewById(R.id.login_password);
        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(URLConnectionUtility.isNetworkAvaliable(mContext))
                    new LoginTask(mContext).execute(mLoginUsername.getText().toString(), mLoginPassword.getText().toString());
                else
                    ToastUtility.makeError(mContext,"NETWORK_ERROR");
            }
        });
    }

    public void asyncTaskResponse(JSONObject response) {
        try {
            if (!response.has("error")) {
                mSharedPreferencesUtility
                        .putString(SharedPreferencesUtility.KEY_TOKEN, response.getString("token"))
                        .putString(SharedPreferencesUtility.KEY_USERNAME, mLoginUsername.getText().toString())
                        .putString(SharedPreferencesUtility.KEY_NAME, response.getString("name"))
                        .putString(SharedPreferencesUtility.KEY_EMAIL, response.getString("email"))
                        .apply();

                ExternalStorageUtility.createRootFolder();
                ExternalStorageUtility.writeImage("avatar/avatar_0.ovs", response.getString("avatar"));

                Intent intent = new Intent(mContext, MainActivity.class);
                mContext.startActivity(intent);
                mContext.finish();
            } else {
                ToastUtility.makeError(mContext, response.getString("error"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mSharedPreferencesUtility.contains(SharedPreferencesUtility.KEY_TOKEN)) {
            Intent intent = new Intent(mContext, MainActivity.class);
            mContext.startActivity(intent);
            mContext.finish();
        } else {
            mLoginUsername.setText(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_USERNAME, ""));
        }
    }
}