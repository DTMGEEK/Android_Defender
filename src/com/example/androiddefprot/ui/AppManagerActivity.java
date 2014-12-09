package com.example.androiddefprot.ui;

import java.util.ArrayList;
import java.util.List;

import com.example.androiddefprot.R;
import com.example.androiddefprot.adapter.AppManagerAdapter;
import com.example.androiddefprot.domain.AppInfo;
import com.example.androiddefprot.engine.AppInfoProvider;
import com.example.androiddefprot.logmanagement.LogManagement;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;



/**
 * 
* @Title: AppManagerActivity.java 
* @Package com.example.androiddefprot.ui 
* @Description:  ���������Activity 
* @author lian_weijian@163.com   
* @version V1.0
 */
public class AppManagerActivity extends Activity implements OnClickListener {
	
	private static final int GET_ALL_APP_FINISH = 10;
	private static final int GET_ALL_USER_APP_FINISH = 11;
	private static final String TAG = "AppManagerActivity";
	private TextView app_manager_tv_title = null;
	private ListView app_manager_lv = null;
	private LinearLayout app_manager_ll_loading = null;
	private List<AppInfo> appInfos = null; //��������app����Ϣ
	private List<AppInfo> userAppInfos = null; //�����û�app����Ϣ
	private AppManagerAdapter adapter = null;  //����������
	private PopupWindow popupWindow = null;     //��������
	private boolean isloading = false; //�ж��ǲ��Ǽ��������
	private LinearLayout app_manager_tv_ll_title = null;
	
	
	
	private Handler handler = new Handler(){         //����һ���µ��̣߳�ȥ�������app����Ϣ
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				 case GET_ALL_APP_FINISH:      //��ȡ���е�Ӧ�ó���
				  	adapter = new AppManagerAdapter(AppManagerActivity.this,appInfos);
				  	app_manager_lv.setAdapter(adapter);
					app_manager_ll_loading.setVisibility(View.INVISIBLE);
				  	AppManagerActivity.this.isloading = false;
			     break;	
			     
				 case GET_ALL_USER_APP_FINISH:   //��ȡ���û��Լ���װ�ĳ���
						adapter = new AppManagerAdapter(AppManagerActivity.this,userAppInfos);
					  	app_manager_lv.setAdapter(adapter);
					  	app_manager_ll_loading.setVisibility(View.INVISIBLE);
					  	AppManagerActivity.this.isloading = false;
				 break;
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.app_manager);
		this.app_manager_tv_title = (TextView) this.findViewById(R.id.app_manager_tv_title);
		this.app_manager_tv_ll_title = (LinearLayout) this.findViewById(R.id.app_manager_tv_ll_title);
		//����Ӧ�ó�����û�Ӧ�ó���������¼�
		AppManagerActivity.this.app_manager_tv_ll_title.setOnClickListener(AppManagerActivity.this);//���Ҫ��������¼���������������
		
		this.app_manager_lv = (ListView) this.findViewById(R.id.app_manager_lv);
	    this.app_manager_ll_loading = (LinearLayout) this.findViewById(R.id.app_manager_ll_loading);
	    this.app_manager_ll_loading.setVisibility(View.VISIBLE);
	    
	    initUI(true);//��ʼ�����߸���listView
	    
	    //������ListView��ÿһ����ʱ������popupwindow
	    app_manager_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				dismissPopWin();
				
				int[] arrayInt = new int[2];
				view.getLocationInWindow(arrayInt);
				 
				int i = arrayInt[0] + 60;
				int j = arrayInt[1] - 15;
				
				AppInfo appinfo = (AppInfo) app_manager_lv.getItemAtPosition(position);

				View popupview = View.inflate(AppManagerActivity.this, R.layout.popup_item, null); //�õ�popupwindow�Ĳ����ļ�ʵ��
				
				LinearLayout ll_start = (LinearLayout) popupview.findViewById(R.id.ll_start);
				LinearLayout ll_uninstall = (LinearLayout) popupview.findViewById(R.id.ll_uninstall);
				LinearLayout ll_share = (LinearLayout) popupview.findViewById(R.id.ll_share);
				
				//����popupwindow�ļ�����
				ll_start.setOnClickListener(AppManagerActivity.this);
				ll_uninstall.setOnClickListener(AppManagerActivity.this);
				ll_share.setOnClickListener(AppManagerActivity.this);
				
				
				//����Tag��ǩ�����Ա�ʶ�ǵ�����һ��app
				ll_start.setTag(position);
				ll_uninstall.setTag(position);
				ll_share.setTag(position);
				
				
				//����popupwindow�ĵ�������
				LinearLayout ll_popup = (LinearLayout) popupview.findViewById(R.id.ll_popup);
				ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f);
				sa.setDuration(200);
				
				
				//ʵ����popupwindow
				popupWindow = new PopupWindow(popupview, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				Drawable background = AppManagerActivity.this.getResources().getDrawable(R.drawable.local_popup_bg); //����popupwindow�ı�����һ��Ҫ���ñ�����Ȼ����ֺܶ���ֵ�����
				popupWindow.setBackgroundDrawable(background); //���ñ���
				popupWindow.showAtLocation(view, Gravity.LEFT|Gravity.TOP, i, j); //����popupwindow�ĳ���λ��
				ll_popup.startAnimation(sa);  //��������
				
			}
		});

	    
	    //����listview����ʱ��״̬�ĸı�
	    app_manager_lv.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				dismissPopWin();
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				dismissPopWin();
			}
		});
	}


	/**
	 * ��ʼ�����߸���ListView�߳�
	 */
	private void initUI(final boolean flag) {
		this.app_manager_ll_loading.setVisibility(View.VISIBLE);
		new Thread(){       //����һ�����̣߳�����ȡ����app��Ϣ
			@Override
			public void run() {
				AppManagerActivity.this.isloading = true;
				if(flag){
				    appInfos = new AppInfoProvider(AppManagerActivity.this).getAllApp(); 
					Message msg = new Message();
					msg.what = GET_ALL_APP_FINISH;
					handler.sendMessage(msg);
				}else{//ֻ�ǻ���û���װ��Ӧ�ó���
					appInfos = new AppInfoProvider(AppManagerActivity.this).getAllApp(); 
					AppManagerActivity.this.userAppInfos = getUserApp(appInfos);
					Message msg = new Message();
					msg.what = GET_ALL_USER_APP_FINISH;
					handler.sendMessage(msg);
				}
			}
	    }.start();
	}
	
	
	
	/**
	 * �ر�popupwindow
	 */
	private void dismissPopWin() {
		if(popupWindow != null){
			popupWindow.dismiss();
			popupWindow = null;
		}
	}


     /**
      * �ж��û����popupwind����һ��ѡ��
      */
	@Override
	public void onClick(View v) {
		LogManagement.i(TAG, "hello");
		if(AppManagerActivity.this.isloading){
			return;
		}
		int positon = 0;
		if(v.getTag() != null){
			positon = (Integer) v.getTag();
		}
		
		LogManagement.i(TAG, String.valueOf(positon));
		AppInfo item = appInfos.get(positon);
		String packname = item.getPackname();

		
		// �жϵ�ǰ�б��״̬
		String titletext;
		TextView tv;
		if (v instanceof TextView) {
			tv = (TextView) v;
			titletext = tv.getText().toString();
			if ("���г���".equals(titletext)) {
				item = appInfos.get(positon);
				packname = item.getPackname();	
			} else {
				item = userAppInfos.get(positon);
				packname = item.getPackname();
			}
		} else {
			if ("���г���".equals(app_manager_tv_title.getText().toString())) {
				item = appInfos.get(positon);
				packname = item.getPackname();
			} else {
				item = userAppInfos.get(positon);
				packname = item.getPackname();
			}
		}
		
		
		dismissPopWin();
		switch (v.getId()) {
		
			case R.id.ll_share:         //�û�����popupwindow�ķ���ť��ʱ��Ĵ���
				LogManagement.i(TAG, "����" + packname);
				Intent shareIntent = new Intent();
				shareIntent.setAction(Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_TEXT, "�Ƽ�ʹ��ʹ��һ��app" + item.getAppname());
				shareIntent.putExtra(Intent.EXTRA_SUBJECT, "����");
				AppManagerActivity.this.startActivity(shareIntent);
			break;
				
			
			
			case R.id.ll_uninstall:    //�û�����popupwindow��ж�ذ�ť��ʱ��Ĵ���
				// ������ж��ϵͳ��Ӧ�ó���
				if (item.isSystemApp()) {
					Toast.makeText(this, "ϵͳӦ�ò��ܱ�ɾ��", 0).show();
				} else {
					LogManagement.i(TAG, "ж��" + packname);
					String uristr = new String("package:" + packname);
					Intent deleteIntent = new Intent();
					Uri uri = Uri.parse(uristr);
					deleteIntent.setAction(Intent.ACTION_DELETE);
					deleteIntent.setData(uri);
					AppManagerActivity.this.startActivityForResult(deleteIntent, 0);
				}
			break;
			
			
			
			case R.id.ll_start:         //�û�����popupwindow�����а�ť��ʱ��Ĵ���
				LogManagement.i(TAG, "����" + packname);
				// getPackageManager().queryIntentActivities(intent, flags);
				try {
					PackageInfo info = getPackageManager().getPackageInfo(
							packname,
							PackageManager.GET_UNINSTALLED_PACKAGES
									| PackageManager.GET_ACTIVITIES);       //�õ�����package����Ϣ
					ActivityInfo[] activityinfos = info.activities;
					if (activityinfos.length > 0) {       //ֻ����activity����Ϣ��ʱ���ִ��
						ActivityInfo startActivity = activityinfos[0];      
						Intent intent = new Intent();
						intent.setClassName(packname, startActivity.name);    //����������activity��Ϣ
						startActivity(intent);                       //����activity     
 					} else {
						Toast.makeText(this, "��ǰӦ�ó����޷�����", 0).show();
					}
				} catch (Exception e) {
					Toast.makeText(this, "Ӧ�ó����޷�����", 0).show();
					e.printStackTrace();
				}
			break;
			
			
			/**
			 * �������Ӧ�ó����ʱ����Ӧ�¼�
			 */
			case R.id.app_manager_tv_ll_title:
				String allappTitle = "���г���";//AppManagerActivity.this.getResources().getString(R.string.app_manager_tv_title_text);
				String title = app_manager_tv_title.getText().toString().trim();
				if(allappTitle.equals(title)){
					app_manager_tv_title.setText(AppManagerActivity.this.getResources().getString(R.string.app_manager_tv_title_user_text));
					//app_manager_tv_title.setText("�û�����");
					AppManagerActivity.this.initUI(false);
					
					
				}else{
					app_manager_tv_title.setText(AppManagerActivity.this.getResources().getString(R.string.app_manager_tv_title_text));
					//app_manager_tv_title.setText("�û�����");
					AppManagerActivity.this.initUI(true);
				}
			break;
		}
	}
	
	
	

	/**
	 *  ����һ��Activity��Ļص�����
	 */
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		String allappTitle = AppManagerActivity.this.getResources().getString(R.string.app_manager_tv_title_text);
		String title = app_manager_tv_title.getText().toString().trim();
		if(allappTitle.equals(title)){
			AppManagerActivity.this.initUI(false);
		}else{
			AppManagerActivity.this.initUI(true);
		}
			
	}
	
	
		/**
		 * ��������û���װ��app
		 * @param appinfos  �������ֻ�������app����Ϣ��List
		 * @return   //�������û�app��Ϣ��List
		 */
		private List<AppInfo> getUserApp(List<AppInfo> appinfos) {
			List<AppInfo> userAppInfos = new ArrayList<AppInfo>();
			for(AppInfo appinfo:appInfos){
				if(!appinfo.isSystemApp()){
					userAppInfos.add(appinfo);
				}
			}
			return userAppInfos;
		}

}
