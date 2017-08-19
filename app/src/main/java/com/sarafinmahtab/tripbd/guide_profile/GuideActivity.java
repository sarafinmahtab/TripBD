package com.sarafinmahtab.tripbd.guide_profile;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sarafinmahtab.tripbd.MySingleton;
import com.sarafinmahtab.tripbd.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {

    String user_id;
    String upload_exp = "http://192.168.0.63/TripBD/upload_exp.php";
    String exp_url = "http://192.168.0.63/TripBD/exp_places.php";

    private List<GuideExpListItem> expPlaceList;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        Toolbar toolbar = (Toolbar) findViewById(R.id.guide_toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.guide_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.guide_tabs);
        tabLayout.setupWithViewPager(mViewPager);

//        expPlaceList = new ArrayList<>();
//        updateGuideExpListDataFromDataBase();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new GuideProfile();
                case 1:
                    return new UpdateGuideExpList();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Profile";
                case 1:
                    return "Update List";
            }
            return null;
        }
    }

    private void updateGuideExpListDataFromDataBase() {
        StringRequest stringRequestExpList = new StringRequest(Request.Method.POST, exp_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("exp_place_query_list");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        GuideExpListItem courseItem = new GuideExpListItem(
                                obj.getString("pin_point_id"),
                                obj.getString("pin_point_name"),
                                obj.getString("centre_point_name"),
                                obj.getString("details_link")
                        );

                        expPlaceList.add(courseItem);
                    }

                } catch (JSONException e) {
                    Toast.makeText(GuideActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GuideActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });

        MySingleton.getMyInstance(GuideActivity.this).addToRequestQueue(stringRequestExpList);
    }

    public List<GuideExpListItem> getExpPlaceList() {
        return expPlaceList;
    }
}
