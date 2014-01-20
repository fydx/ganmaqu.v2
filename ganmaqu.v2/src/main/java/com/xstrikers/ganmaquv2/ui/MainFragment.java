package com.xstrikers.ganmaquv2.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xstrikers.ganmaquv2.R;

/**
 * Created by LB on 14-1-18.
 */
public class MainFragment extends android.support.v4.app.Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;

        }

}