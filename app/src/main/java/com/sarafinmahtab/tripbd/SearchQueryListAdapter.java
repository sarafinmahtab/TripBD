package com.sarafinmahtab.tripbd;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arafin on 7/24/2017.
 */

class SearchQueryListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Place> placeList = null;
    private ArrayList<Place> placeArraylist;

    SearchQueryListAdapter(Context context, List<Place> placeList) {
        this.context = context;
        this.placeList = placeList;

        inflater = LayoutInflater.from(context);

        this.placeArraylist = new ArrayList<>();
        this.placeArraylist.addAll(placeList);
    }

    public class ViewHolder {
        TextView placeName;
    }

    @Override
    public int getCount() {
        return placeList.size();
    }

    @Override
    public Place getItem(int position) {
        return placeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.search_query_row_items, null);

            // Locate the TextViews in search_query_row_items.xml
            holder.placeName = view.findViewById(R.id.place_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.placeName.setText(String.valueOf(placeList.get(position).getCentrePointName()));
        return view;
    }
}
