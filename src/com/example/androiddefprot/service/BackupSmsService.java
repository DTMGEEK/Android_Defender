package com.example.androiddefprot.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import com.example.androiddefprot.R;
import com.example.androiddefprot.domain.SmsInfo;
import com.example.androiddefprot.engine.SmsInfoService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Xml;
import android.widget.Toast;

/**
 * 
* @Title: BackupSmsService.java 
* @Package com.example.androiddefprot.service 
* @Description: ���ű��ݷ����࣬�ں�̨����һ���������ڷ���������һ���̣߳�������Ϣ��
* @author lian_weijian@163.com   
* @version V1.0
 */
public class BackupSmsService extends Service {

	private SmsInfoService smsinfoService = null;
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		smsinfoService = new SmsInfoService(BackupSmsService.this);
		new Thread(){
			@Override
			public void run() {
				List<SmsInfo> infos = smsinfoService.getSmsInfos();
				try {
					//File file = new File("/sdcard/smsbackup.xml");
					File file = new File(getApplicationContext().getResources().getString(R.string.sms_backup_path)); //���������Ϣ���ļ�
					XmlSerializer xmlSerializer = Xml.newSerializer();   //xml���л���
					FileOutputStream os = new FileOutputStream(file);       //�����ļ������
				
					//�Ѷ��ŵ���Ϣ�����ݶ�д�뵽xml�ļ���
					xmlSerializer.setOutput(os, "utf-8");
					xmlSerializer.startDocument("utf-8", true);
					xmlSerializer.startTag(null, "smss");
					xmlSerializer.startTag(null, "count");
					xmlSerializer.text(infos.size()+"");
					xmlSerializer.endTag(null, "count");
					
					for(SmsInfo info : infos){
						xmlSerializer.startTag(null, "sms");
						
						xmlSerializer.startTag(null, "id");
						xmlSerializer.text(info.getId());
						xmlSerializer.endTag(null, "id");
						
						
						
						xmlSerializer.startTag(null, "address");
						xmlSerializer.text(info.getAddress());
						xmlSerializer.endTag(null, "address");
						
						
						xmlSerializer.startTag(null, "date");
						xmlSerializer.text(info.getDate());
						xmlSerializer.endTag(null, "date");
						
						
						xmlSerializer.startTag(null, "type");
						xmlSerializer.text(info.getType()+"");
						xmlSerializer.endTag(null, "type");
						
						
						
						xmlSerializer.startTag(null, "body");
						xmlSerializer.text(info.getBody());
						xmlSerializer.endTag(null, "body");
						
						xmlSerializer.endTag(null, "sms");
					}
					//�Ѷ��ŵ���Ϣ�����ݶ�д�뵽xml�ļ���
					xmlSerializer.endTag(null, "smss");
					xmlSerializer.endDocument();
					//���ļ�������������д��ȥ
					os.flush();
					os.close();
					//�Ѷ��ŵ���Ϣ�����ݶ�д�뵽xml�ļ���
					
					//��Ϊ���һ������û����Ϣ��������Ҫ�յ�����һ����Ϣ����
					Looper.prepare();
					Toast.makeText(getApplicationContext(), "�������", Toast.LENGTH_LONG).show();
					Looper.loop();
				} catch (Exception e) {
					e.printStackTrace();
					Looper.prepare();
					Toast.makeText(getApplicationContext(), "����ʧ��", Toast.LENGTH_LONG).show();
					Looper.loop();
				}
			}
			
		}.start();
	}
	
	

}
