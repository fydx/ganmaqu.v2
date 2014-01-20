package com.xstrikers.ganmaquv2.ui;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.xstrikers.ganmaquv2.R;

/**
 * Created by LB on 14-1-20.
 */
public class SelectCityActivity extends Activity {
  private RequestQueue requestQueue;
  private List<String> cities;
  private ListView listView;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_select_city);
    listView = (ListView) findViewById(R.id.listView_city);
    requestQueue = Volley.newRequestQueue(this);
    cities = new ArrayList<String>();
    StringBuilder url = new StringBuilder();
    url.append("http://" + getResources().getString(R.string.Hostname)).append(
        ":8080/?command=getavailablecity");
    Log.i("ganmaqu", url.toString());
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
        Request.Method.GET,
        url.toString(),
        null,
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject jsonObject) {
            int i = 1;
            Log.i("ganmaqu", "Start item is " + String.valueOf(i));

                  while (jsonObject.has("item" + String.valueOf(i)))
                  {
                      Log.i("ganmaqu", "Now item is " + String.valueOf(i));
                      try {
                          String str = jsonObject.getString("item" + String.valueOf(i));
                          cities.add(str);
                      } catch (JSONException e) {
                e.printStackTrace();
              }
              i++;
            }
            Log.i("ganmaqu", "Finish item is " + String.valueOf(i));
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext()
                , android.R.layout.simple_expandable_list_item_1, cities);
            listView.setAdapter(arrayAdapter);

          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError volleyError) {

          }
        }
        );
    requestQueue.add(jsonObjectRequest);
  }

  /**
   * 字符串编码转换的实现方法
   * 
   * @param str 待转换编码的字符串
   * @param oldCharset 原编码
   * @param newCharset 目标编码
   * @return
   * @throws UnsupportedEncodingException
   */
  public String changeCharset(String str, String oldCharset, String newCharset)
      throws UnsupportedEncodingException {
    if (str != null) {
      // 用旧的字符编码解码字符串。解码可能会出现异常。
      byte[] bs = str.getBytes(oldCharset);
      // 用新的字符编码生成字符串
      return new String(bs, newCharset);
    }
    return null;
  }

}
