package com.example.androiddefprot.domain;

import android.graphics.drawable.Drawable;

/**
 * 
* @Title: TaskInfo.java 
* @Package com.example.androiddefprot.domain 
* @Description: �߳���ϢDomain�ࣨʵ�壩
* @author lian_weijian@163.com   
* @version V1.0
 */
public class TaskInfo {

	private String appname;  //app����
	private int pid;         //�߳�id
 	private Drawable appicon; //appͼ��
	private String packagename;  //����
	private boolean isCheck;    //�Ƿ�ѡ��
	private int memorysize;    //ռ���ڴ��С
	private boolean isSystemapp; //�ǲ���ϵͳԤװӦ�� 
	
	
	public String getAppname() {
		return appname;
	}
	
	public void setAppname(String appname) {
		this.appname = appname;
	}
	
	public int getPid() {
		return pid;
	}
	
	public void setPid(int pid) {
		this.pid = pid;
	}
	
	public Drawable getAppicon() {
		return appicon;
	}
	
	public void setAppicon(Drawable appicon) {
		this.appicon = appicon;
	}
	
	public String getPackagename() {
		return packagename;
	}
	
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	
	public boolean isCheck() {
		return isCheck;
	}
	
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	
	public int getMemorysize() {
		return memorysize;
	}
	
	public void setMemorysize(int memorysize) {
		this.memorysize = memorysize;
	}

	public boolean isSystemapp() {
		return isSystemapp;
	}

	public void setSystemapp(boolean isSystemapp) {
		this.isSystemapp = isSystemapp;
	}
	
	
	

}
