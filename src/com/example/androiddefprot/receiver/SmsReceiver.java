package com.example.androiddefprot.receiver;


import com.example.androiddefprot.R;
import com.example.androiddefprot.engine.GPSInfoProvider;
import com.example.androiddefprot.logmanagement.LogManagement;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.telephony.gsm.SmsManager;
import android.util.Log;

/**
 * 
* @Title: SmsReceiver.java 
* @Package com.example.androiddefprot.receiver 
* @Description: ���Ź㲥������ ����ȡ�������ݣ�������Ӧ�Ĳ�����
* @author lian_weijian@163.com   
* @version V1.0
 */
public class SmsReceiver extends BroadcastReceiver {

	private static final String TAG = "SmsReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		for(Object pdu : pdus){
			SmsMessage sms = SmsMessage.createFromPdu((byte[])pdu);
			
			String content = sms.getMessageBody();
			String sender = sms.getOriginatingAddress();
			LogManagement.i(TAG, "�������ݣ�"+content);
			if("#*location*#".equals(content)){
				this.abortBroadcast();
				GPSInfoProvider provider = GPSInfoProvider.getInstance(context);
				String location = provider.getLocation();
				if("".equals(location)){
					
				}else{
					SmsManager manager = SmsManager.getDefault();
					manager.sendTextMessage(sender, null, "�ص���:"+location, null, null);
				}
			
			}else if("#*locknow*#".equals(content)){    //Զ�̿������������������롣
				DevicePolicyManager manager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
				manager.resetPassword("123", 0);
				manager.lockNow();
				this.abortBroadcast();
			}else if("#*wipedata*#".equals(content)){       //���Ͷ��Ű��ֻ���������ȫ���
				DevicePolicyManager manager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
				manager.wipeData(0);
				this.abortBroadcast();
			}else if("#*alarm*#".equals(content)){         //���Ͷ���ʹ�ֻ�����������
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
				player.setVolume(1.0f, 1.0f);
				player.start();
				this.abortBroadcast();
			}
			
		}
	}

}
