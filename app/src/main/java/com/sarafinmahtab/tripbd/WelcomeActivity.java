package com.sarafinmahtab.tripbd;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        Intent welcomeIntent = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(welcomeIntent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }
                }, SPLASH_TIME_OUT);
    }
}
