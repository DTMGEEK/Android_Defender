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
* @Description: ������Activity
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
				et.setHint("�������ı�");
				Dialog dialog = new AlertDialog.Builder(MainScreenActivity.this).setTitle("����")
						.setMessage("������Ҫ���µ�����").setView(et)
						.setPositiveButton("ȷ��", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								String name = et.getText().toString().trim();
								if("".equals(name)){
									Toast.makeText(MainScreenActivity.this, "�޸ĵ����ֲ���Ϊ��", Toast.LENGTH_SHORT).show();
								}else{
									MainScreenActivity.this.editor.putString("lostname", name);
									MainScreenActivity.this.editor.commit();
									TextView tv = (TextView) view.findViewById(R.id.tv_mainscreen);
									tv.setText(name);
								}
							}
						}).setNegativeButton("ȡ��", new OnClickListener() {
							
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
	 * ��gridview����Ŀ�������ʱ�� ��Ӧ�Ļص� parent :��girdview view : ��ǰ���������Ŀ Linearlayout
	 * position : �����Ŀ��Ӧ��λ�� id : ������к�
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		LogManagement.i(this.TAG, "�����λ��" + position);
		switch(position){
			
			case 0: //�ֻ���������
				LogManagement.i(this.TAG, "�����ֻ�����");
				Intent intent = new Intent(this,LostProtectedActivity.class);
				this.startActivity(intent);
			break;
			
			
			case 1: //����ͨѶ��ʿ
				LogManagement.i(this.TAG, "����ͨѶ��ʿ");
				Intent callsmsIntents = new Intent(this,CallSmsActivity.class);
				MainScreenActivity.this.startActivity(callsmsIntents);
			break;
			
			
			case 2://����������
				LogManagement.i(this.TAG, "�����������");
				Intent appmanagerIntent = new Intent(this,AppManagerActivity.class);
				MainScreenActivity.this.startActivity(appmanagerIntent);
			break;
			
			
			case 3://�����������
				LogManagement.i(this.TAG, "������̹���");
				Intent TaskIntent = new Intent(this,TaskManagerActivity.class);
				MainScreenActivity.this.startActivity(TaskIntent);
			break;
			
			case 4://������������
				LogManagement.i(this.TAG, "������������");
				Intent TrafficIntent = new Intent(this,TrafficManagerActivity.class);
				MainScreenActivity.this.startActivity(TrafficIntent);
			break;
			
			
			case 5://����ɱ�����棨����ɱ����
				LogManagement.i(this.TAG, "����ɱ��");
				Intent AntiVirutsIntent = new Intent(this,AntiVirutsActivity.class);
				MainScreenActivity.this.startActivity(AntiVirutsIntent);
			break;
			
			
			case 6://����߼�����
				LogManagement.i(this.TAG, "����߼�����");
				Intent AToolsintent = new Intent(this,AToolsActivity.class);
				this.startActivity(AToolsintent);
			break;
			
			
			case 7://����ϵͳ�Ż�
				LogManagement.i(this.TAG, "����ϵͳ�Ż�");
				Intent Cacheintent = new Intent(this,CacheInfoActivity.class);
				this.startActivity(Cacheintent);
			break;
			
			
			case 8: //������������
				LogManagement.i(this.TAG, "������������");
				Intent Settingintent = new Intent(this,SettinCenterActivity.class);
				this.startActivity(Settingintent);
			break;
		}
	}

	
}
