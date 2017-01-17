package pl.edu.pw.ee.overseer.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pl.edu.pw.ee.overseer.R;

public class LoginActivity extends AppCompatActivity {
    private Context mContext;
    private EditText mLoginUsername;
    private EditText mLoginPassword;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;

        mLoginUsername = (EditText) findViewById(R.id.login_username);
        mLoginPassword = (EditText) findViewById(R.id.login_password);
        mLoginButton = (Button) findViewById(R.id.login_button);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                mContext.startActivity(intent);
            }
        });
    }
}
