package com.example.androiddefprot.ui;


import java.io.File;

import com.example.androiddefprot.R;
import com.example.androiddefprot.engine.DownLoadFileTask;
import com.example.androiddefprot.engine.SmsInfoService;
import com.example.androiddefprot.logmanagement.LogManagement;
import com.example.androiddefprot.service.AddressServiec;
import com.example.androiddefprot.service.BackupSmsService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 
* @Title: AToolsActivity.java 
* @Package com.example.androiddefprot.ui 
* @Description: �߼�����Activity
* @author lian_weijian@163.com   
* @version V1.0
 */

public class AToolsActivity extends Activity implements OnClickListener {
	
	private static final String TAG = "AToolsActivity";
	private static final int  SUCCESS = 1;
	private static final int  FAIL = 0;
	private TextView tv_atool_query = null;
	private TextView tv_atools_address = null;
	private CheckBox atools_cb_address = null;
	private TextView atools_tv_select_bg = null;
	private TextView atools_tv_change_location = null;
	private Intent serviceIntent = null;
	private SharedPreferences sp = null;
	private ProgressDialog pd = null;
	private TextView atools_tv_change_sms_backup = null;
	private TextView atools_tv_change_sms_restore = null;
	private TextView atools_tv_change_contacts_backup = null;
	private TextView atools_tv_change_contacts_restore = null;
	private TextView atools_tv_applock = null;
	private TextView atools_tv_common_num = null;
	
	
	
	//���߳����غ�������ص����ݿ�
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch(msg.what){
				
				case SUCCESS:
					Toast.makeText(AToolsActivity.this.getApplicationContext(), "���ݿ����سɹ�", Toast.LENGTH_SHORT).show();
				break;
				
				case FAIL:
					Toast.makeText(AToolsActivity.this.getApplicationContext(), "�������ݿ�ʧ��", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.atools);
		this.tv_atool_query = (TextView)this.findViewById(R.id.atools_tv_query);
		this.tv_atool_query.setOnClickListener(this);
		this.tv_atools_address = (TextView) this.findViewById(R.id.tv_atools_address);
		this.atools_cb_address = (CheckBox) this.findViewById(R.id.atools_cb_address);
		this.atools_tv_select_bg = (TextView) this.findViewById(R.id.atools_tv_select_bg);
		this.atools_tv_change_location = (TextView) this.findViewById(R.id.atools_tv_change_location);
		this.atools_tv_change_sms_backup = (TextView) this.findViewById(R.id.atools_tv_change_sms_backup);
		this.atools_tv_change_sms_restore = (TextView) this.findViewById(R.id.atools_tv_change_sms_restore);
		this.atools_tv_change_contacts_backup = (TextView) this.findViewById(R.id.atools_tv_change_contacts_backup);
		this.atools_tv_change_contacts_restore = (TextView) this.findViewById(R.id.atools_tv_change_contacts_restore);
		this.atools_tv_applock = (TextView) this.findViewById(R.id.atools_tv_applock);
		this.atools_tv_common_num = (TextView) this.findViewById(R.id.atools_tv_common_num);
		this.serviceIntent = new Intent(this,AddressServiec.class);
		this.sp = this.getSharedPreferences("config", Context.MODE_PRIVATE);
		this.atools_cb_address.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	    
			//����ѡ��ѡ�񱻸ı�ʱ�򼤻�
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					AToolsActivity.this.startService(serviceIntent);
					AToolsActivity.this.tv_atools_address.setTextColor(Color.WHITE);
					AToolsActivity.this.tv_atools_address.setText(AToolsActivity.this.getResources().getString(R.string.atool_tv_text_service));
				}else{
					AToolsActivity.this.stopService(serviceIntent);
					AToolsActivity.this.tv_atools_address.setTextColor(Color.RED);
					AToolsActivity.this.tv_atools_address.setText(AToolsActivity.this.getResources().getString(R.string.atool_tv_text_noservice));
				}
			}
		});
		
		tv_atool_query.setOnClickListener(this);  //���õ����ѯ�ļ���
		this.atools_tv_select_bg.setOnClickListener(this); //����ѡ�񱳾��ļ���
		this.atools_tv_change_location.setOnClickListener(this); 
		this.atools_tv_change_sms_backup.setOnClickListener(this);
		this.atools_tv_change_sms_restore.setOnClickListener(this);
		this.atools_tv_change_contacts_backup.setOnClickListener(this);
		this.atools_tv_change_contacts_restore.setOnClickListener(this);
		this.atools_tv_applock.setOnClickListener(this);
		this.atools_tv_common_num.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
		
			case R.id.atools_tv_query:       //���û������ѯ��������ص�textview��ʱ�򼤻�
				this.pd = new ProgressDialog(this);
				
				if(isDBExit()){      //�ж����ݿ��Ƿ����
					LogManagement.i(this.TAG, "��������ѯ����");
					Intent intent = new Intent(AToolsActivity.this,QueryNumberActivity.class);
					this.startActivity(intent);
					overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
				}else{                //���ݿⲻ���ھ����ӷ���������
					LogManagement.i(this.TAG, "�������ݿ�");
					this.pd.setMessage("�����������ݿ�");
					this.pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//������ʾ�����
					this.pd.show();
					final String path = this.getResources().getString(R.string.dburl);
					final String filepath = "/sdcard/address.db";
					new Thread(){ //����һ���µ��̣߳��������ݿ�
						@Override
						public void run() {
							super.run();
							try {
								DownLoadFileTask.getFile(path, filepath, pd);
								Message msg = new Message();
								msg.what = SUCCESS;
								pd.dismiss();
								handler.sendMessage(msg);
							} catch (Exception e) {
								Message msg = new Message();
								msg.what = FAIL;
								pd.dismiss();
								handler.sendMessage(msg);
								e.printStackTrace();
							}
						}
					}.start();
				}
			break;
			
			
			case R.id.atools_tv_select_bg:     //ѡ����ʾ��������صı���
				AlertDialog.Builder builder = new Builder(this);
				builder.setTitle("��������ʾ���");
				String[] items = new String[]{
					"��͸��","������","ƻ����"	
				};
				builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SharedPreferences.Editor editor = sp.edit();
						editor.putInt("background", which);
						editor.commit();
					}
				});
				builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});
				builder.create().show();
			break;

			
			
		   case R.id.atools_tv_change_location:       //�ı���ʾ��������ص���ʾλ��
			   Intent intent = new Intent(this,DragViewActivity.class);
			   AToolsActivity.this.startActivity(intent);
		   break;
		   
		   
		   case R.id.atools_tv_change_sms_backup:     //���ݶ���
			   Intent smsbackupIntent = new Intent(this,BackupSmsService.class);
			   AToolsActivity.this.startService(smsbackupIntent);
		   break;
		   
		   
		   case R.id.atools_tv_change_sms_restore:    //���Ż�ԭ
			   final ProgressDialog pd = new ProgressDialog(AToolsActivity.this);  //��̬����һ��ProgressDialog
			   pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);     //����ProgressDialog����ʾ���
			   pd.setMessage("���Ż�ԭ");    //���ñ���
			   pd.show();                 //��ʾProgressDialog
			   final String sms_backup_path = this.getResources().getString(R.string.sms_backup_path);
			   final SmsInfoService service = new SmsInfoService(this);
			   final String restore_success = this.getResources().getString(R.string.sms_store_success);
			   final String restore_fail = this.getResources().getString(R.string.sms_store_fail);
			  
			   new Thread(){
					@Override
					public void run() {
						super.run();
						try {
							service.restoreSms(sms_backup_path, pd);//�ָ�����
							pd.dismiss();                           //ȡ��ProgressDialog
							
							//��Ϊ��һ���µ��߳�������û����Ϣ���еĵģ�����Ҫ�Զ�һ����Ϣ���вſ���ʹ��Toast
							Looper.prepare();
							Toast.makeText(AToolsActivity.this, restore_success, Toast.LENGTH_LONG).show();
							Looper.loop();
						} catch (Exception e) {
							e.printStackTrace();
							Looper.prepare();
							Toast.makeText(AToolsActivity.this, restore_fail, Toast.LENGTH_LONG).show();
							Looper.loop();
						}
					}
			   }.start();
		   break;
		   

		   case R.id.atools_tv_change_contacts_backup:  //��ϵ�˱���
		   break;
		   
		   
		   case R.id.atools_tv_change_contacts_restore:    //��ϵ�˻�ԭ
		   break;
		   
		   
		   
		   case R.id.atools_tv_applock: //������
			   Intent applockIntent = new Intent(AToolsActivity.this,AppLockActivity.class);
			   AToolsActivity.this.startActivity(applockIntent);
		   break;
		   
		   
		   case R.id.atools_tv_common_num: //���ú����ѯ
			   Intent commonNumIntent = new Intent(AToolsActivity.this,CommonNumberActivity.class);
			   AToolsActivity.this.startActivity(commonNumIntent);
		   break;
	

		}
	}


	//�жϱ��غ�����������ݿ��Ƿ����
	public boolean isDBExit(){
		File file = new File("/sdcard/address.db");
		boolean flag = file.exists();
		System.out.println(flag);
		if(flag){
			return true;
		}else{
			return false;
		}
	}

	
}
