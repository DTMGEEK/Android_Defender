package com.example.androiddefprot.domain;


/**
 * 
* @Title: UpdateInfo.java 
* @Package com.example.androiddefprot.domain 
* @Description:app������Ϣdomain��
* @author lian_weijian@163.com   
* @version V1.0
 */
public class UpdateInfo {
	
	//�汾��
	private String version;
	//����
	private String description;
	//url
	private String apkurl;
	
	
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getApkurl() {
		return apkurl;
	}
	
	public void setApkurl(String apkurl) {
		this.apkurl = apkurl;
	}

}
