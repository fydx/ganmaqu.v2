package com.xstrikers.ganmaquv2.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xstrikers.ganmaquv2.R;
import com.xstrikers.ganmaquv2.ui.adapter.typeSelectAdapter;
import com.xstrikers.ganmaquv2.ui.ganmaquApplication;

/**
 * Created by LB on 14-2-15.
 */
public class typeDialog extends Dialog {
    private Context mContext;
    private ganmaquApplication mGanmaquApplication;
    public typeDialog(Context context, ganmaquApplication application) {
        super(context,R.style.CircleDialog);
        this.mContext = context;
        this.mGanmaquApplication = application;
        setCustomDialog();
    }

    private void setCustomDialog() {

        final View mView = LayoutInflater.from(getContext()).inflate(
                R.layout.dialog_type, null);
        super.setContentView(mView);
        GridView gridView = (GridView) mView.findViewById(R.id.gridView_types);
        typeSelectAdapter mTypeSelectAdapter = new typeSelectAdapter(this.getContext());
        gridView.setAdapter(mTypeSelectAdapter);
        RadioGroup radioGroup = (RadioGroup)mView.findViewById(R.id.radioGroup);
        final RadioButton allDay = (RadioButton)mView.findViewById(R.id.radioButton_full);
        final RadioButton partDay = (RadioButton)mView.findViewById(R.id.radioButton_part);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(allDay.getId() == checkedId)
                {
                    Log.i("select full", "selected");
                    mGanmaquApplication.allDay = true;

                }
                if (partDay.getId() == checkedId) {
                    Log.i("select part", "selected");
                    mGanmaquApplication.allDay = false;
                }
            }
        });
    }

}
