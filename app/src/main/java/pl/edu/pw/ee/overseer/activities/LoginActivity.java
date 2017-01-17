package pl.edu.pw.ee.overseer.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import pl.edu.pw.ee.overseer.R;
import pl.edu.pw.ee.overseer.tasks.AuthenticateTask;
import pl.edu.pw.ee.overseer.utilities.ExternalStorageUtility;
import pl.edu.pw.ee.overseer.utilities.SharedPreferencesUtility;
import pl.edu.pw.ee.overseer.utilities.ToastUtility;

public class LoginActivity extends AppCompatActivity {
    private Activity mContext;
    private EditText mLoginUsername;
    private EditText mLoginPassword;
    private Button mLoginButton;

    private SharedPreferencesUtility mSharedPreferencesUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;
        mSharedPreferencesUtility = new SharedPreferencesUtility(mContext);

        mLoginUsername = (EditText) findViewById(R.id.login_username);
        mLoginPassword = (EditText) findViewById(R.id.login_password);
        mLoginButton = (Button) findViewById(R.id.login_button);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject request = new JSONObject();
                    request.put("username", mLoginUsername.getText().toString());
                    request.put("password", mLoginPassword.getText().toString());

                    JSONObject response = new AuthenticateTask().execute(request).get();

                    if (response.getBoolean("authenticated")) {
                        mSharedPreferencesUtility
                                .putBoolean(SharedPreferencesUtility.KEY_AUTHENTICATED, true)
                                .putString(SharedPreferencesUtility.KEY_USERNAME, request.getString("username"))
                                .putString(SharedPreferencesUtility.KEY_TOKEN, response.getString("token"))
                                .putString(SharedPreferencesUtility.KEY_NAME, response.getString("name"))
                                .putString(SharedPreferencesUtility.KEY_EMAIL, response.getString("email"))
                                .putString(SharedPreferencesUtility.KEY_MOBILE, response.getString("mobile"))
                                .putString(SharedPreferencesUtility.KEY_RANK, response.getString("rank"))
                                .putString(SharedPreferencesUtility.KEY_TEAM, response.getString("team"))
                                .putString(SharedPreferencesUtility.KEY_DEPARTMENT, response.getString("department"))
                                .putString(SharedPreferencesUtility.KEY_COMPANY, response.getString("company"))
                                .putString(SharedPreferencesUtility.KEY_SUPERVISOR, response.getString("supervisor"))
                                .apply();

                        ExternalStorageUtility.createRootFolder();
                        ExternalStorageUtility.writeImage("avatar.png", response.getString("avatar"));

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
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mSharedPreferencesUtility.getBoolean(SharedPreferencesUtility.KEY_AUTHENTICATED, false)) {
            Intent intent = new Intent(mContext, MainActivity.class);
            mContext.startActivity(intent);
            mContext.finish();
        } else {
            mLoginUsername.setText(mSharedPreferencesUtility.getString(SharedPreferencesUtility.KEY_USERNAME, ""));
        }
    }
}
