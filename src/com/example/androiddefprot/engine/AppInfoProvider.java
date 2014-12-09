package com.example.androiddefprot.engine;

import java.util.ArrayList;
import java.util.List;

import com.example.androiddefprot.domain.AppInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;


/**
 * 
* @Title: AppInfoProvider.java 
* @Package com.example.androiddefprot.engine 
* @Description: ϵͳӦ����Ϣ�ṩ��
* @author lian_weijian@163.com   
* @version V1.0
 */
public class AppInfoProvider {

	private Context context;
	private PackageManager manager;
	
	
	public AppInfoProvider(Context context) {
		super();
		this.context = context;
		manager = context.getPackageManager();
	}
	
	
	/**
	 * �õ����а�װ�˵������Ϣ
	 * @return Ӧ�ó���ļ���
	 */
	public List<AppInfo> getAllApp(){
		
		List<AppInfo> appInfos = new ArrayList<AppInfo>();  //����appinfo��Ϣ
		List<PackageInfo> packInfos = this.manager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES); //��ȡ���а�װ�˵������Ϣ
		for(PackageInfo info:packInfos){
			AppInfo appinfo = new AppInfo();
			String packName = info.packageName;
			
			appinfo.setPackname(packName);
			
			ApplicationInfo applicationinfo = info.applicationInfo;
			Drawable icon = applicationinfo.loadIcon(manager);		
			appinfo.setIcon(icon);
			
			appinfo.setAppname(applicationinfo.loadLabel(manager).toString());  //��������
			
			appinfo.setSystemApp(filterApp(applicationinfo));
			
			appInfos.add(appinfo);
		}
		return appInfos;
	}
	
	
	
	/**
	 * ͨ�����Ϸ��������Եõ��ֻ��а�װ������Ӧ�ó��򣬼Ȱ������ֶ���װ��apk������Ϣ��Ҳ������ϵͳԤװ��Ӧ���������Ϣ��Ҫ���������������ʹ�����·���:
     *	a.��packageInfoList��ȡ��packageInfo����ͨ��packageInfo.applicationInfo��ȡapplicationInfo��
     *	b.�ж�(applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)��ֵ����ֵ����0ʱ����ʾ��ȡ��Ӧ��ΪϵͳԤװ��Ӧ�ã���֮��Ϊ�ֶ���װ��Ӧ�á�
	 * �ж���ϵͳӦ�û��ǵ�����Ӧ��
	 * @param appinfo
	 * @return
	 */
	public boolean filterApp(ApplicationInfo appinfo){
		
		if((appinfo.flags & appinfo.FLAG_SYSTEM) <= 0){
			return false;
		}
		return true;
	}
	
	
}
