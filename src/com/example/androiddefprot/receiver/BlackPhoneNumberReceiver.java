package com.example.androiddefprot.receiver;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.example.androiddefprot.db.dao.BlackNumberDao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.util.Log;
/**
 * 
* @Title: BlackNumberDao.java 
* @Package com.example.androiddefprot.db.dao 
* @Description: ����������Ҷ�
* @author lian_weijian@163.com   
* @version V1.0
 */

public class BlackPhoneNumberReceiver extends BroadcastReceiver {

	private BlackNumberDao bndao = null;
	private TelephonyManager telManager = null;
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		this.bndao = new BlackNumberDao(context);
		this.telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		int state = telManager.getCallState();
		switch(state){
		
		    //���õ�ʱ��
			case TelephonyManager.CALL_STATE_IDLE:
			break;
			
			//�绰�Ҷ�
			case TelephonyManager.CALL_STATE_OFFHOOK:
			break;
			
			//�绰����
			case TelephonyManager.CALL_STATE_RINGING:
				String incomeNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
				boolean blackNumFlag = this.bndao.find(incomeNumber);
				if(blackNumFlag){
					endCall();
				}
			break;
		}
	}
	
	
	
	/**
	 * �Ҷϵ绰 
	 */
	private void endCall(){
		Class<TelephonyManager> c = TelephonyManager.class;      
		
		try {
			    Method getITelephonyMethod = c.getDeclaredMethod("getITelephony", (Class[]) null);  
	            getITelephonyMethod.setAccessible(true);  
	            ITelephony iTelephony = null;  
	            iTelephony = (ITelephony) getITelephonyMethod.invoke(telManager, (Object[]) null);  
	            iTelephony.endCall();  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
