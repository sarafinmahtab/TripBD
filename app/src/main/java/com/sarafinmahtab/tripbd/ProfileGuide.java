package com.sarafinmahtab.tripbd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileGuide extends AppCompatActivity {

    ListView exp_listed_place;
    ExpertListedAdapter exp_adapter;
    SearchView exp_searchView;
    ImageView exp_closeButton;
    EditText exp_searchEditText;
    ArrayList<ExpPlace> exp_arraylist = new ArrayList<>();

    String user_id;
    String upload_exp = "http://192.168.0.63/TripBD/upload_exp.php";
    String exp_url = "http://192.168.0.63/TripBD/exp_places.php";
//    String[] listA = new String[]{"Lion", "Tiger", "Dog",
//            "Cat", "Tortoise", "Rat", "Elephant", "Fox",
//            "Cow","Donkey","Monkey"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_guide);

        Bundle bundle = getIntent().getExtras();
        user_id = bundle.getString("user_id");

        StringRequest expStringRequest = new StringRequest(Request.Method.POST, exp_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                exp_listed_place = (ListView) findViewById(R.id.exp_listView);

                try {
                    final JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("exp_place_query_list");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        ExpPlace expPlaceObj = new ExpPlace(obj.getString("pin_point_id"),
                                obj.getString("pin_point_name"));

                        // Binds all strings into an array
                        exp_arraylist.add(expPlaceObj);
                    }

                    // Pass results to ListViewAdapter Class
                    exp_adapter = new ExpertListedAdapter(ProfileGuide.this, exp_arraylist);

                    // Binds the Adapter to the ListView
                    exp_listed_place.setAdapter(exp_adapter);

                    exp_listed_place.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            final ExpPlace exp_placeObj = (ExpPlace) exp_adapter.getItem(i);

                            StringRequest stringRequestP = new StringRequest(Request.Method.POST, upload_exp, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String code = jsonObject.getString("code");

                                        Toast.makeText(ProfileGuide.this, code, Toast.LENGTH_LONG).show();

                                    } catch (JSONException e) {
                                        Toast.makeText(ProfileGuide.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(ProfileGuide.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                    error.printStackTrace();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();

                                    params.put("exp_place_id", exp_placeObj.getExpPlaceID());
                                    params.put("exp_user_id", user_id);

                                    return params;
                                }
                            };

                            MySingleton.getMyInstance(ProfileGuide.this).addToRequestQueue(stringRequestP);
//                                    adapter.notifyDataSetChanged();
                        }
                    });

                } catch (JSONException e) {
                    Toast.makeText(ProfileGuide.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileGuide.this, error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });

        MySingleton.getMyInstance(ProfileGuide.this).addToRequestQueue(expStringRequest);

        exp_searchView = (SearchView) findViewById(R.id.expSearchView);
        exp_searchEditText = (EditText) findViewById(R.id.search_src_text);
//        searchEditText.setHint("Find Tour Places");

        exp_searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exp_searchView.setIconified(false);
            }
        });

        exp_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                exp_adapter.filter(newText);
                return false;
            }
        });
    }
}
