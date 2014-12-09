package com.example.androiddefprot.receiver;

import com.example.androiddefprot.logmanagement.LogManagement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.util.Log;

/**
 * 
* @Title: BootCompleteReceiver.java 
* @Package com.example.androiddefprot.receiver 
* @Description: ���ֻ������󣬼���sim���Ĵ����Ƿ��иı䣬�ı��˾���Ԥ�����õİ�ȫ�ֻ����Ͷ���
* @author lian_weijian@163.com   
* @version V1.0
 */
public class BootCompleteReceiver extends BroadcastReceiver {

	private static final String TAG = "BootCompleteReceiver";
	private SharedPreferences sp = null;
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
	    LogManagement.i(TAG, "�������");
		  
		this.sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean isprotecting = this.sp.getBoolean("isprotecting", false);
		if(isprotecting){
			TelephonyManager telephonyManager =  (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String simNumber = telephonyManager.getSimSerialNumber();
			String realSim = sp.getString("sim", "");
	
			if(!simNumber.equals(realSim)){
				LogManagement.i(TAG, "���Ͷ���");
				SmsManager smsManager = SmsManager.getDefault();
				String destinationAddress = this.sp.getString("safenumber", "");
				smsManager.sendTextMessage(destinationAddress, null, "�ֻ�sim�� �ı䣬���ܱ���", null, null);
			}
		}
	}

}
