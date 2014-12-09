package com.example.androiddefprot.domain;

import android.graphics.drawable.Drawable;

/**
 * 
* @Title: AppInfo.java 
* @Package com.example.androiddefprot.domain 
* @Description: Ӧ����Ϣ�ģ�ʵ�壩��
* @author lian_weijian@163.com   
* @version V1.0
 */
public class AppInfo {

	private Drawable icon;   //Ӧ��ͼ��
	private String appname;  //Ӧ������
	private String packname; //����
	private boolean isSystemApp; //�ǲ���ϵͳӦ��
	
	public Drawable getIcon() {
		return icon;
	}
	
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	
	public String getAppname() {
		return appname;
	}
	
	public void setAppname(String appname) {
		this.appname = appname;
	}
	
	public String getPackname() {
		return packname;
	}
	
	public void setPackname(String packname) {
		this.packname = packname;
	}
	
	public boolean isSystemApp() {
		return isSystemApp;
	}
	
	public void setSystemApp(boolean isSystemApp) {
		this.isSystemApp = isSystemApp;
	}
	
	
}
