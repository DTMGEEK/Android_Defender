package com.example.androiddefprot.engine;

import java.security.Provider;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;



/**
 * 
* @Title: GPSInfoProvider.java 
* @Package com.example.androiddefprot.engine 
* @Description: GPS��Ϣ�ṩ�� ����ʹ�õ�����ƣ����Է�ֹGPS�����ʵ�����������ֻ����� 
* @author lian_weijian@163.com   
* @version V1.0
 */
public class GPSInfoProvider {

	private LocationManager manager = null;         
	private static GPSInfoProvider gpsProvider = null;
	private static SharedPreferences sp = null;
	private static Context context = null;
	private static MyLocationListener listener = null;
	
	
	//���췽��˽�л�
	private GPSInfoProvider() {
		super();
	}
	
	
	/**
	 * ʹ��synchronized ʹ�߳�ͬ����ֻ�ܻ��һ��ʵ����
	 * @param context    ������
	 * @return   GPSInfoProvider��ʵ����   
	 */
	public static synchronized GPSInfoProvider getInstance(Context context){
		if(gpsProvider == null){
			gpsProvider = new GPSInfoProvider();
			GPSInfoProvider.context = context;
		}
		return gpsProvider;
	}
	
	
	/**
	 * 
	 * @return ��õ�ǰ�ֻ���λ����Ϣ
	 */
	public String getLocation(){
		String location = null;
		this.manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		String provider = getProvider(manager);
		manager.requestLocationUpdates(provider, 6000, 50, getListener());
		this.sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		location = sp.getString("location", "");
		return location;
	}

	
	/**
	 * �õ�GPSProvider 
	 * @param manager   λ�ù�������ʵ��
	 * @return      ������õ�λ���ṩ��
	 */
	private String getProvider(LocationManager manager) {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		criteria.setAltitudeRequired(false);
		criteria.setCostAllowed(true);
		criteria.setSpeedRequired(true);
		return manager.getBestProvider(criteria, true);
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	private LocationListener getListener() {
		if(listener == null){
			listener = new MyLocationListener();
		}
		return listener;
	}
	
	
	/**
	 * ֹͣ��ȡGPS��Ϣ
	 */
	public void stopProvider(){
		this.manager.removeUpdates(getListener());
	}
	
	

	/**
	 * ���GPS�ľ�γ�ȣ����ұ��浽SharedPreferences��xml�ļ��С�
	 * @author jake
	 *
	 */
	private class MyLocationListener implements LocationListener{

		/**
		 * ���ֻ�λ�÷����ı�ʱ�򣬵��ø÷�����
		 */
		@Override
		public void onLocationChanged(Location location) {
			String latitude = "latitude" + location.getLatitude();
			String longitude = "longitude" + location.getLongitude();
			SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putString("location", latitude + "-" + longitude);
			editor.commit();
		}

		/**
		 * ���ֻ�״̬�����ı��ʱ�򣬵��ã�����->�����ã�������->���á�
		 */
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		/**
		 * ĳһ�豸����
		 */
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		/**
		 * ĳһ�豸������
		 */
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
	}

	
	
}
