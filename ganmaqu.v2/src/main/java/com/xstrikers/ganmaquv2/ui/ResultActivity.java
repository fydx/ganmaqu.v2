package com.xstrikers.ganmaquv2.ui;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.haarman.listviewanimations.itemmanipulation.AnimateDismissAdapter;
import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.haarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.haarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;
import com.xstrikers.ganmaquv2.R;
import com.xstrikers.ganmaquv2.model.Place;
import com.xstrikers.ganmaquv2.model.Route;
import com.xstrikers.ganmaquv2.ui.adapter.PlaceAdapter;


/**
 * Created by LB on 14-1-25.
 */
public class ResultActivity extends ActionBarActivity {
  private List<Place> places;
  private FinalDb db;
  private String placesJSON;
  private PlaceAdapter placeAdapter;
  public static AnimateDismissAdapter animateDismissAdapter;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_result);
    db = FinalDb.create(this);

    /*
     * action bar 设置
     */
    ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP,
        ActionBar.DISPLAY_HOME_AS_UP);
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
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
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    placesJSON = getIntent().getStringExtra("result");
    // places = DecodeUtil.decodeJson(placesJSON);
    places = gson.fromJson(placesJSON, new TypeToken<List<Place>>() {}.getType());
    Log.i("ganmaqu", "Place Num : " + String.valueOf(places.size()));
    Log.i("ganmaqu", "In Result Activity : " + places.toString());

    ListView listView = (ListView) findViewById(R.id.listView_result);
    placeAdapter =
        new PlaceAdapter(ResultActivity.this, R.layout.listitem_result, R.id.result_name, places);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ResultActivity.this, DetailActivity.class);
        intent.putExtra("name", places.get(position).getShopName());
        intent.putExtra("addr", places.get(position).getAddress());
        intent.putExtra("cost", String.valueOf(places.get(position).getCost()));
        intent.putExtra("info", places.get(position).getInfo());
        intent.putExtra("tel", places.get(position).getTelNumber());
        startActivity(intent);
      }
    });
    AnimationAdapter animAdapter = new AlphaInAnimationAdapter(placeAdapter);
    animateDismissAdapter = new AnimateDismissAdapter(animAdapter, new OnDismissCallback() {
      @Override
      public void onDismiss(AbsListView absListView, int[] ints) {
        for (int position : ints) {
          placeAdapter.remove(position);
        }
      }
    });
    animateDismissAdapter.setAbsListView(listView);

    listView.setAdapter(animateDismissAdapter);
    // animateDismissAdapter.animateDismiss(1);

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    this.getMenuInflater().inflate(R.menu.result, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int i1 = item.getItemId();
    if (i1 == R.id.change) {

    } else if (i1 == R.id.save) {
      saveRouteLocal();
    } else if (i1 == R.id.map) {
      openMapActivity();
    } else {}
    return true;
  }

  private void saveRouteLocal()
  {
    Route route = new Route();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd  hh:mm");// 设置日期格式
    route.setplacesJSON(placesJSON);
    route.setUserId(0);
    route.setDate(df.format(new Date()));
    db.save(route);

  }

  private void openMapActivity()
  {
    Intent intent = new Intent();
    intent.setClass(getApplicationContext(), MapActivity.class);
    intent.putExtra("places", (Serializable) places);
    startActivity(intent);
    overridePendingTransition(android.R.anim.fade_in,
        android.R.anim.fade_out);
  }
//  public void deleteItem(int position)
//  {
//      animateDismissAdapter.animateDismiss(position);
//  }

}
