package com.xstrikers.ganmaquv2.ui;


import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;


public class ganmaquApplication extends Application {
	
    private static ganmaquApplication mInstance = null;
    public boolean m_bKeyRight = true;
    BMapManager mBMapManager = null;
    public boolean[] selectType= null ;
    public boolean firstloc;
    public boolean myCircle; 
    public boolean allDay ;
    public double circle_lat;
    public double circle_lng;
    public enum types {EAT,SHOP,MOVIE,SCENE,COFFEE,KTV};
    
    //public static final String strKey = "970293a9a6573c4517821ee70a7e30a5";  // for baiduapi 
     public static final String strKey = "397c8e382ee8dcc520182990a807b1b9"; // for XPS 
     
    /*
    	注意：为了给用户提供更安全的服务，Android SDK自v2.1.3版本开始采用了全新的Key验证体系。
    	因此，当您选择使用v2.1.3及之后版本的SDK时，需要到新的Key申请页面进行全新Key的申请，
    	申请及配置流程请参考开发指南的对应章节
    */
	
	@Override
    public void onCreate() {
	    super.onCreate();
		mInstance = this;
		initEngineManager(this);
		selectType = new boolean[6];
		allDay = true;
		myCircle = false;
		firstloc= true;
		for (int i = 0; i < selectType.length; i++) {
			selectType[i] = false;
		}
		
	}
	
	public boolean isMyCircle() {
		return myCircle;
	}

	public void setMyCircle(boolean myCircle) {
		this.myCircle = myCircle;
	}

	public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(strKey,new MyGeneralListener())) {
            Toast.makeText(ganmaquApplication.getInstance().getApplicationContext(),
                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }
	}
	
	public static ganmaquApplication getInstance() {
		return mInstance;
	}
	
	
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
    static class MyGeneralListener implements MKGeneralListener {
        
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(ganmaquApplication.getInstance().getApplicationContext(), "您的网络出错啦！",
                    Toast.LENGTH_LONG).show();
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(ganmaquApplication.getInstance().getApplicationContext(), "输入正确的检索条件！",
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onGetPermissionState(int iError) {
            if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
                //授权Key错误：
//                Toast.makeText(DemoApplication.getInstance().getApplicationContext(), 
//                        "请在 DemoApplication.java文件输入正确的授权Key！", Toast.LENGTH_LONG).show();
            	Log.e("BAIDU KEY", "请在 DemoApplication.java文件输入正确的授权Key！");
                ganmaquApplication.getInstance().m_bKeyRight = false;
            }
        }
       
    }
}