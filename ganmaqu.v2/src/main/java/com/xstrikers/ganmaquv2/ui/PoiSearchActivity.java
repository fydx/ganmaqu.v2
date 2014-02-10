package com.xstrikers.ganmaquv2.ui;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionInfo;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.xstrikers.ganmaquv2.R;

public class PoiSearchActivity extends ActionBarActivity {

	private MapView mMapView = null;
	private MKSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private PopupOverlay pop = null;
	private Button button;
	private ArrayList<OverlayItem> mItems = null;
	private ArrayList<MKPoiInfo> mkPoiInfos = null;
	private MapView.LayoutParams layoutParam = null;
	private OverlayItem mCurItem = null;
	private MyOverlay mOverlay = null;
	private View popView = null;
	private ganmaquApplication ganmaquApplication;
	private EditText editSearchKey;
	private String areaString;
	private TextView popupTextView;
	/**
	 * 搜索关键字输入窗口
	 */
	private AutoCompleteTextView keyWorldsView = null;
	private ArrayAdapter<String> sugAdapter = null;
	private int load_Index;
	private String city;
	private SharedPreferences userInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR); // 加入 Actionbar
		ganmaquApplication app = (ganmaquApplication) this.getApplication();
		userInfo = getApplicationContext().getSharedPreferences("userInfo", 0);

		city = userInfo.getString("city", "西安市");

		/*
		 * actionbar 设置
		 */
		ActionBar actionBar = this.getSupportActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP,
				ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.main_actionbar_bg));
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle("自定义商圈");
		int titleId = Resources.getSystem().getIdentifier("action_bar_title",
				"id", "android");
		TextView title = (TextView) findViewById(titleId);
		title.setTextColor(Color.parseColor("#FFFFFF"));
		actionBar.setDisplayHomeAsUpEnabled(true);
		//city = getIntent().getStringExtra("city");
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(this);
			app.mBMapManager.init(ganmaquApplication.strKey,
					new ganmaquApplication.MyGeneralListener());
		}
		setContentView(R.layout.activity_poisearch);
		ganmaquApplication = (ganmaquApplication) getApplication();
		GeoPoint p = new GeoPoint((int) (34.265733 * 1E6),
				(int) (108.953906 * 1E6));
		// GeoPoint p = new GeoPoint((int) (36.065159 * 1E6),
		// (int) (103.833222 * 1E6));
		/**
		 * 创建自定义overlay
		 */
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.getController().enableClick(true);
		mMapView.getController().setZoom(12);
		mMapView.getController().setCenter(p);
		// 初始化搜索模块，注册搜索事件监听
		mSearch = new MKSearch();
		mOverlay = new MyOverlay(getResources().getDrawable(R.drawable.mark),
				mMapView);
		popView = getLayoutInflater().inflate(R.layout.popup_mycircle, null);
		popupTextView = (TextView)popView.findViewById(R.id.textView_mycircle);
		
		
		mSearch.init(app.mBMapManager, new MKSearchListener() {
			// 在此处理详情页结果
			@Override
			public void onGetPoiDetailSearchResult(int type, int error) {
				if (error != 0) {
					Toast.makeText(PoiSearchActivity.this, "抱歉，未找到结果",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(PoiSearchActivity.this, "成功，查看详情页面",
							Toast.LENGTH_SHORT).show();
				}
			}

			/**
			 * 在此处理poi搜索结果
			 */
			public void onGetPoiResult(MKPoiResult res, int type, int error) {
				// 错误号可参考MKEvent中的定义
				if (error != 0 || res == null) {
					Toast.makeText(PoiSearchActivity.this, "抱歉，未找到结果",
							Toast.LENGTH_LONG).show();
					return;
				}
				// 将地图移动到第一个POI中心点
				if (res.getCurrentNumPois() > 0) {
					// 将poi结果显示到地图上
					// MyPoiOverlay poiOverlay = new MyPoiOverlay(
					// PoiSearchActivity.this, mMapView, mSearch);
					mOverlay.getAllItem().clear();
					mkPoiInfos = new ArrayList<MKPoiInfo>();
					// poiOverlay.setData(res.getAllPoi());
					mkPoiInfos = res.getAllPoi();
					for (int i = 0; i < mkPoiInfos.size(); i++) {
						GeoPoint location = mkPoiInfos.get(i).pt;
						OverlayItem item = new OverlayItem(location,
								"location", "");
						item.setMarker(getResources().getDrawable(
								R.drawable.mark));
						mOverlay.addItem(item);

					}
					/**
					 * 保存所有item，以便overlay在reset后重新添加
					 */
					mItems = new ArrayList<OverlayItem>();
					mItems.addAll(mOverlay.getAllItem());
					/**
					 * 清空所有图层
					 */
					mMapView.getOverlays().clear();
					/**
					 * 将overlay 添加至MapView中
					 */
					mMapView.getOverlays().add(mOverlay);
					/**
					 * 刷新地图
					 */

					// mMapView.getOverlays().add(poiOverlay);
					mMapView.refresh();
					// 当ePoiType为2（公交线路）或4（地铁线路）时， poi坐标为空
					for (MKPoiInfo info : res.getAllPoi()) {
						if (info.pt != null) {
							mMapView.getController().animateTo(info.pt);
							break;
						}
					}
				} else if (res.getCityListNum() > 0) {
					// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
					String strInfo = "在";
					for (int i = 0; i < res.getCityListNum(); i++) {
						strInfo += res.getCityListInfo(i).city;
						strInfo += ",";
					}
					strInfo += "找到结果";
					Toast.makeText(PoiSearchActivity.this, strInfo,
							Toast.LENGTH_LONG).show();
				}
			}

			public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
			}

			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
			}

			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
			}

			public void onGetAddrResult(MKAddrInfo res, int error) {
			}

			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			}

			/**
			 * 更新建议列表
			 */
			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
				if (res == null || res.getAllSuggestions() == null) {
					return;
				}
				sugAdapter.clear();
				for (MKSuggestionInfo info : res.getAllSuggestions()) {
					if (info.key != null)
						sugAdapter.add(info.key);
				}
				sugAdapter.notifyDataSetChanged();

			}

			@Override
			public void onGetShareUrlResult(MKShareUrlResult result, int type,
					int error) {
				// TODO Auto-generated method stub

			}
		});

		keyWorldsView = (AutoCompleteTextView) findViewById(R.id.searchkey);
		sugAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line);
		keyWorldsView.setAdapter(sugAdapter);

		/**
		 * 当输入关键字变化时，动态更新建议列表
		 */
		keyWorldsView.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (cs.length() <= 0) {
					return;
				}

				/**
				 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
				 */
				mSearch.suggestionSearch(cs.toString(), city);
			}
		});
		/**
		 * 创建一个popupoverlay
		 */
		PopupClickListener popListener = new PopupClickListener() {
			@Override
			public void onClickedPopup(int index) {

				finish();
			}
		};
		pop = new PopupOverlay(mMapView, popListener);
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mMapView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	private void initMapView() {
		mMapView.setLongClickable(true);
		mMapView.getController().setZoom(14);
		mMapView.getController().enableClick(true);
		mMapView.setBuiltInZoomControls(true);
	}

	/**
	 * 影响搜索按钮点击事件
	 * 
	 * @param v
	 */
	public void searchButtonProcess(View v) {

		editSearchKey = (EditText) findViewById(R.id.searchkey);
		areaString = editSearchKey.getText().toString();
		mSearch.poiSearchInCity(city, editSearchKey.getText().toString());
	}

	public void goToNextPage(View v) {
		// 搜索下一组poi
		int flag = mSearch.goToPoiPage(++load_Index);
		if (flag != 0) {
			Toast.makeText(PoiSearchActivity.this, "先搜索开始，然后再搜索下一组数据",
					Toast.LENGTH_SHORT).show();
		}
	}

	public class MyOverlay extends ItemizedOverlay {

		public MyOverlay(Drawable defaultMarker, MapView mapView) {
			super(defaultMarker, mapView);
		}

		@Override
		public boolean onTap(final int index) {

			popupTextView.setText(mkPoiInfos.get(index).name);
			popView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					ganmaquApplication.myCircle = true;
					ganmaquApplication.circle_lat = mkPoiInfos.get(index).pt
							.getLatitudeE6() / 1E6;
					ganmaquApplication.circle_lng = mkPoiInfos.get(index).pt
							.getLongitudeE6() / 1E6;
					intent.putExtra("circle", areaString);
					setResult(2, intent);
					finish();
				}
			});
			GeoPoint pt = mkPoiInfos.get(index).pt;
			// 创建布局参数
			layoutParam = new MapView.LayoutParams(
			// 控件宽,继承自ViewGroup.LayoutParams
					MapView.LayoutParams.WRAP_CONTENT,
					// 控件高,继承自ViewGroup.LayoutParams
					MapView.LayoutParams.WRAP_CONTENT,
					// 使控件固定在某个地理位置
					pt, 0, -32,
					// 控件对齐方式
					MapView.LayoutParams.BOTTOM_CENTER);
			// 添加View到MapView中
			mMapView.addView(popView, layoutParam);

			return true;

		}

		@Override
		public boolean onTap(GeoPoint pt, MapView mMapView) {
			if (pop != null) {
				pop.hidePop();
				mMapView.removeView(popView);
			}
			return false;
		}

	}

}
