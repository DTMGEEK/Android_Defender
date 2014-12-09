package com.example.androiddefprot.service;

import java.util.ArrayList;
import java.util.List;

import com.example.androiddefprot.db.dao.AppLockDao;
import com.example.androiddefprot.logmanagement.LogManagement;
import com.example.androiddefprot.ui.LockScreenActivity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * 
* @Title: WatchDogService.java 
* @Package com.example.androiddefprot.service 
* @Description: ���Ź����񣬿�������󣬻�۲�ϵͳ�б�������Ӧ�ã����Ƿ���Ҫ����������
* @author lian_weijian@163.com   
* @version V1.0
 */
public class WatchDogService extends Service {

	protected static final String TAG = "WatchDogService";
	private List<String> lockApps = null;
	private AppLockDao dao = null;
	private boolean flag = true;
	private ActivityManager activityManager = null;
	private Intent lockScreenIntent = null;
	private List<String> tempstopApps = null;
	private MyBinder binder = null;
	private KeyguardManager keyguardManager = null;
	
	
	//Ϊ����LockScreenActivity���Ե��õ�IService����ķ�����ͨ��IBinder���á�
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return binder;
	}


	//ͨ��IBinder�����������ķ���  
	public class MyBinder extends Binder implements IService{

		@Override
		public void callAppProtectStart(String packagename) {
			appProtectStart(packagename);
		}

		@Override
		public void callAppProtectStop(String packagename) {
			appProtectStop(packagename);
		}
	}
	
	
	/**
	 * ��ĳ�����Ƴ���ʱֹͣ�����ļ��ϣ�������������
	 * @param packagename
	 */
	
	public void appProtectStart(String packagename){
		if(this.tempstopApps.contains(packagename)){
			tempstopApps.remove(packagename);
		}
	}

	/**
	 * ��ĳ��������ӽ�����ʱֹͣ�����ļ��ϣ���ʱֹͣ����
	 * @param packagename
	 */
	public void appProtectStop(String packagename){
		this.tempstopApps.add(packagename);
	}
	
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		this.getContentResolver().registerContentObserver(
				Uri.parse("content://com.text.androiddefprot.provider"), true,
				new MyObserver(new Handler()));
		 // �۲�ĳ�����ݵ���Ϣ�Ƿ�ı�
		
		//��ȡ�ֻ��Ƿ�����
		WatchDogService.this.keyguardManager = (KeyguardManager)WatchDogService.this.getSystemService(KEYGUARD_SERVICE);
		
		binder = new MyBinder();
		this.dao = new AppLockDao(WatchDogService.this);
		this.lockApps = this.dao.findAll();
		this.activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
		lockScreenIntent = new Intent(WatchDogService.this,LockScreenActivity.class);
		tempstopApps = new ArrayList<String>();
		lockScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//���񲻴�������ջ���ڷ���������activity������ϴ��д���
		new Thread(){
			@Override
			public void run() {
				super.run();
				
				while(WatchDogService.this.flag){
					try {
						//�ж��ǲ���������״̬���Ǿ����������ʱ��ͣ�����ļ���
						if(WatchDogService.this.keyguardManager.inKeyguardRestrictedInputMode()){
							//�����ʱ�ļ���
							tempstopApps.clear();
						}
						
						//lockapps ��ϢΪ���µ�
						//lockapps = dao.getAllApps();
						// �õ���ǰ�������г���İ���
						// ����ϵͳ���������ջ����Ϣ , taskinfos�ļ�������ֻ��һ��Ԫ��
						// ���ݾ��ǵ�ǰ�������еĽ��̶�Ӧ������ջ
						
						
						List<RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
						RunningTaskInfo currenttask = tasks.get(0);
						String packagename = currenttask.topActivity.getPackageName();
						LogManagement.i(TAG, "��ǰ���еĳ���" + packagename);
						if(lockApps.contains(packagename)){
							
							//�����ǰӦ�ó�����Ҫ����ʱֹͣ����
							if(WatchDogService.this.tempstopApps.contains(packagename)){
								this.sleep(1000);    //���Ҫ��ͣ���������߳���˯��
								continue;
							}
							LogManagement.i(TAG, "��Ҫ����");
							lockScreenIntent.putExtra("packagename", packagename); 
							WatchDogService.this.startActivity(lockScreenIntent);
						}else{
							//todo����
						}
						
						sleep(1000);
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	
	
	/**
	 * ����ְͣ��ʱ��ͬʱֹͣ�߳�
	 */
	@Override
	public void onDestroy() {
		
		super.onDestroy();
		WatchDogService.this.flag = false;
	}


	/**
	 * �Զ���۲���
	 * @author jake
	 *
	 */
	private class MyObserver extends ContentObserver{

		public MyObserver(Handler handler) {
			super(handler);
		}
		
		//�����ݷ����ı��������������
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			//���¸���lockApps�������������
			LogManagement.i(TAG, "------------------------------lockApps���ݷ����˸ı�");
			WatchDogService.this.lockApps = WatchDogService.this.dao.findAll();//���»��������Ӧ�ó���
		}
	}
	
	
}
