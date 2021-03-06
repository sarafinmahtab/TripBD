package com.sarafinmahtab.tripbd.guideProfile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class UpdateGuideExpList extends Fragment {

    String expUrl = ServerAddress.getMyServerAddress().concat("exp_places.php");

    SearchView guideExpListSsearchView;
    EditText guideExpListSearchEditText;
    ImageView guideExpListCloseButton;

    RecyclerView guideExpListRecyclerView;
    List<GuideExpListItem> guideExpList;
    GuideExpListAdapter guideExpListAdapter;

    GuideActivity guideActivity;

    String tourGuideID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_update_guide_exp_list, container, false);

        guideActivity = (GuideActivity) getActivity();
        tourGuideID = guideActivity.userID;

        guideExpListSsearchView = rootView.findViewById(R.id.frag_searchView_place_exp);
        guideExpListSearchEditText = rootView.findViewById(R.id.search_src_text);
        guideExpListCloseButton = rootView.findViewById(R.id.search_close_btn);

        guideExpListRecyclerView = rootView.findViewById(R.id.frag_recyclerView_place_list);
        guideExpListRecyclerView.setHasFixedSize(true);
        guideExpListRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        guideExpList = new ArrayList<>();

        //Volley List Data
//        GuideActivity guideActivity = (GuideActivity) getActivity();
//        guideExpList = guideActivity.getExpPlaceList();

        StringRequest stringRequestExpList = new StringRequest(Request.Method.POST, expUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("exp_place_query_list");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        GuideExpListItem guideExpListItem = new GuideExpListItem(
                                obj.getString("pin_point_id"),
                                obj.getString("pin_point_name"),
                                obj.getString("centre_point_name"),
                                obj.getString("details_link")
                        );

                        guideExpList.add(guideExpListItem);
                    }

                    guideExpListAdapter = new GuideExpListAdapter(guideExpList, rootView.getContext(), guideActivity.userID);
                    guideExpListRecyclerView.setAdapter(guideExpListAdapter);

                } catch (JSONException e) {
                    Toast.makeText(rootView.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(rootView.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("tour_guide_id", tourGuideID);

                return params;
            }
        };

        MySingleton.getMyInstance(rootView.getContext()).addToRequestQueue(stringRequestExpList);

        guideExpListSsearchView.onActionViewExpanded();
        guideExpListSsearchView.setIconifiedByDefault(false);
        guideExpListSsearchView.setQueryHint("Select Where You're Expert");

        if(!guideExpListSsearchView.isFocused()) {
            guideExpListSsearchView.clearFocus();
        }

//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchView.setIconified(false);
//            }
//        });

        guideExpListSsearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                guideExpListAdapter.checkQueryFromList(newText);

//                Toast.makeText(rootView.getContext(), newText, Toast.LENGTH_LONG).show();

                return false;
            }
        });

        guideExpListCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear the text from EditText view
                guideExpListSearchEditText.setText("");

                //Clear query
                guideExpListSsearchView.setQuery("", false);
                guideExpListAdapter.notifyDataSetChanged();
                guideExpListSsearchView.clearFocus();
            }
        });

        return rootView;
    }
}
