package com.sarafinmahtab.tripbd.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sarafinmahtab.tripbd.DetailsActivity;
import com.sarafinmahtab.tripbd.R;

import java.util.List;

/**
 * Created by Arafin on 7/24/2017.
 */

public class SearchQueryListAdapter extends RecyclerView.Adapter<SearchQueryListAdapter.SearchQueryViewHolder> {

    private Context context;
    private List<Place> placeList;

    SearchQueryListAdapter(Context context, List<Place> placeList) {
        this.context = context;
        this.placeList = placeList;
    }

    @Override
    public SearchQueryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_query_row_items, parent, false);

        return new SearchQueryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchQueryViewHolder holder, int position) {
        final Place place = placeList.get(position);

        holder.textViewPlaceName.setText(place.getPinPointName());

        holder.linearLayoutAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("place_id", place.getPinPointID());
                bundle.putString("details_link", place.getDetailsLink());
                intent.putExtras(bundle);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }


    public static class SearchQueryViewHolder extends RecyclerView.ViewHolder {

        TextView textViewPlaceName;
        LinearLayout linearLayoutAdapter;

        public SearchQueryViewHolder(View itemView) {
            super(itemView);

            textViewPlaceName = itemView.findViewById(R.id.place_name);
            linearLayoutAdapter = itemView.findViewById(R.id.linearLayoutAdapter);
        }
    }
}
