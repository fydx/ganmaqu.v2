package com.xstrikers.ganmaquv2.util;

import android.util.Log;

import com.xstrikers.ganmaquv2.model.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LB on 14-2-7.
 */
public class DecodeUtil {

    public static List<Place> decodeJson(String result)
    {
        List<Place> places;
        JSONArray jsonArray;
        places = new ArrayList<Place>();
        try {
            jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                System.out.println(String.valueOf(i));
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Place tempPlace = JsonobTOPlace(jsonObject);
                System.out.println(tempPlace.toString());
                places.add(tempPlace);
            }
        } catch (JSONException e)
        {}
        return places;
    }

    public static Place JsonobTOPlace(JSONObject jsonObject) throws JSONException
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
