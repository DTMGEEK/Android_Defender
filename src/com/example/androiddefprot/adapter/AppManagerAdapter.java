package com.example.androiddefprot.adapter;

import java.util.List;

import com.example.androiddefprot.R;
import com.example.androiddefprot.domain.AppInfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
* @Title: AppManagerAdapter.java 
* @Package com.example.androiddefprot.adapter 
* @Description: Ӧ�ó����б�Adapter
* @author lian_weijian@163.com   
* @version V1.0
 */
public class AppManagerAdapter extends BaseAdapter {
	
	private static final String TAG = "AppManagerAdapter";
	private Context context;
	private List<AppInfo> appinfos = null;
	private static ImageView iv = null;  //��������һ����̬������Ԥ���������ڴ�ռ䡣�������Ż�ListView������  �ռ任ʱ��
	private static TextView tv = null;   //��������һ����̬������Ԥ���������ڴ�ռ䡣�������Ż�ListView������   �ռ任ʱ��
	

	
	public AppManagerAdapter(Context context, List<AppInfo> appinfos) {
		super();
		this.context = context;
		this.appinfos = appinfos;
	}
	

	@Override
	public int getCount() {
		
		return appinfos.size();
	}

	@Override
	public Object getItem(int position) {
		return appinfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	/**
	 * View convertView (ת��view���� ,��ʷview����Ļ���) convertview �����϶���ʱ�򱻻��յ���view����
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AppInfo appinfo = appinfos.get(position);
		View view = null;
		if(convertView == null){
			//LogManagement.i(TAG,"ͨ����Դ�ļ� ����view����");
			 view = View.inflate(context, R.layout.app_item, null);  //��δ����൱�ķ�ʱ�� 
		}else{
			//LogManagement.i(TAG,"ʹ����ʷ����view����");
			view = convertView;
		}
		 this.tv = (TextView) view.findViewById(R.id.app_name_tv);
		 this.iv = (ImageView) view.findViewById(R.id.app_name_iv);
		iv.setImageDrawable(appinfo.getIcon());
		tv.setText(appinfo.getAppname());
		return view;
	}

}
