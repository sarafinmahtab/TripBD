package com.sarafinmahtab.tripbd.guide_profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sarafinmahtab.tripbd.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arafin on 8/19/2017.
 */

public class GuideExpListAdapter extends RecyclerView.Adapter<GuideExpListAdapter.GuideExpListViewHolder> {

    List<GuideExpListItem> guideExpList;
    Context context;

    private ArrayList<GuideExpListItem> newGuideExpList;

    public GuideExpListAdapter(List<GuideExpListItem> guideExpList, Context context) {
        this.guideExpList = guideExpList;
        this.context = context;

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
            public void onClick(View view) {
                Toast.makeText(context, String.valueOf(guideExpListItem.getGuideExpPlaceID()) + " Added", Toast.LENGTH_LONG).show();
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
