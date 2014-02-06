package com.xstrikers.ganmaquv2.ui.fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xstrikers.ganmaquv2.R;
import com.xstrikers.ganmaquv2.map.LocationManagerHelper;
import com.xstrikers.ganmaquv2.ui.ResultActivity;
import com.xstrikers.ganmaquv2.ui.dialog.CircleDialog;

/**
 * Created by LB on 14-1-18.
 */
public class MainFragment extends android.support.v4.app.Fragment {
  private SharedPreferences userInfo;
  private Button circleButton;
  private RadioButton familyRadioButton, friendsRadioButton, coupleRadioButton;
  private String selectType;
  private final String types[] = new String[] {"亲子出行", "朋友出行", "情侣出行"};
  private Boolean full = true;
  private Dialog dialogTrans;
  private LocationManagerHelper locationManagerHelper;


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_main, container, false);
    userInfo = getActivity().getSharedPreferences("userInfo", 0);
    circleButton = (Button) rootView.findViewById(R.id.main_button_circle);
    locationManagerHelper = LocationManagerHelper.getInstance();

    circleButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        CircleDialog circleDialog =
            new CircleDialog(getActivity(), userInfo.getString("city", "西安市"), getActivity());
        circleDialog.setbutton(circleButton);
        circleDialog.show();

      }
    });
    selectType = types[0];
    RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.main_radioGroup);

    familyRadioButton = (RadioButton) rootView.findViewById(R.id.radioButton_family);
    friendsRadioButton = (RadioButton) rootView.findViewById(R.id.radioButton_friend);
    coupleRadioButton = (RadioButton) rootView.findViewById(R.id.radioButton_couple);
    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == familyRadioButton.getId())
          selectType = types[0];
        else if (checkedId == friendsRadioButton.getId())
          selectType = types[1];
        else if (checkedId == coupleRadioButton.getId())
          selectType = types[2];

      }


    });
    showCircle();
    Button getRouteButton = (Button) rootView.findViewById(R.id.button_getRoute);
    getRouteButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        enterRouteActivity();
          dialogTrans = new Dialog(getActivity(),
                  R.style.activity_translucent);
          dialogTrans.setContentView(R.layout.dialog_connect);
          dialogTrans.show();
        // Log.i("ganmaqu","Location : " +locationManagerHelper.getLocation());
      }
    });
    return rootView;
  }

  private void enterRouteActivity()
  {
    final AsyncHttpClient asyncHttpClient, circleAsyncHttpClient;

    asyncHttpClient = new AsyncHttpClient();
    circleAsyncHttpClient = new AsyncHttpClient();

    final RequestParams params = new RequestParams();
    if (full == true)
    {
      params.put("command", "full");
      params.put("type", selectType);
    }
    else
    {
      params.put("command", "part");
    }
    // params.put("pos_x", String.valueOf(locationManagerHelper.getLng()));
    // params.put("pos_y", String.valueOf(locationManagerHelper.getLat()));
    JSONObject json = new JSONObject();
    JSONArray item = new JSONArray();
    try {
      json.put("item", item);
    } catch (JSONException e)
    {

    }
    try {
      params.put("json", json.getString("item"));
    } catch (JSONException e)
    {

    }

    params.put("id", "root");
    Log.i("ganmaqu", "POST Params :  " + params.toString());
    circleAsyncHttpClient.get("http://" + getActivity().getResources().getString(R.string.Hostname)
        + ":8080/?command=circlepos&city=" + userInfo.getString("city", "西安市") + "&circleName="
        + circleButton.getText().toString(), new AsyncHttpResponseHandler()
    {
      @Override
      public void onSuccess(String response) {
        try {
          Log.i("ganmaqu", "Circle Position : " + response);
          JSONObject jsonObject = new JSONObject(response);
          params.put("pos_x", jsonObject.getString("lng"));
          params.put("pos_y", jsonObject.getString("lat"));
        }
        catch (JSONException e)
        {

        }
        asyncHttpClient.post("http://" + getActivity().getResources().getString(R.string.Hostname)
            + ":8080/", params, new AsyncHttpResponseHandler() {
          @Override
          public void onSuccess(String response) {
            Log.i("ganmaqu", "Result : " + response);
              dialogTrans.dismiss();
              Intent intent = new Intent(getActivity(), ResultActivity.class);
              intent.putExtra("result",response);
              startActivity(intent);
              getActivity().overridePendingTransition(android.R.anim.fade_in,
                      android.R.anim.fade_out);
          }
        });
      }
    });


  }

  public void showCircle()
  {

    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    asyncHttpClient.get("http://" + getActivity().getResources().getString(R.string.Hostname)
    + ":8080/?command=getshopcircle&city=" + userInfo.getString("city", "西安市") + "&pos_x=" + locationManagerHelper.getLng()
            + "&pos_y=" + locationManagerHelper.getLat(), new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(String response)
        {
            circleButton.setText(response);
        }
    }
    );
}
}
