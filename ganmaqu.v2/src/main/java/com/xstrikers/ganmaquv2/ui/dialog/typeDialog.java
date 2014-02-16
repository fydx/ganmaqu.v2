package com.xstrikers.ganmaquv2.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

import com.xstrikers.ganmaquv2.R;
import com.xstrikers.ganmaquv2.ui.adapter.typeSelectAdapter;

/**
 * Created by LB on 14-2-15.
 */
public class typeDialog extends Dialog {
    private Context mContext;
    public typeDialog(Context context) {
        super(context,R.style.CircleDialog);
        this.mContext = context;
        setCustomDialog();
    }

    private void setCustomDialog() {

        final View mView = LayoutInflater.from(getContext()).inflate(
                R.layout.dialog_type, null);
        super.setContentView(mView);
        GridView gridView = (GridView) mView.findViewById(R.id.gridView_types);
        typeSelectAdapter mTypeSelectAdapter = new typeSelectAdapter(this.getContext());
        gridView.setAdapter(mTypeSelectAdapter);
    }

}
