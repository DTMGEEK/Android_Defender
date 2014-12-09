package com.example.androiddefprot.ui;


import com.example.androiddefprot.R;
import com.example.androiddefprot.adapter.MainUiAdapter;
import com.example.androiddefprot.logmanagement.LogManagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 
* @Title: MainScreenActivity.java 
* @Package com.example.androiddefprot.ui 
* @Description: 主界面Activity
* @author lian_weijian@163.com   
* @version V1.0
 */
public class MainScreenActivity extends Activity implements OnItemClickListener {

	private static final String TAG = "MainScreenActivity";
	private GridView gv_main = null;
	private MainUiAdapter mainuiadapter = null;
	private SharedPreferences.Editor editor = null;
	
	
	@Override
	 protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainscreen);
		this.gv_main = (GridView) this.findViewById(R.id.gv_main);
		this.mainuiadapter = new MainUiAdapter(this);
		this.gv_main.setAdapter(mainuiadapter);
		this.gv_main.setOnItemClickListener(this);
	
		this.editor = this.getSharedPreferences("config", Context.MODE_PRIVATE).edit();
		
		this.gv_main.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, final View view,
					int position, long id) {
				final EditText et = new EditText(MainScreenActivity.this);
				et.setHint("请输入文本");
				Dialog dialog = new AlertDialog.Builder(MainScreenActivity.this).setTitle("设置")
						.setMessage("请输入要更新的名字").setView(et)
						.setPositiveButton("确定", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								String name = et.getText().toString().trim();
								if("".equals(name)){
									Toast.makeText(MainScreenActivity.this, "修改的名字不能为空", Toast.LENGTH_SHORT).show();
								}else{
									MainScreenActivity.this.editor.putString("lostname", name);
									MainScreenActivity.this.editor.commit();
									TextView tv = (TextView) view.findViewById(R.id.tv_mainscreen);
									tv.setText(name);
								}
							}
						}).setNegativeButton("取消", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
							}
						})
						.create();
				dialog.show();
				return false;
			}
		});
	}


	
	
	/**
	 * 当gridview的条目被点击的时候 对应的回调 parent :　girdview view : 当前被点击的条目 Linearlayout
	 * position : 点击条目对应的位置 id : 代表的行号
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		LogManagement.i(this.TAG, "点击的位置" + position);
		switch(position){
			
			case 0: //手机防盗功能
				LogManagement.i(this.TAG, "进入手机防盗");
				Intent intent = new Intent(this,LostProtectedActivity.class);
				this.startActivity(intent);
			break;
			
			
			case 1: //进入通讯卫士
				LogManagement.i(this.TAG, "进入通讯卫士");
				Intent callsmsIntents = new Intent(this,CallSmsActivity.class);
				MainScreenActivity.this.startActivity(callsmsIntents);
			break;
			
			
			case 2://进入程序管理
				LogManagement.i(this.TAG, "进入软件管理");
				Intent appmanagerIntent = new Intent(this,AppManagerActivity.class);
				MainScreenActivity.this.startActivity(appmanagerIntent);
			break;
			
			
			case 3://进入任务管理
				LogManagement.i(this.TAG, "进入进程管理");
				Intent TaskIntent = new Intent(this,TaskManagerActivity.class);
				MainScreenActivity.this.startActivity(TaskIntent);
			break;
			
			case 4://进入流量管理
				LogManagement.i(this.TAG, "进入流量管理");
				Intent TrafficIntent = new Intent(this,TrafficManagerActivity.class);
				MainScreenActivity.this.startActivity(TrafficIntent);
			break;
			
			
			case 5://进入杀毒界面（特征杀毒）
				LogManagement.i(this.TAG, "进入杀毒");
				Intent AntiVirutsIntent = new Intent(this,AntiVirutsActivity.class);
				MainScreenActivity.this.startActivity(AntiVirutsIntent);
			break;
			
			
			case 6://进入高级工具
				LogManagement.i(this.TAG, "进入高级工具");
				Intent AToolsintent = new Intent(this,AToolsActivity.class);
				this.startActivity(AToolsintent);
			break;
			
			
			case 7://进入系统优化
				LogManagement.i(this.TAG, "进入系统优化");
				Intent Cacheintent = new Intent(this,CacheInfoActivity.class);
				this.startActivity(Cacheintent);
			break;
			
			
			case 8: //进入设置中心
				LogManagement.i(this.TAG, "进入设置中心");
				Intent Settingintent = new Intent(this,SettinCenterActivity.class);
				this.startActivity(Settingintent);
			break;
		}
	}

	
}
