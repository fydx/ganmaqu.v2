package com.xstrikers.ganmaquv2.ui.dialog;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.xstrikers.ganmaquv2.R;
import com.xstrikers.ganmaquv2.net.Connect;

/**
 * Created by LB on 14-1-22.
 */
public class CircleDialog extends Dialog {
  private Activity activity;
  private GridView gridView;
  private String city;
  private Button button;
  private RequestQueue mRequestQueue;
  private GridAdapter gridAdapter;

  // private Context mContext;


  public CircleDialog(Context context, String cityString,
      Activity activity_main) {
    super(context, R.style.CircleDialog);
    this.city = cityString;
    activity = activity_main;
    // this.mContext = context;
    setCustomDialog();

  }

  private void setCustomDialog()
  {
    final View mView = LayoutInflater.from(getContext()).inflate(
        R.layout.dialog_main_circle, null);
    gridView = (GridView) mView
        .findViewById(R.id.gridView_circles);
    mRequestQueue = Volley.newRequestQueue(activity);
    gridAdapter = new GridAdapter(this.getContext());
    // getCityCircles(city);
    // new getCircles().execute(city);
    // AsyncHttpClient方式
    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    StringBuilder getCircles =
        new StringBuilder("http://").append(activity.getResources().getString(R.string.Hostname))
            .append(":8080/?command=getcirclelist&city=").append(city);
    asyncHttpClient.get(getCircles.toString(), new AsyncHttpResponseHandler()
    {
                @Override
                public void onSuccess(String response) {
                try {
                    gridAdapter.setHashMap(getData(new JSONObject(response)));
                    gridView.setAdapter(gridAdapter);
                }
                catch (JSONException e)
                {

        }

        // System.out.println(response);
      }
    });
    super.setContentView(mView);
  }


  public void setbutton(Button button_trans) {
    this.button = button_trans;
  }

  public class GridAdapter extends BaseAdapter {

    private holder holder;

    private class holder {
      Button button;
    }

    private HashMap<Integer, String> circlesInfo;

    private Context context;
    private LayoutInflater mInflater;

    public GridAdapter(Context c) {
      super();

      this.context = c;
      mInflater = (LayoutInflater) context
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setHashMap(HashMap<Integer, String> circles) {
      this.circlesInfo = circles;
    }

    @Override
    public int getCount() {
      Log.i("ganmaqu", "circle size = " + String.valueOf(circlesInfo.size()));
      return circlesInfo.size();
    }

    @Override
    public Object getItem(int index) {

      return circlesInfo.get(index);
    }

    @Override
    public long getItemId(int position) {

      return position;
    }

    @Override
    public View getView(final int position, View convertView,
        ViewGroup parent) {


      if (convertView == null) {
        convertView = mInflater.inflate(R.layout.griditem_dialog_circle,
            null);

        // button = (Button) convertView
        // .findViewById(R.id.button_circle);
        holder = new holder();
        holder.button = (Button) convertView
            .findViewById(R.id.button_circle);
        convertView.setTag(holder);
        Log.i("convertview", "null");

      } else {
        holder = (holder) convertView.getTag();
      }

      holder.button.setText(circlesInfo.get(position));
      Log.i("circles info", circlesInfo.toString());
      holder.button.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
          button.setText(circlesInfo.get(position));
          CircleDialog.this.dismiss();

        }
      });

      return convertView;
    }

  }

  /**
   * Volley 方式
   * 
   * @param city
   */
  private void getCityCircles(String city)
  {
    StringBuilder getCircles =
        new StringBuilder("http://").append(activity.getResources().getString(R.string.Hostname))
            .append(":8080/?command=getcirclelist&city=").append(city);
    // getCircles = new
    // StringBuilder("http://115.28.17.121:8080/?command=getshopcircle&city=北京市&pos_x=0.0&pos_y=0.0");
    Log.i("ganmaqu", getCircles.toString());

    StringRequest jsonObjectRequest = null;

    jsonObjectRequest = new StringRequest(
        Request.Method.GET,
        getCircles.toString(),

        new Response.Listener<String>() {
          @Override
          public void onResponse(String jsonObject) {
            Log.i("ganmaqu", jsonObject.toString());
            // gridAdapter.setHashMap(getData(jsonObject));
            // gridView.setAdapter(gridAdapter);
          }


        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError volleyError) {

          }
        }
        );


    mRequestQueue.add(jsonObjectRequest);

  }

  /**
   * asynctask方式
   */
  public class getCircles extends AsyncTask<String, Integer, String>
  {

    @Override
    protected String doInBackground(String... params) {
      return (Connect.getInstance(activity.getResources().getString(R.string.Hostname))
          .GetCircleList(params[0]));
    }

    @Override
    public void onPostExecute(String result)
    {
      try {
        gridAdapter.setHashMap(getData(new JSONObject(result)));
        gridView.setAdapter(gridAdapter);
      } catch (JSONException e)
      {

      }


    }
  }

  private HashMap<Integer, String> getData(JSONObject jsonObject)
  {
    int i = 1;
    HashMap<Integer, String> map = new HashMap<Integer, String>();
    Log.i("jsonString", jsonObject.toString());
    while (jsonObject.has("item" + String.valueOf(i)))
    {
      Log.i("ganmaqu", "Now item is " + String.valueOf(i));
      try {
        map.put(i - 1, jsonObject.getString("item" + String.valueOf(i)));
      } catch (JSONException e) {
        e.printStackTrace();
      }
      i++;
    }
    Log.i("ganmaqu", "Finish item is " + String.valueOf(i));
    return map;
  }


}
