package com.xstrikers.ganmaquv2.ui.adapter;

/**
 * Created by LB on 14-2-16.
 */
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.xstrikers.ganmaquv2.R;
import com.xstrikers.ganmaquv2.ui.ganmaquApplication;

public class typeSelectAdapter extends BaseAdapter {
    private final String[] types = { "美食", "购物", "电影院", "风景", "咖啡/甜点", "KTV" };
    private final int[] typeDrawable = { R.drawable.button_eat,
            R.drawable.button_shop, R.drawable.button_movie,
            R.drawable.button_scene, R.drawable.button_coffee,
            R.drawable.button_ktv };
    private Context context;
    private LayoutInflater mInflater;
    private ganmaquApplication ganmaquApplication;

    private int count;
    private class holder{
        ImageView imageView;
        TextView textView;
        CheckBox checkbox;
    }
    public typeSelectAdapter(Context c) {
        super();
        this.context = c;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.i("ganmaqu","PLACE TYPES NUM : " + String.valueOf(types.length));
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return types.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return types[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ganmaquApplication=(ganmaquApplication)context.getApplicationContext();
        Log.i("ganmaqu" , "ALLOCATE getView : " + String.valueOf(position));
        final holder holder1;
        if (convertView == null)
        {
            convertView = mInflater
                    .inflate(R.layout.griditem_type, null);
            holder1 = new holder();
            holder1.textView=(TextView)convertView.findViewById(R.id.textview_typeSelect);
            holder1.imageView=(ImageView)convertView.findViewById(R.id.button_typeSelect);
            holder1.checkbox=(CheckBox)convertView.findViewById(R.id.checkbox_typeSelect);
            convertView.setTag(holder1);
        }
        else {
            holder1 = (holder) convertView.getTag();
        }
        holder1.imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (holder1.checkbox.isChecked()==true) {
                    holder1.checkbox.setChecked(false);
                }
                else {
                    holder1.checkbox.setChecked(true);
                }


            }
        });
        holder1.textView.setText(types[position]);
        holder1.imageView.setImageDrawable(convertView.getContext().getResources().getDrawable(typeDrawable[position]));
        holder1.checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    ganmaquApplication.selectType[position] = true;
                    Log.i("set true", String.valueOf(position));
                }
                else {
                    ganmaquApplication.selectType[position] = false;
                    Log.i("set false", String.valueOf(position));
                }
            }
        });
        return convertView;
    }

}