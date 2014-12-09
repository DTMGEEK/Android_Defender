package com.example.androiddefprot.receiver;

import com.example.androiddefprot.ui.LostProtectedActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * 
* @Title: CallPhoneReceiver.java 
* @Package com.example.androiddefprot.receiver 
* @Description: ���Ž��������룬������������������123123ʱ�ͻ���뵽 �ֻ���ʿ�ķ�������
* @author lian_weijian@163.com   
* @version V1.0
 */
public class CallPhoneReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String number = this.getResultData();
		if("123123".equals(number)){
			Intent lostIntent = new Intent(context,LostProtectedActivity.class);
			//ָ��Ҫ�����activity���Լ�������ջ�������� 
			lostIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);      //��Activity����ջ�������Activity�������Activity�����������Լ�������ջ��
			context.startActivity(lostIntent);
			// ��ֹ������绰 
			// ����ͨ��  abortBroadcast();
			this.setResultData(null); //��ֹ����绰
		}
	}

}
