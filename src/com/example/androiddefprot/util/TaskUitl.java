package com.example.androiddefprot.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

/**
 * 
* @Title: TaskUitl.java 
* @Package com.example.androiddefprot.util 
* @Description: ���̹�����
* @author lian_weijian@163.com   
* @version V1.0
 */
public class TaskUitl {
	
	//ɱ�����н��̣�����ϵͳĬ�Ͻ���
	public static void killAllProcess(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		 List<RunningAppProcessInfo> infos =  am.getRunningAppProcesses();
		 for(RunningAppProcessInfo info : infos){
			 String packageName = info.processName;
			 am.killBackgroundProcesses(packageName);
		 }
	}
	
	
	//�õ����̵�����
	public static int getProcessCount(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> list = am.getRunningAppProcesses();
		return list.size();
	}
	
	//�õ����õ��ڴ���
	public static String getMemeorySize(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return TextFormater.getDataSize(outInfo.availMem);
	}
	
	
}
