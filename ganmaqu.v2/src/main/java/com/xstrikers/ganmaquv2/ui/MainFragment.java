package com.xstrikers.ganmaquv2.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xstrikers.ganmaquv2.R;
import com.xstrikers.ganmaquv2.ui.dialog.CircleDialog;

/**
 * Created by LB on 14-1-18.
 */
public class MainFragment extends android.support.v4.app.Fragment {
  //private String city;
  private SharedPreferences userInfo;
  private Button circleButton;
  private final String types[] = new String[] { "亲子出行", "朋友出行", "情侣出行" };
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_main, container, false);
      userInfo = getActivity().getSharedPreferences("userInfo",0);
      circleButton = (Button)rootView.findViewById(R.id.main_button_circle);

      circleButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              CircleDialog circleDialog = new CircleDialog(getActivity(),userInfo.getString("city","西安市"),getActivity());
              circleDialog.setbutton(circleButton);
              circleDialog.show();

          }
      });
      Button typeButton = (Button)rootView.findViewById(R.id.textView_type);
      typeButton.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(View v) {


          }
      });
      return rootView;
  }


}
