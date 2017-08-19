package com.sarafinmahtab.tripbd.guide_profile;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Arafin on 8/19/2017.
 */

public class GuideExpListAdapter extends RecyclerView.Adapter<GuideExpListAdapter.GuideExpListViewHolder> {

    String guideID;
    String upload_exp_url = "http://192.168.0.63/TripBD/upload_exp.php";

    List<GuideExpListItem> guideExpList;
    Context context;

    private ArrayList<GuideExpListItem> newGuideExpList;

    public GuideExpListAdapter(List<GuideExpListItem> guideExpList, Context context, String guideID) {
        this.guideExpList = guideExpList;
        this.context = context;
        this.guideID = guideID;

        newGuideExpList = new ArrayList<>();
        newGuideExpList.addAll(this.guideExpList);
    }

    @Override
    public GuideExpListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.update_guide_exp_list_item, parent, false);

        return new GuideExpListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GuideExpListViewHolder holder, int position) {
        final GuideExpListItem guideExpListItem = newGuideExpList.get(position);

        holder.textViewExpItemTitle.setText(guideExpListItem.getGuideExpItemName());

        holder.imageViewExpItemAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
//                Toast.makeText(context, String.valueOf(guideExpListItem.getGuideExpPlaceID()) + " Added", Toast.LENGTH_LONG).show();

                StringRequest stringRequestforPlaceExpAdd = new StringRequest(Request.Method.POST, upload_exp_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String data_report = jsonObject.getString("data_report");

                            switch (data_report) {
                                case "success":
                                    Toast.makeText(context,
                                            "You've listed as Expert Guide for " + guideExpListItem.getGuideExpPlaceName(),
                                            Toast.LENGTH_LONG).show();
                                    break;
                                case "failed":
                                    Toast.makeText(context,
                                            "Insertion failed!! Please try again.",
                                            Toast.LENGTH_LONG).show();
                                    break;
                            }
                        } catch (JSONException e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        params.put("exp_place_id", guideExpListItem.getGuideExpPlaceID());
                        params.put("exp_user_id", guideID);

                        return params;
                    }
                };

                MySingleton.getMyInstance(context).addToRequestQueue(stringRequestforPlaceExpAdd);
            }
        });

        holder.linearLayoutExpItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, String.valueOf(guideExpListItem.getGuideExpPlaceDetailLink()), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != newGuideExpList ? newGuideExpList.size() : 0);
    }

    public static class GuideExpListViewHolder extends RecyclerView.ViewHolder {

        TextView textViewExpItemTitle;
        ImageButton imageViewExpItemAdd;

        LinearLayout linearLayoutExpItem;

        public GuideExpListViewHolder(View itemView) {
            super(itemView);

            textViewExpItemTitle = itemView.findViewById(R.id.exp_place_title);
            imageViewExpItemAdd = itemView.findViewById(R.id.add_exp_place_btn);

            linearLayoutExpItem = itemView.findViewById(R.id.exp_list_linear);
        }
    }

    public void checkQueryFromList(final String query) {
        int i, j, m;

        newGuideExpList.clear();

        for(i = 0; i < guideExpList.size(); i++)
        {

            for(j = 0; j < guideExpList.get(i).getGuideExpItemName().length(); j++)
            {
                if(j+query.length() > guideExpList.get(i).getGuideExpItemName().length()) {
                    break;
                }

                boolean ck = true;

                for(m = 0; m < query.length(); m++)
                {
                    if(query.charAt(m) != guideExpList.get(i).getGuideExpItemName().toLowerCase().charAt(j+m)) {
                        ck = false;
                    }
                }

                if(ck) {
                    newGuideExpList.add(guideExpList.get(i));
                    break;
                }
            }
        }

        notifyDataSetChanged();
    }
}
