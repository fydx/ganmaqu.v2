package com.xstrikers.ganmaquv2.ui.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xstrikers.ganmaquv2.R;
import com.xstrikers.ganmaquv2.model.Place;

/**
 * Created by LB on 14-1-27.
 */
public class PlaceAdapter extends com.haarman.listviewanimations.ArrayAdapter {
  private List<Place> Places;
  private Context mContext;

  public PlaceAdapter(Context context, int resource, int textViewResourceId, List<Place> transPlaces) {
    // super(context, resource, textViewResourceId, transPlaces);
    super(transPlaces);
    this.Places = transPlaces;
    this.mContext = context;
    Log.i("ganmaqu",
        "In PlacesAdapter: " + String.valueOf(resource) + "  " + String.valueOf(textViewResourceId)
            + "  " + transPlaces.toString());
  }

  private class ViewHolder {
    TextView typeTextView, nameTextView, addrTextView, payTextView;
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    // View v = super.getView(position, convertView, parent);
    View v = LayoutInflater.from(mContext).inflate(R.layout.listitem_result, parent, false);
    Log.i("ganmaqu", Places.get(position).toString());
    if (v != convertView && v != null) {
      ViewHolder holder = new ViewHolder();
      holder.nameTextView = (TextView) v.findViewById(R.id.result_name);
      holder.typeTextView = (TextView) v.findViewById(R.id.result_type);
      holder.addrTextView = (TextView) v.findViewById(R.id.result_address);
      holder.payTextView = (TextView) v.findViewById(R.id.result_pay);

      // holder.dragImageView = iv;
      v.setTag(holder);
    }

    ViewHolder holder = (ViewHolder) v.getTag();
    holder.nameTextView.setText(Places.get(position).getShopName());
    holder.typeTextView.setText(Places.get(position).getMainType());
    holder.addrTextView.setText(Places.get(position).getAddress());
    holder.payTextView.setText(String.valueOf(Places.get(position).getCost()));


    return v;
  }

}
