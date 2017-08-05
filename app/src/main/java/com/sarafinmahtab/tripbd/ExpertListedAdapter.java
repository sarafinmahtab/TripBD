package com.sarafinmahtab.tripbd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Arafin on 8/5/2017.
 */

public class ExpertListedAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater expInflater;
    private List<ExpPlace> expList = null;
    private ArrayList<ExpPlace> expArrayList;

    ExpertListedAdapter(Context context, List<ExpPlace> expList) {
        this.context = context;
        this.expList = expList;

        expInflater = LayoutInflater.from(context);

        this.expArrayList = new ArrayList<>();
        this.expArrayList.addAll(expList);
    }

    public class ViewHolder {
        TextView expPlaceName;
    }


    @Override
    public int getCount() {
        return expList.size();
    }

    @Override
    public Object getItem(int i) {
        return expList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ExpertListedAdapter.ViewHolder holder;
        if (view == null) {
            holder = new ExpertListedAdapter.ViewHolder();
            view = expInflater.inflate(R.layout.exp_row_items, null);

            // Locate the TextViews in search_query_row_items.xml
            holder.expPlaceName = view.findViewById(R.id.expPlaceName);
            view.setTag(holder);
        } else {
            holder = (ExpertListedAdapter.ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.expPlaceName.setText(String.valueOf(expList.get(i).getExpPlaceName()));
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        expList.clear();
        if (charText.length() == 0) {
            expList.addAll(expArrayList);
        } else {
            for (ExpPlace wp : expArrayList) {
                if (wp.getExpPlaceName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    expList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
