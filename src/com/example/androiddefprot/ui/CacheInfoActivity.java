package com.example.androiddefprot.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.example.androiddefprot.R;
import com.example.androiddefprot.domain.CacheInfo;
import com.example.androiddefprot.util.TextFormater;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
* 
* @Title: CacheInfoActivity.java 
* @Package com.example.androiddefprot.ui 
* @Description: ���App������Ϣ��Activity
* @author lian_weijian@163.com   
* @version V1.0
 */
public class CacheInfoActivity extends Activity {
	
	private ListView cache_info_lv = null;
	private PackageManager pm = null;
	private CacheInfoAdapter adapter = null;
	private Map<String,CacheInfo> maps = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.cache_info_layout);
		this.maps = new HashMap<String,CacheInfo>();
	
		this.cache_info_lv = (ListView) this.findViewById(R.id.cache_info_lv);
		this.pm = this.getPackageManager();
		
		List<PackageInfo> infos = this.pm
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for(PackageInfo info : infos){
			String name = info.applicationInfo.loadLabel(pm).toString();
			String packageName = info.packageName;
			CacheInfo cacheInfo = new CacheInfo();
			cacheInfo.setName(name);
			cacheInfo.setPackagename(packageName);
			maps.put(packageName, cacheInfo);
			

		    this.getAppSize(packageName);
		}
		
		this.adapter = new CacheInfoAdapter();
		this.cache_info_lv.setAdapter(adapter);
		

		this.cache_info_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/**
				 * Android2.3��settings������Ǹ�Ӧ�õ���ϸ����
				 * �������ֲ���һ��Android4.1�ģ�Ҳ������д�ģ�����Ӧ����2.3֮�󣬶�������д���ˣ�
				 * ������ֻ�ǲ²⣬��λ�пյĿ���ȥ����Android Settings�Ĵ��뿴һ��
				 * �����Ϳ������ɶ���汾��������
				 * <intent-filter> 
				 *  
				 * <category android:name="android.intent.category.DEFAULT">  
				 * <data android:scheme="package"> 
				 * </data></category></action></intent-filter>
				 */
				 
				/**
				 * Android2.2��settings������Ǹ�Ӧ�õ���ϸ����
				 * ������汾���򿪵Ļ�����Ҫ�Ӷ�һ��Ѱ������ý�ȥ��
				 * intent.putExtra("pkg", packageName);
				 * <intent-filter> 
				 *  
				 * <category android:name="android.intent.category.DEFAULT"> 
				 * <category android:name="android.intent.category.VOICE_LAUNCH">
				 * </category></category></action></intent-filter>
				 */
				 
				Intent intent = new Intent();
				intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
				intent.addCategory("android.intent.category.DEFAULT");
				Set<String> set = maps.keySet();
				String[] pArray = new String[set.size()]; 
				set.toArray(pArray);
				intent.setData(Uri.parse("package:" + pArray[position]));
				startActivity(intent);
			}
		});
		
	}
	
	
	/**
	 * ���app�Ļ�����Ϣ
	 * @param packageName
	 * ���ݰ�����ȡӦ�ó���������Ϣ ע��: ���������һ���첽�ķ��� ��������Ҫ��һ��ʱ����ܻ�ȡ��.
	 * 
	 * @param packname
	 */
	private void getAppSize(final String packageName)  {
		try{
			 Method method = PackageManager.class.getMethod("getPackageSizeInfo",
			            new Class[] { String.class, IPackageStatsObserver.class });
			 
			    // ���� getPackageSizeInfo ��������Ҫ����������1����Ҫ����Ӧ�ð�����2���ص�
			method.invoke(this.pm, new Object[]{packageName,
						new IPackageStatsObserver.Stub() {
							
							@Override
							public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
									throws RemoteException {
								 //Looper.prepare();
				                 // ��pStats����ȡ������������
								long cachesize = pStats.cacheSize;
								long codesize = pStats.codeSize;
								long datasize = pStats.dataSize;
								CacheInfo cacheinfo = maps.get(packageName);
								cacheinfo.setCache_size(TextFormater.getDataSize(cachesize));
								cacheinfo.setCode_size(TextFormater.getDataSize(codesize));
								cacheinfo.setData_size(TextFormater.getDataSize(datasize));
								 // ����һ����Ϣ���У�����Toast
			                   // Looper.loop();
							}
						}
			});
					
		}catch(Exception e){
			e.printStackTrace();
		}
	}



	/**
	 * app���ݻ�������������
	 * @author jake
	 *
	 */
	private class CacheInfoAdapter extends BaseAdapter{

		private Set<Entry<String,CacheInfo>> sets;
		private List<CacheInfo> cacheInfos;

		
		public CacheInfoAdapter() {
			super();
			sets = maps.entrySet();
			this.cacheInfos = new ArrayList<CacheInfo>();
			for(Entry<String,CacheInfo> entry :sets){
				cacheInfos.add(entry.getValue());
			}
		}

		@Override
		public int getCount() {
			return cacheInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return cacheInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			//�Ż�
			if(convertView == null){
				view = View.inflate(CacheInfoActivity.this, R.layout.cache_info_item, null);
			}else{
				view = convertView;
			}
			TextView cache_info_item_tv_name = (TextView) view.findViewById(R.id.cache_info_item_tv_name);
			TextView cache_info_item_tv_code_size = (TextView) view.findViewById(R.id.cache_info_item_tv_code_size);
			TextView cache_info_item_tv_cache_size = (TextView) view.findViewById(R.id.cache_info_item_tv_cache_size);
			TextView cache_info_item_tv_data_size = (TextView) view.findViewById(R.id.cache_info_item_tv_data_size);
			CacheInfo cacheinfo = cacheInfos.get(position);
			cache_info_item_tv_name.setText(cacheinfo.getName());
			cache_info_item_tv_code_size.setText(cacheinfo.getCode_size());
			cache_info_item_tv_cache_size.setText(cacheinfo.getCache_size());
			cache_info_item_tv_data_size.setText(cacheinfo.getData_size());
			return view;
		}
	}
	

}
