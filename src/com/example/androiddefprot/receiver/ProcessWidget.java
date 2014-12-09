package com.example.androiddefprot.receiver;

import com.example.androiddefprot.logmanagement.LogManagement;
import com.example.androiddefprot.service.UpdateWidgetService;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 
* @Title: ProcessWidget.java 
* @Package com.example.androiddefprot.receiver 
* @Description:  * 
 * ���������ļ� ÿ���̶���ʱ�� ����һ�½���
 * ��Сֵ ���Сʱ 1800000����
 * onRecevie - > onUpdate
 *
 * ע�� widget������������ʵ�����ǵ�Ӧ�ó�������
 * ��ʾ�������Ӧ�ó���
 * ��ͬ������ ���ǵ�widget�Ĵ��������ٶ�Ӧ�� �ص����¼����ܻ��в���
 * android luncher / htc sence / ��ui / 360����/awt /qq����/....
* @author lian_weijian@163.com   
* @version V1.0
 */
public class ProcessWidget extends AppWidgetProvider {

	private static final String TAG = "ProcessWidget";
	private Intent intent = null;
	
	 // widget��һ�δ�����ʱ�� ִ�еķ���
    // ��ʼ��widget���ݵĲ���,�����Ժ��̨
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		LogManagement.i(TAG, "widget onEnabled");
		intent = new Intent(context,UpdateWidgetService.class);
		context.startService(intent);
	}
	 

	
	//��ĳһ��widget��ɾ����ʱ�� ��ִ��ondelete����
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		LogManagement.i(TAG, "widget onDeleted");
		intent = new Intent(context,UpdateWidgetService.class);
		context.stopService(intent);
	}


}
