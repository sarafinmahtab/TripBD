package com.sarafinmahtab.tripbd;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.sarafinmahtab.tripbd.main.MainActivity;

public class WelcomeActivity extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        imageView = (ImageView)findViewById(R.id.tripbd_logo);

//        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.welcome_animation);
//        imageView.setAnimation(animation);

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
