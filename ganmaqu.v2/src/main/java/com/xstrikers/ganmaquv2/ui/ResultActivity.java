package com.xstrikers.ganmaquv2.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.xstrikers.ganmaquv2.R;
import com.xstrikers.ganmaquv2.model.Place;
import com.xstrikers.ganmaquv2.ui.adapter.PlaceAdapter;

/**
 * Created by LB on 14-1-25.
 */
public class ResultActivity extends ActionBarActivity {
    private List<Place> places;
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_result);
    /*
     * action bar 设置
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
    places = decodeJson(getIntent().getStringExtra("result"));
    Log.i("ganmaqu","In Result Activity : " + places.toString());
    ListView listView = (ListView) findViewById(R.id.listView_result);
    PlaceAdapter placeAdapter  =  new PlaceAdapter(ResultActivity.this,R.layout.listitem_result,R.id.result_name,places);
    listView.setAdapter(placeAdapter);

  }

  private List<Place> decodeJson(String result)
  {
    List<Place> places;
    JSONArray jsonArray;
    places = new ArrayList<Place>();
    try {
      jsonArray = new JSONArray(result);
      for (int i = 0; i < jsonArray.length(); i++) {
        System.out.println(String.valueOf(i));
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        Place tempPlace =  JsonobTOPlace(jsonObject);
        System.out.println(tempPlace.toString());
        places.add(tempPlace);
      }
    } catch (JSONException e)
    {}
    return places;
  }

  private Place JsonobTOPlace(JSONObject jsonObject) throws JSONException
  {
    Place temp_place = new Place();
            temp_place.setAddress(jsonObject.getString("address"));
      temp_place.setCost(jsonObject.getInt("cost"));
      temp_place.setDetailType(jsonObject.getString("detailType"));
      temp_place.setId(jsonObject.getInt("id"));
      temp_place.setMainType(jsonObject.getString("mainType"));
      temp_place.setlng(jsonObject.getDouble("pos_x"));
      temp_place.setlat(jsonObject.getDouble("pos_y"));
      temp_place.setRate(jsonObject.getInt("rate"));
      temp_place.setShopName(jsonObject.getString("shopName"));
      temp_place.setSuitType(jsonObject.getString("suitType"));
      temp_place.setTelNumber(jsonObject.getString("telNumber"));
    try {
      temp_place.setTime(jsonObject.getString("time"));
    } catch (Exception e) {
      Log.w("JSON in decode", "can not find TIME,add afternoon temp");
      temp_place.setTime("下午");
    }
    try {
      temp_place.setPicUrl(jsonObject.getString("url"));
    } catch (Exception jsonException) {
      Log.w("JSON in decode", "can not find URL");
    }
    temp_place.setWeight(jsonObject.getInt("weight"));
    return temp_place;
  }
}
