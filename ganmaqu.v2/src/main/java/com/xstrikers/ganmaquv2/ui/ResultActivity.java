package com.xstrikers.ganmaquv2.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.xstrikers.ganmaquv2.R;

/**
 * Created by LB on 14-1-25.
 */
public class ResultActivity extends ActionBarActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         action bar 设置
         */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP,
                ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("推荐路线");
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.main_actionbar_bg));
        int actionBarTitleId =
                getResources().getSystem().getIdentifier("action_bar_title", "id", "android");
        if (actionBarTitleId > 0) {
            TextView title = (TextView) findViewById(actionBarTitleId);
            if (title != null) {
                title.setTextColor(Color.WHITE);
            }
        }
        

    }
}