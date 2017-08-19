package com.sarafinmahtab.tripbd;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.sarafinmahtab.tripbd.main.MainActivity;

public class DetailsActivity extends AppCompatActivity {

    FloatingActionButton fab_plus, fab_home, fab_guide, fab_hotel, fab_restaurant, fab_visited, fab_review;
    TextView home, guide, hotel, restaurant, visited, review;
    Animation FabOpen, FabClose, FabClockwise, FabAntiClockwise;

    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fab_layout_details);

        Bundle bundle = getIntent().getExtras();

        String url = bundle.getString("details_link");

        WebView webView = (WebView) findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setLoadsImagesAutomatically(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportMultipleWindows(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new MyBrouser());
        ///  onConfigurationChanged();

        fab_plus= (FloatingActionButton)findViewById(R.id.fab_plus);
        fab_home= (FloatingActionButton)findViewById(R.id.fab_home);
        fab_guide= (FloatingActionButton)findViewById(R.id.fab_guide);
        fab_hotel= (FloatingActionButton)findViewById(R.id.fab_hotel);
        fab_restaurant= (FloatingActionButton)findViewById(R.id.fab_restaurant);
        fab_visited= (FloatingActionButton)findViewById(R.id.fab_visited);
        fab_review= (FloatingActionButton)findViewById(R.id.fab_review);

        home= (TextView)findViewById(R.id.home);
        guide= (TextView)findViewById(R.id.guide);
        hotel= (TextView)findViewById(R.id.hotel);
        restaurant= (TextView)findViewById(R.id.restaurant);
        visited= (TextView)findViewById(R.id.visited);
        review= (TextView)findViewById(R.id.review);

        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        FabAntiClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

        fab_plus.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(isOpen){
                    fab_home.startAnimation(FabClose);
                    fab_guide.startAnimation(FabClose);
                    fab_hotel.startAnimation(FabClose);
                    fab_restaurant.startAnimation(FabClose);
                    fab_visited.startAnimation(FabClose);
                    fab_review.startAnimation(FabClose);
                    fab_plus.startAnimation(FabAntiClockwise);
                    fab_home.setClickable(false);
                    fab_guide.setClickable(false);
                    fab_hotel.setClickable(false);
                    fab_restaurant.setClickable(false);
                    fab_visited.setClickable(false);
                    fab_review.setClickable(false);
                    home.setText("");
                    home.setBackgroundColor(Color.TRANSPARENT);
                    guide.setText("");
                    guide.setBackgroundColor(Color.TRANSPARENT);
                    hotel.setText("");
                    hotel.setBackgroundColor(Color.TRANSPARENT);
                    restaurant.setText("");
                    restaurant.setBackgroundColor(Color.TRANSPARENT);
                    visited.setText("");
                    visited.setBackgroundColor(Color.TRANSPARENT);
                    review.setText("");
                    review.setBackgroundColor(Color.TRANSPARENT);
                    isOpen = false;
                }else{
                    fab_home.startAnimation(FabOpen);
                    fab_guide.startAnimation(FabOpen);
                    fab_hotel.startAnimation(FabOpen);
                    fab_restaurant.startAnimation(FabOpen);
                    fab_visited.startAnimation(FabOpen);
                    fab_review.startAnimation(FabOpen);
                    fab_plus.startAnimation(FabClockwise);
                    fab_home.setClickable(true);
                    fab_guide.setClickable(true);
                    fab_hotel.setClickable(true);
                    fab_restaurant.setClickable(true);
                    fab_visited.setClickable(true);
                    fab_review.setClickable(true);
                    home.setText("Home");
                    home.setBackgroundColor(Color.rgb(79, 74, 84));
                    guide.setText("Hire a Guide");
                    guide.setBackgroundColor(Color.rgb(79, 74, 84));
                    hotel.setText("Hotels");
                    hotel.setBackgroundColor(Color.rgb(79, 74, 84));
                    restaurant.setText("Restaurants");
                    restaurant.setBackgroundColor(Color.rgb(79, 74, 84));
                    visited.setText("Visited");
                    visited.setBackgroundColor(Color.rgb(79, 74, 84));
                    review.setText("Review");
                    review.setBackgroundColor(Color.rgb(79, 74, 84));
                    isOpen = true;
                }
            }
        });
        fab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        fab_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, GuideListActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        fab_hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailsActivity.this, "Hotel", Toast.LENGTH_SHORT).show();
            }
        });
        fab_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailsActivity.this, "Restaurant", Toast.LENGTH_SHORT).show();
            }
        });
        fab_visited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailsActivity.this, "Visited", Toast.LENGTH_SHORT).show();
            }
        });
        fab_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailsActivity.this, "Review", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class MyBrouser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
