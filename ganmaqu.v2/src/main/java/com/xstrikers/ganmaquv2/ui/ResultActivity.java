package com.xstrikers.ganmaquv2.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import net.tsz.afinal.FinalDb;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by LB on 14-1-25.
 */
public class ResultActivity extends ActionBarActivity {
    private List<Place> places;
    private FinalDb db;
    private String placesJSON;
    private PlaceAdapter placeAdapter;
    private AnimateDismissAdapter animateDismissAdapter;

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
        places = gson.fromJson(placesJSON, new TypeToken<List<Place>>() {
        }.getType());
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
        } else {
        }
        return true;
    }

    private void saveRouteLocal() {
        Gson gson = new Gson();
        Route route = new Route();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd  hh:mm");// 设置日期格式
        route.setplacesJSON(gson.toJson(places));
        Log.i("ganmaqu", "SAVE GSON : " + route.getPlacesJSON());
        route.setUserId(0);
        route.setDate(df.format(new Date()));
        db.save(route);

    }

    private void openMapActivity() {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MapActivity.class);
        intent.putExtra("places", (Serializable) places);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }

    public class PlaceAdapter extends com.haarman.listviewanimations.ArrayAdapter {
        private List<Place> Places;
        private Context mContext;

        public PlaceAdapter(Context context, int resource, int textViewResourceId,
                            List<Place> transPlaces) {
            // super(context, resource, textViewResourceId, transPlaces);
            super(transPlaces);
            this.Places = transPlaces;
            this.mContext = context;
            Log.i(
                    "ganmaqu",
                    "In PlacesAdapter: " + String.valueOf(resource) + "  "
                            + String.valueOf(textViewResourceId)
                            + "  " + transPlaces.toString());
        }

        private class ViewHolder {
            TextView typeTextView, nameTextView, addrTextView, payTextView;
            Button deleteButton;
        }

        @Override
        public int getCount() {
            return places.size();
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            // View v = super.getView(position, convertView, parent);
            Log.i("ganmaqu", "NOW POSITION IS " + String.valueOf(position));
            View v = LayoutInflater.from(mContext).inflate(R.layout.listitem_result, parent, false);
            // Log.i("ganmaqu", Places.get(position).toString());
            if (v != convertView && v != null) {
                ViewHolder holder = new ViewHolder();
                holder.nameTextView = (TextView) v.findViewById(R.id.result_name);
                holder.typeTextView = (TextView) v.findViewById(R.id.result_type);
                holder.addrTextView = (TextView) v.findViewById(R.id.result_address);
                holder.payTextView = (TextView) v.findViewById(R.id.result_pay);
                holder.deleteButton = (Button) v.findViewById(R.id.result_delete);
                // holder.dragImageView = iv;
                v.setTag(holder);
            }
            // if (position<=getCount())
            // {
            ViewHolder holder = (ViewHolder) v.getTag();
            holder.nameTextView.setText(Places.get(position).getShopName());
            holder.typeTextView.setText(Places.get(position).getMainType());
            holder.addrTextView.setText(Places.get(position).getAddress());
            holder.payTextView.setText(String.valueOf(Places.get(position).getCost()));
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (places.size() > 1) {
                        animateDismissAdapter.animateDismiss(position);
                        places.remove(position);
                    } else {
                        Toast.makeText(ResultActivity.this, "这是最后一个地点了", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            // }
            return v;
        }

    }

}
