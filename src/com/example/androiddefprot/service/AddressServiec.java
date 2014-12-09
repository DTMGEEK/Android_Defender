package com.example.androiddefprot.service;


import com.example.androiddefprot.R;
import com.example.androiddefprot.engine.NumberAddressService;
import com.example.androiddefprot.logmanagement.LogManagement;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
* @Title: AddressServiec.java 
* @Package com.example.androiddefprot.service 
* @Description: ��ַ��ѯ���񣬼����ֻ������״̬�����е绰�������ʱ��ͻ��ѯ���ݿ⣬�õ�����Ĺ����� 
* @author lian_weijian@163.com   
* @version V1.0
 */
public class AddressServiec extends Service {

	private static final String TAG = "AddressServiec";
	private TelephonyManager telephonyManager = null;     
	private WindowManager windowManager = null;
	private SharedPreferences sp = null;
	private View view = null;
	private MyPhoneStateListener listener = null;       //�绰״̬���������Զ������̳���PhoneStateListener
	
	

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	
	
	@Override
	public void onCreate() {
		super.onCreate();
		this.telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);//���ڷ������ֻ�ͨѶ��ص�״̬����Ϣ
		this.windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);         // ���ڿ�����
		this.sp = this.getSharedPreferences("config", Context.MODE_PRIVATE);
		listener = new MyPhoneStateListener();   
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE); //�绰״̬����
	}
	
	
	//����ȡ����ʱ�����
	@Override
	public void onDestroy() {
		super.onDestroy();
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
		this.view = null;
	}
	
	
	
	//�绰״̬����
	private class MyPhoneStateListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch(state){
			
			    case TelephonyManager.CALL_STATE_IDLE:  //�绰���ھ�ֹ״̬��û�к���
			    	if(view != null){
			    		windowManager.removeView(view);     //ȥ�������Ĺ�������ʾ������Ȼ�᳤��ͣ���ڽ�����
			    		view = null;
			    		
			    		//�ٴλ��ϵͳʱ�䣬�ж������Ƿ񳬹�3�룬���������3�룬�ж�Ϊɧ�ŵ绰
			    		
			    	}
				break;
			
				
			    case TelephonyManager.CALL_STATE_OFFHOOK:       //�绰��ͨ״̬    //���Լ���ͨ���Է��ĵ�������״��
			    	if(view != null){
			    		windowManager.removeView(view);         //ȥ�������Ĺ�������ʾ������Ȼ�᳤��ͣ���ڽ�����
			    		view = null;
			    	}
				break;
			
				
			    case TelephonyManager.CALL_STATE_RINGING:       //�绰����״̬
			    	LogManagement.i(TAG, "���������:" + incomingNumber);
			    	String address = NumberAddressService.getAddressByNumber(incomingNumber);
			    	//Toast.makeText(getApplicationContext(), "�����������:" + address, Toast.LENGTH_LONG).show();
			    	LogManagement.i(TAG, "�����������:" + address);
			    	showLocation(address);
			    	
			    	//��ȡ��ǰϵͳʱ�䣬������һ���ĵ绰
			    	
				break;
			}
		}
	}



	//�ڴ�����ʾ�����������Ϣ
	public void showLocation(String address) {
		//���ÿؼ����ֵĲ���
		 WindowManager.LayoutParams params = new LayoutParams();    
	     params.height = WindowManager.LayoutParams.WRAP_CONTENT;
	     params.width = WindowManager.LayoutParams.WRAP_CONTENT;
	     params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
	                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
	                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
	     params.format = PixelFormat.TRANSLUCENT;
	     params.type = WindowManager.LayoutParams.TYPE_TOAST;
	     params.setTitle("Toast");
	     params.gravity = Gravity.LEFT | Gravity.TOP;
	    //���ÿؼ����ֵĲ���
	     
	     params.x = sp.getInt("lastx", 0);
	     params.y = sp.getInt("lasty", 0);
		
	     //��ò����ļ���ʵ��
		view = View.inflate(getApplicationContext(), R.layout.show_location, null);
		LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_location);
		
		int background = sp.getInt("background", 0);
		if(background == 0){
			ll.setBackgroundResource(R.drawable.call_locate_gray); //���ñ���
		}else if(background == 1){
			ll.setBackgroundResource(R.drawable.call_locate_orange);
		}else{
			ll.setBackgroundResource(R.drawable.call_locate_green);
		}
				
		TextView tv = (TextView) view.findViewById(R.id.location_tv);
		tv.setTextSize(this.getResources().getDimension(R.dimen.atool_textsize_noservice));//���������С
		tv.setText(address);
		AddressServiec.this.windowManager.addView(view,params);
	}


}
