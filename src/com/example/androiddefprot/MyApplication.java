package com.example.androiddefprot;

import com.example.androiddefprot.domain.TaskInfo;
import com.example.androiddefprot.receiver.LockScreenReceiver;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 
* @Title: MyApplication.java 
* @Package com.example.androiddefprot 
* @Description: �洢Ӧ�ó����е�ȫ�ֱ����������ṩ���������ʹ�ã�activity��service��contentprovider��broadcastreceiver��
* @author lian_weijian@163.com   
* @version V1.0
 */
public class MyApplication extends Application {

	public TaskInfo taskinfo;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		//�ֹ�ע��һ���㲥������
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF); //����㲥������ֻ�ǹ�ע�����¼�
		filter.setPriority(1000);//�������ȼ���
		LockScreenReceiver receiver = new LockScreenReceiver();
		this.registerReceiver(receiver, filter);
	}
	
}
