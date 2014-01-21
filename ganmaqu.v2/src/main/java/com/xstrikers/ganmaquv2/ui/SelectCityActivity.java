package com.xstrikers.ganmaquv2.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.xstrikers.ganmaquv2.R;
import com.xstrikers.ganmaquv2.map.LocationManagerHelper;

/**
 * Created by LB on 14-1-20.
 */
public class SelectCityActivity extends Activity {

  // private List<String> cities;
  private ListView listView;
  private SharedPreferences userInfo;
  private ArrayList<HashMap<String, String>> cities;
  private LocationManagerHelper locationManagerHelper;
  private RequestQueue mRequestQueue;


  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_select_city);
    listView = (ListView) findViewById(R.id.listView_city);
    // cities = new ArrayList<String>();

    userInfo = this.getSharedPreferences("userInfo", 0);
    locationManagerHelper = LocationManagerHelper.getInstance();
    locationManagerHelper.setLocationManager((LocationManager) this
        .getSystemService(Context.LOCATION_SERVICE));
    locationManagerHelper.start();
    mRequestQueue = Volley.newRequestQueue(this);
    getAvailableCities();
    getLocationCity();


  }
  private void getAvailableCities()
  {
      StringBuilder getCitiesURL = new StringBuilder();
      getCitiesURL.append("http://" + getResources().getString(R.string.Hostname)).append(
              ":8080/?command=getavailablecity");
      Log.i("ganmaqu", getCitiesURL.toString());
      JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
              Request.Method.GET,
              getCitiesURL.toString(),
              null,
              new Response.Listener<JSONObject>() {
                  @Override
                  public void onResponse(JSONObject jsonObject) {
                      cities = getData(jsonObject);
                      SimpleAdapter simpleAdapter = new SimpleAdapter(getApplicationContext(),
                              cities,
                              R.layout.listitem_select_city,
                              new String[]
                                      {"city"},
                              new int[] {
                                      R.id.textView_city
                              }
                      );
                      listView.setAdapter(simpleAdapter);
                      AdapterView.OnItemClickListener itemClickListener =
                              new AdapterView.OnItemClickListener() {
                                  @Override
                                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    setCity(cities.get(position).get("city"));
                                  }
                              };
                      listView.setOnItemClickListener(itemClickListener);
                  }
              },
              new Response.ErrorListener() {
                  @Override
                  public void onErrorResponse(VolleyError volleyError) {
                      Toast.makeText(getApplicationContext(), "网络访问错误 : " + volleyError.getMessage(),
                              Toast.LENGTH_SHORT).show();
                  }
              }
      );
      mRequestQueue.add(jsonObjectRequest);
  }
  private void getLocationCity()
  {
      StringBuilder geocodingURL =
              new StringBuilder(
                      "http://api.map.baidu.com/geocoder/v2/?ak=ogjpAwKHx0XBwsnxKDr5plXR&output=json&pois=0&coordtype=wgs84ll&location=");
      geocodingURL.append(locationManagerHelper.getLocation());
      Log.i("ganmaqu", geocodingURL.toString());
      JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
              Request.Method.GET,
              geocodingURL.toString(),
              null,
              new Response.Listener<JSONObject>() {
                  @Override
                  public void onResponse(JSONObject response) {
                      try {
                          Log.i("ganmaqu", response.toString());
                          final String city =
                                  (new JSONObject(new JSONObject(response.getString("result"))
                                          .getString("addressComponent"))).getString("city");
                          Log.i("ganmaqu", city);
                          userInfo.edit().putBoolean("firstBoot", false).commit();
                          userInfo.edit().putString("city", city).commit();
                          TextView textView = (TextView)findViewById(R.id.textView_selectcity_auto);
                          textView.setText(city);
                          textView.setOnClickListener( new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                 setCity(city);
                              }
                          });
                      }
                      catch (JSONException e)
                      {
                          Log.i("ganmaqu", "excption in DecodeJson city");
                      }

                  }
              },
              new Response.ErrorListener() {
                  @Override
                  public void onErrorResponse(VolleyError arg0) {
                      System.out.println("sorry,Error");
                  }
              });

      mRequestQueue.add(jsonObjectRequest);
  }
  private ArrayList<HashMap<String, String>> getData(JSONObject jsonObject)
  {
    int i = 1;
    ArrayList<HashMap<String, String>> hashMaps;
    HashMap<String, String> map = null;
    hashMaps = new ArrayList<HashMap<String, String>>();
    Log.i("jsonString", jsonObject.toString());
    while (jsonObject.has("item" + String.valueOf(i)))
    {
      Log.i("ganmaqu", "Now item is " + String.valueOf(i));
      try {
        String str = jsonObject.getString("item" + String.valueOf(i));
        map = new HashMap<String, String>();
        map.put("city", str);
        hashMaps.add(map);
      } catch (JSONException e) {
        e.printStackTrace();
      }
      i++;
    }
    Log.i("ganmaqu", "Finish item is " + String.valueOf(i));
    return hashMaps;
  }
    private void setCity(String city)
    {
        Log.i("ganmaqu", "origin city : " + userInfo.getString("city", "西安市"));
        userInfo.edit().putString("city",city).commit();
        Log.i("ganmaqu", "new city : " + userInfo.getString("city", "西安市"));
        // 数据是使用Intent返回
        Intent intent = new Intent();
        // 把返回数据存入Intent
        intent.putExtra("city", city);
        // 设置返回数据
        SelectCityActivity.this.setResult(RESULT_OK, intent);
        // 关闭Activity
        SelectCityActivity.this.finish();
    }

}
