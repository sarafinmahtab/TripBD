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

import com.sarafinmahtab.tripbd.guideList.GuideListActivity;
import com.sarafinmahtab.tripbd.main.MainActivity;

public class DetailsActivity extends AppCompatActivity {

    FloatingActionButton fabPlus, fabHome, fabGuide, fabHotel, fabRestaurant, fabVisited, fabReview;
    TextView home, guide, hotel, restaurant, visited, review;
    Animation FabOpen, FabClose, FabClockwise, FabAntiClockwise;

    boolean isOpen = false;

    String pinPointID, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fab_layout_details);

        Bundle bundle = getIntent().getExtras();
        pinPointID = bundle.getString("pin_point_id");
        url = bundle.getString("details_link");
        url = ServerAddress.getMyServerAddress().concat("Asset/" + url);

        final WebView webView = (WebView) findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setLoadsImagesAutomatically(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportMultipleWindows(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new MyBrowser());
        ///  onConfigurationChanged();

        fabPlus = (FloatingActionButton)findViewById(R.id.fab_plus);
        fabHome = (FloatingActionButton)findViewById(R.id.fab_home);
        fabGuide = (FloatingActionButton)findViewById(R.id.fab_guide);
        fabHotel = (FloatingActionButton)findViewById(R.id.fab_hotel);
        fabRestaurant = (FloatingActionButton)findViewById(R.id.fab_restaurant);
        fabVisited = (FloatingActionButton)findViewById(R.id.fab_visited);
        fabReview = (FloatingActionButton)findViewById(R.id.fab_review);

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

        fabPlus.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(isOpen){
                    webView.setAlpha(1.0f);
                    fabHome.startAnimation(FabClose);
                    fabGuide.startAnimation(FabClose);
                    fabHotel.startAnimation(FabClose);
                    fabRestaurant.startAnimation(FabClose);
                    fabVisited.startAnimation(FabClose);
                    fabReview.startAnimation(FabClose);
                    fabPlus.startAnimation(FabAntiClockwise);
                    fabHome.setClickable(false);
                    fabGuide.setClickable(false);
                    fabHotel.setClickable(false);
                    fabRestaurant.setClickable(false);
                    fabVisited.setClickable(false);
                    fabReview.setClickable(false);
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
                    webView.setAlpha(0.5f);
                    fabHome.startAnimation(FabOpen);
                    fabGuide.startAnimation(FabOpen);
                    fabHotel.startAnimation(FabOpen);
                    fabRestaurant.startAnimation(FabOpen);
                    fabVisited.startAnimation(FabOpen);
                    fabReview.startAnimation(FabOpen);
                    fabPlus.startAnimation(FabClockwise);
                    fabHome.setClickable(true);
                    fabGuide.setClickable(true);
                    fabHotel.setClickable(true);
                    fabRestaurant.setClickable(true);
                    fabVisited.setClickable(true);
                    fabReview.setClickable(true);
                    home.setText(R.string.home);
                    home.setBackgroundColor(Color.rgb(79, 74, 84));
                    guide.setText(R.string.hire_guide);
                    guide.setBackgroundColor(Color.rgb(79, 74, 84));
                    hotel.setText(R.string.hotels);
                    hotel.setBackgroundColor(Color.rgb(79, 74, 84));
                    restaurant.setText(R.string.restuarants);
                    restaurant.setBackgroundColor(Color.rgb(79, 74, 84));
                    visited.setText(R.string.visited_check);
                    visited.setBackgroundColor(Color.rgb(79, 74, 84));
                    review.setText(R.string.review);
                    review.setBackgroundColor(Color.rgb(79, 74, 84));
                    isOpen = true;
                }
            }
        });

        fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        fabGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, GuideListActivity.class);

                Bundle bundlePinPoint = new Bundle();
                bundlePinPoint.putString("pin_point_id", pinPointID);
                intent.putExtras(bundlePinPoint);

                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        fabHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailsActivity.this, "Hotel", Toast.LENGTH_SHORT).show();
            }
        });

        fabRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailsActivity.this, "Restaurant", Toast.LENGTH_SHORT).show();
            }
        });

        fabVisited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailsActivity.this, "Visited", Toast.LENGTH_SHORT).show();
            }
        });

        fabReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailsActivity.this, "Review", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
