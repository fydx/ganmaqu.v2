package com.xstrikers.ganmaquv2.map;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by LB on 14-1-20.
 */
public class LocationManagerHelper {
  private LocationManager locationManager;
  private Boolean locService;
  private String provider;
  private double lat, lng;
  private String city;
  private Location location;
  private LocationListener locationListener;
  private volatile static LocationManagerHelper uniqueInstance;

  public static LocationManagerHelper getInstance()
  {
    if (uniqueInstance == null)
    {
      synchronized (LocationManagerHelper.class)
      {
        if (uniqueInstance == null)
        {
          uniqueInstance = new LocationManagerHelper();
        }
      }
    }
    return uniqueInstance;
  }

  private LocationManagerHelper()
  {
    this.locService = false;
    locationListener = new LocationListener() {
      // 位置发生改变后调用
      public void onLocationChanged(Location location) {
        updateWithNewLocation();
      }

      // provider被用户关闭后调用
      public void onProviderDisabled(String provider) {
        updateWithNewLocation();
      }

      // provider被用户开启后调用
      public void onProviderEnabled(String provider) {

      }

      // provider状态变化时调用
      public void onStatusChanged(String provider, int status, Bundle extras) {

      }

    };
  }

  // 判断是否开启GPS，若未开启，打开GPS设置界面
  public void openGPS() {
    if (locationManager
        .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
        || locationManager
            .isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
      locService = true;
      Log.i("location service", "位置源已设置！");

      // Toast.makeText(this, , Toast.LENGTH_SHORT).show();
      // return;
    }
    else
    {
      locService = false;
      // 提示未开启定位服务 dialog + toast
      // Toast.makeText(this, "您未开启定位服务", Toast.LENGTH_SHORT).show();
      // createLogoutDialog();
    }
  }

  public void setLocationManager(LocationManager tempLocationManager)
  {
    this.locationManager = tempLocationManager;

  }

  // 获取Location Provider
  public void getProvider() {
    // 构建位置查询条件
    Criteria criteria = new Criteria();
    // 查询精度：高
    criteria.setAccuracy(Criteria.ACCURACY_FINE);
    // 是否查询海拨：否
    criteria.setAltitudeRequired(false);
    // 是否查询方位角:否
    criteria.setBearingRequired(false);
    // 是否允许付费：是
    criteria.setCostAllowed(true);
    // 电量要求：低
    criteria.setPowerRequirement(Criteria.POWER_LOW);
    // 返回最合适的符合条件的provider，第2个参数为true说明,如果只有一个provider是有效的,则返回当前provider
    this.provider = locationManager.getBestProvider(criteria, true);
    try {
      location = locationManager.getLastKnownLocation(provider);
      Log.i("ganmaqu", "Get provider");
    } catch (Exception e) {
      Log.i("ganmaqu", "provider is null");

    }
  }



  // Gps监听器调用，处理位置信息
  public void updateWithNewLocation() {
    String latLongString;
    if (location != null) {
      lat = location.getLatitude();
      lng = location.getLongitude();
      latLongString = "纬度:" + lat + "\n经度:" + lng;

    } else {
      latLongString = "无法获取地理信息";
    }
    Log.i("ganmaqu", latLongString);


  }
  public void requestLocationUpdates()
  {
    try {
      locationManager.requestLocationUpdates(provider, 2000, 30, locationListener);
    } catch (Exception e) {

    }

  }
   public String getLocation()
   {
       return String.valueOf(lat) + "," + String.valueOf(lng);
   }

   public void start()
   {
       this.openGPS();
       this.getProvider();
       this.updateWithNewLocation();
       this.requestLocationUpdates();
   }
}
