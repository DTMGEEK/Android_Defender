package com.example.androiddefprot.service;

import java.util.Timer;
import java.util.TimerTask;

import com.example.androiddefprot.R;
import com.example.androiddefprot.receiver.LockScreenReceiver;
import com.example.androiddefprot.util.TaskUitl;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.RemoteViews.RemoteView;

/**
 * 
* @Title: UpdateWidgetService.java 
* @Package com.example.androiddefprot.service 
* @Description: �Զ�����widget��Ϣ�ĺ�̨������ 
* @author lian_weijian@163.com   
* @version V1.0
 */
public class UpdateWidgetService extends Service {

	private Timer timer = null; //��ʱ��
	private TimerTask timertask = null; //��ʱ�����������
	private AppWidgetManager manager = null; //widget������
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//��ʼ��
		this.timer = new Timer();
		this.manager = AppWidgetManager.getInstance(getApplicationContext());//���widgetmanager
		//��ʱ�������࣬ÿһ�ε��ö�����һ���µ��߳�
		timertask = new TimerTask() {
			
			@Override
			public void run() {
				ComponentName name = new ComponentName(
						"com.example.androiddefprot",
						"com.example.androiddefprot.receiver.ProcessWidget"); // ��һ�������ǰ������ڶ�����������������һ��Ҫ�ڰ�����
				RemoteViews view = new RemoteViews("com.example.androiddefprot", R.layout.process_widget);    
				view.setTextViewText(
						R.id.process_widget_tv_process_count,
						"������Ŀ��"
								+ TaskUitl
										.getProcessCount(getApplicationContext()));
				
				view.setTextColor(R.id.process_widget_tv_process_count, Color.RED);
				
				view.setTextViewText(
						R.id.process_widget_tv_process_memory,
						"�����ڴ�"
								+ TaskUitl
										.getMemeorySize(getApplicationContext()));
				
				view.setTextColor(R.id.process_widget_tv_process_memory,Color.RED);
				Intent intent = new Intent(UpdateWidgetService.this,LockScreenReceiver.class);
				PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
				view .setOnClickPendingIntent(R.id.process_widget_btn_clear, pi);
				UpdateWidgetService.this.manager.updateAppWidget(name, view);   //����widgetui���
			}
		};
		//ÿ��һ����ִ�У�ÿ��������½���
		timer.schedule(timertask, 1000, 2000);
	}
	
	
	
	@Override
	public void onDestroy() {
		this.timer.cancel();
		this.timer = null;
		this.manager = null;
		super.onDestroy();
	}
	
	
}
