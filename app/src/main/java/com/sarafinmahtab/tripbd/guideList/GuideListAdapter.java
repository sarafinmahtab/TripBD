package com.sarafinmahtab.tripbd.guideList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sarafinmahtab.tripbd.R;

import java.util.List;

/**
 * Created by Arafin on 8/20/2017.
 */

public class GuideListAdapter extends RecyclerView.Adapter<GuideListAdapter.GuideListViewHolder>{

    private List<GuideData> guideDataList;
    private Context context;

    public GuideListAdapter(List<GuideData> guideDataList, Context context) {
        this.guideDataList = guideDataList;
        this.context = context;
    }

    @Override
    public GuideListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.guide_list_item, parent, false);

        return new GuideListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GuideListViewHolder holder, int position) {

        final GuideData guideData = guideDataList.get(position);

        holder.textViewGuideName.setText(guideData.getGuideNickName());
        holder.callGuideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(String.format("tel:%s", guideData.getGuideMobile())));
                context.startActivity(intent);
            }
        });

        holder.linearLayoutGuideList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, guideData.getGuideNickName(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return guideDataList.size();
    }

    public static class GuideListViewHolder extends RecyclerView.ViewHolder {

        TextView textViewGuideName;
        ImageButton callGuideBtn;

        LinearLayout linearLayoutGuideList;

        public GuideListViewHolder(View itemView) {
            super(itemView);

            textViewGuideName = itemView.findViewById(R.id.guide_name);
            callGuideBtn = itemView.findViewById(R.id.guide_call_btn);

            linearLayoutGuideList = itemView.findViewById(R.id.guide_list_linear);
        }
    }
}
