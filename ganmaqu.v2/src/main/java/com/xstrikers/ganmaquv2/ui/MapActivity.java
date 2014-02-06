package com.xstrikers.ganmaquv2.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.map.TransitOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.xstrikers.ganmaquv2.R;
import com.xstrikers.ganmaquv2.map.LocationManagerHelper;
import com.xstrikers.ganmaquv2.model.Place;

public class MapActivity extends Activity {
	MapView mMapView = null; // 地图View
	// UI相关
	Button mBtnReverseGeoCode = null; // 将坐标反编码为地址
	Button mBtnGeoCode = null; // 将地址编码为坐标
	private Button preButton, nextButton;
	MKSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	ArrayList<MKPoiInfo> mkpoi = null;
	private OverlayTest itemOverlay = null;
	private List<Place> places;
	private GeoPoint locGeoPoint;
	private List<GeoPoint> geoPoints;
	private PopupWindow mPopupWindow ;
	private int searchType = -1;// 记录搜索的类型，区分驾车/步行和公交
	String tmp = null;
	RouteOverlay routeOverlay = null;
	// 定位相关
	LocationClient mLocClient;
	LocationData locData = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	// 定位图层
	MyLocationOverlay myLocationOverlay = null;
	// UI相关
	OnCheckedChangeListener radioButtonListener = null;
	private int countRoute = 0,isFindBus = 0;
	boolean isRequest = false;// 是否手动触发请求定位
	boolean isFirstLoc = true;// 是否首次定位
	private Button requestLocButton = null;
	// 公交路线相关
	TransitOverlay transitOverlay = null;// 保存公交路线图层数据的变量，供浏览节点时使用

	/**
	 * 用MapController完成地图控制
	 */
	private MapController mMapController = null;
	private MyOverlay mOverlay = null;
	private PopupOverlay pop = null;
	private ArrayList<OverlayItem> mItems = null;
	private TextView popupText = null;
	private View viewCache = null;
	private View popupInfo = null;
	private View popupLeft = null;
	private View popupRight = null;
	private Button button = null;
	private MapView.LayoutParams layoutParam = null;
	private OverlayItem mCurItem = null;
	private SharedPreferences userInfo ;
	private int mode ;
	private String city;
    private LocationManagerHelper locationManagerHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        locationManagerHelper = LocationManagerHelper.getInstance();
		ganmaquApplication app = (ganmaquApplication) this.getApplication();
		final int markers_id[] = { R.drawable.icon_1, R.drawable.icon_2,
				R.drawable.icon_3, R.drawable.icon_4, R.drawable.icon_5,R.drawable.icon_6 };

		// BMapManager 必须在setContentview前面初始化，否则报错
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(this.getApplicationContext());
			app.mBMapManager.init(ganmaquApplication.strKey,
					new ganmaquApplication.MyGeneralListener());
		}
		setContentView(R.layout.activity_map);
		//初始化sharedpreference
		userInfo = getApplicationContext().getSharedPreferences("userInfo", 0);
		mode = userInfo.getInt("mode", 0);
		city = userInfo.getString("city", "西安市");
		places = (List<Place>) getIntent().getSerializableExtra("places");
		Log.i("places nums", String.valueOf(places.size()));
		requestLocButton = (Button) findViewById(R.id.button_loc);
		requestLocButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				requestLocClick();
			}
		});
		preButton = (Button) findViewById(R.id.button_pre);
		preButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (countRoute < 0) {
					Toast.makeText(getApplicationContext(), "这是第一条路线",
							Toast.LENGTH_SHORT).show();
					countRoute = 0;
					if (routeOverlay != null) {
						mMapView.getOverlays().remove(routeOverlay);
						initBusLine();
					}
				} else {
					MKPlanNode start = new MKPlanNode();
					start.pt = geoPoints.get(countRoute);
					MKPlanNode end = new MKPlanNode();
					end.pt = geoPoints.get(countRoute + 1);
					mSearch.walkingSearch(null, start, null, end);
					countRoute--;
				}

			}
		});
		nextButton = (Button) findViewById(R.id.button_next);
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (transitOverlay != null) {
					mMapView.getOverlays().remove(transitOverlay);
					mMapView.refresh();
				}
				if (countRoute < 0) {
					countRoute = 0;
				}
				MKPlanNode start = new MKPlanNode();
				start.pt = geoPoints.get(countRoute);
				MKPlanNode end = new MKPlanNode();
				end.pt = geoPoints.get(countRoute + 1);
				mSearch.walkingSearch(null, start, null, end);
				countRoute++;
				if (countRoute > places.size()-2) {
					countRoute = places.size()-2;
					Toast.makeText(getApplicationContext(), "这是最后一条路线了",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		mkpoi = new ArrayList<MKPoiInfo>();
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapsView);
		/**
		 * 获取地图控制器
		 */
		mMapController = mMapView.getController();
		/**
		 * 设置地图是否响应点击事件 .
		 */
		mMapController.enableClick(true);
		/**
		 * 设置地图缩放级别
		 */
		mMapController.setZoom(14);
		/**
		 * 显示内置缩放控件
		 */
		//mMapView.setBuiltInZoomControls(true);

		/**
		 * 设定地图中心点 （西安钟楼）
		 */
		GeoPoint p = new GeoPoint((int) (34.265733 * 1E6),
				(int) (108.953906 * 1E6));
//		GeoPoint p = new GeoPoint((int) (36.065159 * 1E6),
//				(int) (103.833222 * 1E6));
		mMapController.setCenter(p);

		// 定位初始化
		initLoc();
		// 定位部分结束
		// setLocation(103.833222,36.065159);

		// 封装地点坐标到list中
		geoPoints = null;
		geoPoints = new ArrayList<GeoPoint>();
		for (int i = 0; i < places.size(); i++) {
			geoPoints.add(new GeoPoint((int) (places.get(i).getLat() * 1E6),
					(int) (places.get(i).getLng() * 1E6)));
			Log.i("places info in GeoPoint", places.get(i).getShopName());
		}
		List<Drawable> markers = null;
		markers = new ArrayList<Drawable>();
		for (int i = 0; i < places.size(); i++) {
			markers.add(getResources().getDrawable(markers_id[i]));
		}
		List<OverlayItem> overlayItems = null;
		overlayItems = new ArrayList<OverlayItem>();
		for (int i = 0; i < places.size(); i++) {
			OverlayItem temp_overlayItem = new OverlayItem(geoPoints.get(i),
					places.get(i).getShopName(), places.get(i).getShopName());
			temp_overlayItem.setMarker(markers.get(i));
			overlayItems.add(temp_overlayItem);
		}
		Log.i("overlay items ", String.valueOf(overlayItems.size()));

		// 创建IteminizedOverlay
		itemOverlay = new OverlayTest(markers.get(0), mMapView);

		// 将IteminizedOverlay添加到MapView中
		itemOverlay.addItem(overlayItems);

		// clearOverlay(mMapView);
		// mMapView.getOverlays().clear();
		// mMapView.refresh();
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mMapView.getOverlays().add(itemOverlay);
				mMapView.refresh();

			}
		};
		timer.schedule(timerTask, 100);
		// 初始化搜索模块，注册事件监听
		mSearch = new MKSearch();
		mSearch.init(app.mBMapManager, new MKSearchListener() {

			@Override
			public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGetBusDetailResult(MKBusLineResult busResult, int code) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGetDrivingRouteResult(MKDrivingRouteResult arg0,
					int arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onGetPoiDetailSearchResult(int arg0, int arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1,
					int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
				// 起点或终点有歧义，需要选择具体的城市列表或地址列表
				if (error == MKEvent.ERROR_ROUTE_ADDR) {
					// 遍历所有地址
					// ArrayList<MKPoiInfo> stPois =
					// res.getAddrResult().mStartPoiList;
					// ArrayList<MKPoiInfo> enPois =
					// res.getAddrResult().mEndPoiList;
					// ArrayList<MKCityListInfo> stCities =
					// res.getAddrResult().mStartCityList;
					// ArrayList<MKCityListInfo> enCities =
					// res.getAddrResult().mEndCityList;
					return;
				}
				if (error != 0 || res == null) {
					// Toast.makeText(NewMapActivity.this, "抱歉，未找到结果",
					// Toast.LENGTH_SHORT).show();
					return;
				}

				searchType = 1;
				transitOverlay = new TransitOverlay(MapActivity.this,
						mMapView);
				// 此处仅展示一个方案作为示例
				transitOverlay.setData(res.getPlan(0));
				// 清除其他图层
				// mMapView.getOverlays().clear();
				// 添加路线图层
				mMapView.getOverlays().add(transitOverlay);
				// 执行刷新使生效
				mMapView.refresh();
				// 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
				mMapView.getController().zoomToSpan(
						transitOverlay.getLatSpanE6(),
						transitOverlay.getLonSpanE6());
				// 移动地图到起点
				mMapView.getController().animateTo(res.getStart().pt);
				// 重置路线节点索引，节点浏览时使用
				// nodeIndex = 0;
				int num_line = res.getPlan(0).getNumLines() - 1;
				Log.i("fydx",
						"需要倒"
								+ String.valueOf(res.getPlan(0).getNumLines() - 1)
								+ "次车");

				if (num_line == 0) {
					tmp = "从 " + res.getPlan(0).getLine(0).getGetOnStop().name
							+ " " + res.getPlan(0).getLine(0).getTip();
				} else {
					tmp = "从" + res.getPlan(0).getLine(0).getGetOnStop().name
							+ res.getPlan(0).getLine(0).getTip() + "\n再"
							+ res.getPlan(0).getLine(1).getTip();
				}
//				Toast.makeText(getApplicationContext(), tmp, Toast.LENGTH_SHORT)
//						.show();
				showRoutePopUp();
			}

			@Override
			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
				// TODO Auto-generated method stub
				if (mPopupWindow!=null&&mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				}
				if (error == MKEvent.ERROR_ROUTE_ADDR) {
					// 遍历所有地址
					// ArrayList<MKPoiInfo> stPois =
					// res.getAddrResult().mStartPoiList;
					// ArrayList<MKPoiInfo> enPois =
					// res.getAddrResult().mEndPoiList;
					// ArrayList<MKCityListInfo> stCities =
					// res.getAddrResult().mStartCityList;
					// ArrayList<MKCityListInfo> enCities =
					// res.getAddrResult().mEndCityList;
					return;
				}
				if (error != 0 || res == null) {
					// Toast.makeText(NewMapActivity.this, "抱歉，未找到结果",
					// Toast.LENGTH_SHORT).show();
					return;
				}
				// if (transitOverlay!= null) {
				// mMapView.getOverlays().remove(transitOverlay);
				// mMapView.refresh();
				// }
				if (routeOverlay != null) {
					mMapView.getOverlays().remove(routeOverlay);
					mMapView.refresh();
				}
				searchType = 1;
				routeOverlay = new RouteOverlay(MapActivity.this, mMapView);

				// 此处仅展示一个方案作为示例
				routeOverlay.setData(res.getPlan(0).getRoute(0));
				routeOverlay.setEnMarker(getResources().getDrawable(
						R.drawable.empty));
				routeOverlay.setStMarker(getResources().getDrawable(
						R.drawable.empty));
				// 清除其他图层
				// mMapView.getOverlays().clear();
				// 添加路线图层
				mMapView.getOverlays().add(routeOverlay);
				// 执行刷新使生效
				mMapView.refresh();
				// 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
				mMapView.getController().zoomToSpan(
						routeOverlay.getLatSpanE6(),
						routeOverlay.getLonSpanE6());
				// 移动地图到起点
				mMapView.getController().animateTo(res.getStart().pt);

			}

		});

	}

	class OverlayTest extends ItemizedOverlay<OverlayItem> {
		// 用MapView构造ItemizedOverlay
		public OverlayTest(Drawable mark, MapView mapView) {
			super(mark, mapView);
		}

		protected boolean onTap(int index) {
			// 在此处理item点击事件
			System.out.println("item onTap: " + index);
			Toast.makeText(getApplicationContext(),
					places.get(index).getShopName(), Toast.LENGTH_LONG).show();
			return true;
		}

		public boolean onTap(GeoPoint pt, MapView mapView) {
			// 在此处理MapView的点击事件，当返回 true时
			super.onTap(pt, mapView);
			return false;
		}
	}

	public class MyOverlay extends ItemizedOverlay {

		public MyOverlay(Drawable defaultMarker, MapView mapView) {
			super(defaultMarker, mapView);
		}

		@Override
		public boolean onTap(int index) {
			OverlayItem item = getItem(index);
			mCurItem = item;

			return true;
		}

		@Override
		public boolean onTap(GeoPoint pt, MapView mMapView) {
			if (pop != null) {
				pop.hidePop();
				mMapView.removeView(button);
			}
			return false;
		}

	}

	/**
	 * 清除所有Overlay
	 * 
	 * @param view
	 */
	public void clearOverlay(View view) {

		mOverlay.removeAll();

		if (pop != null) {
			pop.hidePop();
		}
		// mMapView.removeView(button);
		mMapView.refresh();
	}

	/**
	 * 重新添加Overlay
	 * 
	 * @param view
	 */
	public void resetOverlay(View view) {
		clearOverlay(null);
		// 重新add overlay
		mOverlay.addItem(mItems);
		mMapView.refresh();
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
		// 退出时销毁定位
		if (mLocClient != null)
			mLocClient.stop();

		ganmaquApplication app = (ganmaquApplication) this.getApplication();
		if (app.mBMapManager != null) {
			app.mBMapManager.destroy();
			app.mBMapManager = null;
		}
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

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;

			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			// 如果不显示定位精度圈，将accuracy赋值为0即可
			locData.accuracy = location.getRadius();
			locData.direction = location.getDerect();
			// 更新定位数据
			myLocationOverlay.setData(locData);
			// 更新图层数据执行刷新后生效
			mMapView.refresh();
			// 是手动触发请求或首次定位时，移动到定位点
			if (isRequest || isFirstLoc) {
				// 移动地图到定位点
				locGeoPoint = new GeoPoint((int) (locData.latitude * 1e6),
						(int) (locData.longitude * 1e6));
				mMapController.animateTo(locGeoPoint);
				initBusLine();

				isRequest = false;
			}
			// 首次定位完成
			isFirstLoc = false;

		}

		public void onReceivePoi(BDLocation poiLocation) {
			// if (poiLocation == null){
			// return ;
			// }
			// StringBuffer sb = new StringBuffer(256);
			// sb.append("Poi time : ");
			// sb.append(poiLocation.getTime());
			// sb.append("\nerror code : ");
			// sb.append(poiLocation.getLocType());
			// sb.append("\nlatitude : ");
			// sb.append(poiLocation.getLatitude());
			// sb.append("\nlontitude : ");
			// sb.append(poiLocation.getLongitude());
			// sb.append("\nradius : ");
			// sb.append(poiLocation.getRadius());
			// if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
			// sb.append("\naddr : ");
			// sb.append(poiLocation.getAddrStr());
			// }
			// if(poiLocation.hasPoi()){
			// sb.append("\nPoi:");
			// sb.append(poiLocation.getPoi());
			// }else{
			// sb.append("noPoi information");
			// }
			// Log.i("location msg", sb.toString());
		}
	}

	/**
	 * 定位方法
	 */
    public void initLoc() {
        // 定位初始化
        mLocClient = new LocationClient(getApplicationContext());
        mLocClient.setAK("4QMZ8wQO95NVkEKKAG8LgzDo");
        locData = new LocationData();
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(5000);
        mLocClient.setLocOption(option);
        mLocClient.start();

		// 定位图层初始化
		myLocationOverlay = new MyLocationOverlay(mMapView);
		// 设置定位数据
		myLocationOverlay.setData(locData);

		// 添加定位图层
		mMapView.getOverlays().add(myLocationOverlay);
		// Toast.makeText(getApplicationContext(),
		// String.valueOf(locData.longitude) + "  " +
		// String.valueOf(locData.latitude), Toast.LENGTH_SHORT).show();
		myLocationOverlay.enableCompass();
		// 修改定位数据后刷新图层生效
		mMapView.refresh();
	}

	/**
	 * 手动触发一次定位请求
	 */
	public void requestLocClick() {
		isRequest = true;
		mLocClient.requestLocation();
		Toast.makeText(MapActivity.this, "正在定位……", Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * 初始化公交路线
	 */
	public void initBusLine() {
		MKPlanNode start = new MKPlanNode();
		start.pt = locGeoPoint;
		MKPlanNode end = new MKPlanNode();
//		Toast.makeText(
//				getApplicationContext(),
//				String.valueOf(locGeoPoint.getLatitudeE6() / 1E6) + " "
//						+ String.valueOf(locGeoPoint.getLongitudeE6() / 1E6),
//				Toast.LENGTH_SHORT).show();
		end.pt = new GeoPoint(geoPoints.get(0).getLatitudeE6()
				+ (int) (0.0002 * 1E6), geoPoints.get(0).getLongitudeE6()
				+ (int) (0.0002 * 1E6));
		
		mSearch.transitSearch(city, start, end);

	}

	/**
	 * set fake Location
	 * 
	 * @param longitude
	 * @param latitude
	 */
	private void setLocation(double longitude, double latitude) {
		String mMockProviderName = "spoof";
		// int mPostDelayed = 10000;
		LocationManager locationManager = (LocationManager) getApplicationContext()
				.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.getProvider(mMockProviderName) == null) {
			locationManager.addTestProvider(mMockProviderName, false, true,
					false, false, false, false, false, 0, 5);
			locationManager.setTestProviderEnabled(mMockProviderName, true);
		}
		Location loc = new Location(mMockProviderName);
		loc.setTime(System.currentTimeMillis());
		loc.setLatitude(latitude);
		loc.setLongitude(longitude);
		locationManager.setTestProviderLocation(mMockProviderName, loc);

		Log.i("gps", String.format("once: x=%s y=%s", longitude, latitude));
	}

	public void showRoutePopUp() {
		Context mContext = MapActivity.this;
		LayoutInflater mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View route = mLayoutInflater.inflate(R.layout.popup_map, null);
		TextView textView_route = (TextView) route
				.findViewById(R.id.textView_route);
		textView_route.setText(tmp);
		 mPopupWindow = new PopupWindow(route,
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

		mPopupWindow.showAtLocation(findViewById(R.id.bmapsView), Gravity.TOP
				| Gravity.CENTER_HORIZONTAL, 0, 40);
	}
}
