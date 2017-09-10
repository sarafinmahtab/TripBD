package com.sarafinmahtab.tripbd.guideList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sarafinmahtab.tripbd.MySingleton;
import com.sarafinmahtab.tripbd.R;
import com.sarafinmahtab.tripbd.ServerAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuideListActivity extends AppCompatActivity {

    String guideListUrl = ServerAddress.getMyServerAddress().concat("guide_list_for_hire.php");
//    String guideListUrl = "http://192.168.43.65/TripBD/guide_list_for_hire.php";

    String pinPointID;

    SearchView searchView;
    EditText searchEditText;
    ImageView closeButton;

    RecyclerView recyclerView;
    GuideListAdapter guideListAdapter;
    List<GuideData> guideDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_list);

        Toolbar guideListToolbar = (Toolbar) findViewById(R.id.guide_list_toolbar);
        setSupportActionBar(guideListToolbar);

        guideListToolbar.setTitleTextColor(0xFFFFFFFF);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();

        pinPointID = bundle.getString("pin_point_id");

        searchView = (SearchView) findViewById(R.id.guide_list_searchview);
        searchEditText = (EditText) findViewById(R.id.search_src_text);
        closeButton = (ImageView) findViewById(R.id.search_close_btn);
        recyclerView = (RecyclerView) findViewById(R.id.guide_list_recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        guideDataList = new ArrayList<>();

        StringRequest stringRequestForStdList = new StringRequest(Request.Method.POST, guideListUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("guide_list_to_hire");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        GuideData guideData = new GuideData(
                                obj.getString("user_id"),
                                obj.getString("nick_name"),
                                obj.getString("mobile_phone"),
                                obj.getString("email"));
                        guideDataList.add(guideData);
                    }

                    guideListAdapter = new GuideListAdapter(guideDataList, GuideListActivity.this);
                    recyclerView.setAdapter(guideListAdapter);
                } catch (JSONException e) {
                    Toast.makeText(GuideListActivity.this, response, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GuideListActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("pin_point_id", pinPointID);

                return params;
            }
        };

        MySingleton.getMyInstance(GuideListActivity.this).addToRequestQueue(stringRequestForStdList);

        searchView.onActionViewExpanded();
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search Guides To Hire");

        if(!searchView.isFocused()) {
            searchView.clearFocus();
        }

//        stdSearchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchView.setIconified(false);
//            }
//        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

//                guideListAdapter.checkQueryFromList(newText.toLowerCase());
//                Toast.makeText(StudentList.this, newText, Toast.LENGTH_LONG).show();

                return true;
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear the text from EditText view
                searchEditText.setText("");

                //Clear query
                searchView.setQuery("", false);
                guideListAdapter.notifyDataSetChanged();
                searchView.clearFocus();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
