package com.sarafinmahtab.tripbd.guideProfile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.sarafinmahtab.tripbd.DetailsActivity;
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

/**
 * Created by Arafin on 8/19/2017.
 */

public class GuideExpListAdapter extends RecyclerView.Adapter<GuideExpListAdapter.GuideExpListViewHolder> {

    private String uploadExpUrl = ServerAddress.getMyServerAddress().concat("upload_exp.php");

    private String guideID;

    private List<GuideExpListItem> guideExpList;
    private Context context;

    private ArrayList<GuideExpListItem> newGuideExpList;

    private AlertDialog.Builder builder;

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
    public void onBindViewHolder(final GuideExpListViewHolder holder, int position) {
        final GuideExpListItem guideExpListItem = newGuideExpList.get(position);

        holder.textViewExpItemTitle.setText(guideExpListItem.getGuideExpItemName());

        holder.imageViewExpItemAdd.setImageResource(0);
        holder.imageViewExpItemAdd.setImageResource(guideExpListItem.getConfirmImageID());
        holder.imageViewExpItemAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
//                Toast.makeText(context, String.valueOf(guideExpListItem.getGuideExpPlaceID()) + " Added", Toast.LENGTH_LONG).show();

                StringRequest stringRequestforPlaceExpAdd = new StringRequest(Request.Method.POST, uploadExpUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        builder = new AlertDialog.Builder(context);

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String data_report = jsonObject.getString("data_report");

                            switch (data_report) {
                                case "exist":
                                    builder.setTitle("Apply Failed");
                                    display_alert("You're already listed as a guide for " + guideExpListItem.getGuideExpPlaceName());
                                    break;
                                case "success":
                                    guideExpListItem.setConfirmImageID(R.drawable.ic_exp_choice_confirm_btn);
                                    holder.imageViewExpItemAdd.setImageResource(0);
                                    holder.imageViewExpItemAdd.setImageResource(guideExpListItem.getConfirmImageID());

                                    Toast.makeText(context, "You're Are Successfully Listed to Hire!!", Toast.LENGTH_LONG).show();

                                    break;
                                case "failed":
                                    builder.setTitle("Apply Failed");
                                    display_alert("Insertion failed!! Please try again.");
                                    break;
                            }
                        } catch (JSONException e) {
                            display_alert(response);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        display_alert(error.getMessage());
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
                Intent intent = new Intent(context, DetailsActivity.class);

                Bundle bundle = new Bundle();

                bundle.putString("pin_point_id", guideExpListItem.getGuideExpPlaceID());
                bundle.putString("details_link", guideExpListItem.getGuideExpPlaceDetailLink());

                intent.putExtras(bundle);

                context.startActivity(intent);
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

    public void display_alert(String message) {
        builder.setMessage(message);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
