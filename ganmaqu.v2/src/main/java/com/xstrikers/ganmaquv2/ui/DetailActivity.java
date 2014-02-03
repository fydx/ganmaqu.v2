package com.xstrikers.ganmaquv2.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.xstrikers.ganmaquv2.R;


/**
 * Created by LB on 14-2-3.
 */
public class DetailActivity extends ActionBarActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
 /*
     * action bar 设置
     */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP,
                ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("地点详情");
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.main_actionbar_bg));
        int actionBarTitleId =
                getResources().getSystem().getIdentifier("action_bar_title", "id", "android");
        if (actionBarTitleId > 0) {
            TextView title = (TextView) findViewById(actionBarTitleId);
            if (title != null) {
                title.setTextColor(Color.WHITE);
            }
        }
        TextView nameTextView = (TextView)findViewById(R.id.textView_detail_shopname);
        TextView addrTextView = (TextView)findViewById(R.id.textView_detail_address);
        TextView costTextView = (TextView)findViewById(R.id.textView_detail_cost);
        nameTextView.setText(getIntent().getStringExtra("name"));
        addrTextView.setText(getIntent().getStringExtra("addr"));
        costTextView.setText(getIntent().getStringExtra("cost"));
    }
}