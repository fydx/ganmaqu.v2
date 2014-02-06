package com.xstrikers.ganmaquv2.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.xstrikers.ganmaquv2.ui.fragment.MainFragment;
import com.xstrikers.ganmaquv2.ui.fragment.NavigationDrawerFragment;

public class MainActivity extends android.support.v7.app.ActionBarActivity
    implements NavigationDrawerFragment.NavigationDrawerCallbacks {
  /**
   * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
   */
  private NavigationDrawerFragment mNavigationDrawerFragment;

  /**
   * Used to store the last screen title. For use in {@link #restoreActionBar()}.
   */
  private CharSequence mTitle;
  private LocationManagerHelper locationManagerHelper;
  private RequestQueue mRequestQueue;
  private StringBuilder geocodingURL;
  private SharedPreferences userInfo;
  private TextView  typeTextView;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ActionBar actionbar = getSupportActionBar();
    actionbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.main_actionbar_bg));
    int actionBarTitleId =
        getResources().getSystem().getIdentifier("action_bar_title", "id", "android");
    if (actionBarTitleId > 0) {
      TextView title = (TextView) findViewById(actionBarTitleId);
      if (title != null) {
        title.setTextColor(Color.WHITE);
      }
    }
    userInfo = this.getSharedPreferences("userInfo", 0);
    mNavigationDrawerFragment = (NavigationDrawerFragment)
        getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
    mTitle = getTitle();
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction()
        .replace(R.id.container, new MainFragment())
        .commit();
    // Set up the drawer.
    mNavigationDrawerFragment.setUp(
        R.id.navigation_drawer,
        (DrawerLayout) findViewById(R.id.drawer_layout));
    locationManagerHelper = LocationManagerHelper.getInstance();
    locationManagerHelper.setLocationManager((LocationManager) this
        .getSystemService(Context.LOCATION_SERVICE));
    locationManagerHelper.start();
    mRequestQueue = Volley.newRequestQueue(this);


    // startActivity(new Intent(this, SelectCityActivity.class));
    // if (userInfo.getBoolean("firstBoot", true) == true)
    // {
    // if (locationManagerHelper.getLocation() == null)
    // startActivity(new Intent(this, SelectCityActivity.class));
    //
    // // getLocationCity();
    // }
  }

  public void getLocationCity()
  {
    geocodingURL =
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
              String city =
                  (new JSONObject(new JSONObject(response.getString("result"))
                      .getString("addressComponent"))).getString("city");
              Log.i("ganmaqu", city);
              userInfo.edit().putBoolean("firstBoot", false).commit();
              userInfo.edit().putString("city", city).commit();
              Toast.makeText(getApplicationContext(), "检测您在" + city + "\n已设置为当前默认城市",
                  Toast.LENGTH_LONG).show();
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

  @Override
  public void onNavigationDrawerItemSelected(int position) {
    // update the main content by replacing fragments

    Toast.makeText(this, String.valueOf(position), 1000);
  }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
        }
  }

  public void restoreActionBar() {
    ActionBar actionBar = getSupportActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    actionBar.setDisplayShowTitleEnabled(true);
    actionBar.setTitle(mTitle);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    if (!mNavigationDrawerFragment.isDrawerOpen()) {
      // Only show items in the action bar relevant to this screen
      // if the drawer is not showing. Otherwise, let the drawer
      // decide what to show in the action bar.
      getMenuInflater().inflate(R.menu.main, menu);
      restoreActionBar();
      return true;
    }
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
          // Handle action bar item clicks here. The action bar will
          // automatically handle clicks on the Home/Up button, so long
          // as you specify a parent activity in AndroidManifest.xml.
          switch (item.getItemId()) {
              case R.id.action_settings:
                  return true;
          }
    return super.onOptionsItemSelected(item);
  }

  /**
   * A placeholder fragment containing a simple view.
   */
  public static class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
      PlaceholderFragment fragment = new PlaceholderFragment();
      Bundle args = new Bundle();
      args.putInt(ARG_SECTION_NUMBER, sectionNumber);
      fragment.setArguments(args);
      return fragment;
    }

    public PlaceholderFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.fragment_main, container, false);
      return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
      super.onAttach(activity);
      ((MainActivity) activity).onSectionAttached(
          getArguments().getInt(ARG_SECTION_NUMBER));
    }
  }

  /**
   * 为了得到传回的数据，必须在前面的Activity中（指MainActivity类）重写onActivityResult方法
   * 
   * requestCode 请求码，即调用startActivityForResult()传递过去的值
   * resultCode 结果码，结果码用于标识返回数据来自哪个新Activity
   */
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    String result = data.getStringExtra("city");// 得到新Activity 关闭后返回的数据
    Log.i("ganmaqu", result);
    mNavigationDrawerFragment.setCityText(result);
    MainFragment mainFragment =   (MainFragment)
              getSupportFragmentManager().findFragmentById(R.id.container);
    mainFragment.showCircle();
  }


}
