/**
 * 
 */
package com.lt.hm.wovideo.utils.location;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lt.hm.wovideo.interf.OnUpdateLocationListener;
import com.lt.hm.wovideo.utils.SharedPrefsUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.utils.UpdateLocationMsg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * 辅助工具类
 * @创建时间： 2015年11月24日 上午11:46:50
 * @项目名称： AMapLocationDemo2.x
 * @author hongming.wang
 * @文件名称: Utils.java
 * @类型名称: Utils
 */
public class Utils {
	/**
	 *  开始定位
	 */
	public final static int MSG_LOCATION_START = 0;
	/**
	 * 定位完成
	 */
	public final static int MSG_LOCATION_FINISH = 1;
	/**
	 * 停止定位
	 */
	public final static int MSG_LOCATION_STOP= 2;
	
	public final static String KEY_URL = "URL";
	public final static String URL_H5LOCATION = "file:///android_asset/location.html";

	private static AMapLocationClientOption locationOption = null;
	private static AMapLocationClient locationClient = null;


	public static void StartLocation(Context context){
		locationClient = new AMapLocationClient(context);
		locationOption = new AMapLocationClientOption();
		// 设置定位模式为高精度模式
		locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
		// 设置定位监听
		locationClient.setLocationListener(new AMapLocationListener(){

			@Override
			public void onLocationChanged(AMapLocation aMapLocation) {
				if (null != aMapLocation) {
					Message msg = mHandler.obtainMessage();
					msg.obj = aMapLocation;
					msg.what = Utils.MSG_LOCATION_FINISH;
					mHandler.sendMessage(msg);
				}
			}
		});
		// 设置定位参数
		locationClient.setLocationOption(locationOption);
		locationClient.startLocation();
		mHandler.sendEmptyMessage(Utils.MSG_LOCATION_START);
	}

	static Handler mHandler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
				// 定位完成
				case Utils.MSG_LOCATION_FINISH:
					AMapLocation loc = (AMapLocation) msg.obj;
					SharedPrefsUtils.setStringPreference("city_name",loc.getCity());
					SharedPrefsUtils.setStringPreference("city_code",loc.getCityCode());
					String result = Utils.getLocationStr(loc);
					removeBeforViews(loc.getCity(),loc.getCityCode());
					TLog.log("location_result" + result);
					locationClient.stopLocation();
					// TODO: 8/2/16  缓存定位信息。。
					break;
				//停止定位
				case Utils.MSG_LOCATION_STOP:
//                    tvReult.setText("定位停止");
					break;
				default:
					break;
			}
		}
	};

	private static void removeBeforViews(String name,String code) {
		ArrayList<OnUpdateLocationListener> registerSucListeners = UpdateLocationMsg.getInstance().downloadListeners;
		if (registerSucListeners == null || registerSucListeners.size() == 0) return;
		for (int i = 0; i < registerSucListeners.size(); i++) {
			registerSucListeners.get(i).onUpdateLocListener(name,code);
		}
	}


	/**
	 * 根据定位结果返回定位信息的字符串
	 * @param location
	 * @return
	 */
	public synchronized static String getLocationStr(AMapLocation location){
		if(null == location){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		//errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
		if(location.getErrorCode() == 0){
			sb.append("定位成功" + "\n");
			sb.append("定位类型: " + location.getLocationType() + "\n");
			sb.append("经    度    : " + location.getLongitude() + "\n");
			sb.append("纬    度    : " + location.getLatitude() + "\n");
			sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
			sb.append("提供者    : " + location.getProvider() + "\n");
			
			if (location.getProvider().equalsIgnoreCase(
					android.location.LocationManager.GPS_PROVIDER)) {
				// 以下信息只有提供者是GPS时才会有
				sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
				sb.append("角    度    : " + location.getBearing() + "\n");
				// 获取当前提供定位服务的卫星个数
				sb.append("星    数    : "
						+ location.getSatellites() + "\n");
			} else {
				// 提供者是GPS时是没有以下信息的
				sb.append("国    家    : " + location.getCountry() + "\n");
				sb.append("省            : " + location.getProvince() + "\n");
				sb.append("市            : " + location.getCity() + "\n");
				sb.append("城市编码 : " + location.getCityCode() + "\n");
				sb.append("区            : " + location.getDistrict() + "\n");
				sb.append("区域 码   : " + location.getAdCode() + "\n");
				sb.append("地    址    : " + location.getAddress() + "\n");
				sb.append("兴趣点    : " + location.getPoiName() + "\n");
				//定位完成的时间
				sb.append("定位时间: " + formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss:sss") + "\n");
			}
		} else {
			//定位失败
			sb.append("定位失败" + "\n");
			sb.append("错误码:" + location.getErrorCode() + "\n");
			sb.append("错误信息:" + location.getErrorInfo() + "\n");
			sb.append("错误描述:" + location.getLocationDetail() + "\n");
		}
		//定位之后的回调时间
		sb.append("回调时间: " + formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss:sss") + "\n");
		return sb.toString();
	}
	
	private static SimpleDateFormat sdf = null;
	public synchronized static String formatUTC(long l, String strPattern) {
		if (TextUtils.isEmpty(strPattern)) {
			strPattern = "yyyy-MM-dd HH:mm:ss";
		}
		if (sdf == null) {
			try {
				sdf = new SimpleDateFormat(strPattern, Locale.CHINA);
			} catch (Throwable e) {
			}
		} else {
			sdf.applyPattern(strPattern);
		}
		if (l <= 0l) {
			l = System.currentTimeMillis();
		}
		return sdf == null ? "NULL" : sdf.format(l);
	}


}
