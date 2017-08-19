package com.sarafinmahtab.tripbd.guide_list_for_hire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.sarafinmahtab.tripbd.DetailsActivity;
import com.sarafinmahtab.tripbd.R;

public class GuideListActivity extends AppCompatActivity {

    String pinPointID;

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

        Toast.makeText(GuideListActivity.this, String.valueOf(pinPointID), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
