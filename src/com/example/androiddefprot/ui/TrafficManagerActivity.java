package com.example.androiddefprot.ui;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.example.androiddefprot.R;
import com.example.androiddefprot.util.ImageUtil;
import com.example.androiddefprot.util.TextFormater;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
* @Title: TrafficManagerActivity.java 
* @Package com.example.androiddefprot.ui 
* @Description: ��������Activity
* @author lian_weijian@163.com   
* @version V1.0
 */
public class TrafficManagerActivity extends Activity {
	
	private TextView traffic_manager_tv_total = null;
	private TextView traffic_manager_tv_wifi_total = null;
	private ImageView handle = null;
	private ListView content = null;
	private slidingDrawerAdapter adapter;
	private PackageManager pm = null;
	private Timer timer = null;
	private TimerTask task = null;
	
	//���߳�֪ͨ�б������Ϣ
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			adapter.notifyDataSetChanged();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.traffic_manager);
		this.pm = this.getPackageManager();
		this.traffic_manager_tv_total = (TextView) this.findViewById(R.id.traffic_manager_tv_total);
		this.traffic_manager_tv_wifi_total = (TextView) this.findViewById(R.id.traffic_manager_tv_total);
		this.handle = (ImageView) this.findViewById(R.id.handle);
		this.content = (ListView) this.findViewById(R.id.content);
		content.addHeaderView(View.inflate(TrafficManagerActivity.this, R.layout.traffic_title, null));
		setTotalDataInfo();
		
		this.adapter = new slidingDrawerAdapter();
		System.out.print(this.adapter);
		this.content.setAdapter(adapter);
		
	}

	/**
	 * ������������Ϣ
	 */
	private void setTotalDataInfo() {
		//�ֻ�ͨ��2g/3g��������
		long mobileRxBytes = TrafficStats.getMobileRxBytes();
		long mobileTxBytes = TrafficStats.getMobileTxBytes();
	    long mobileTotal = mobileRxBytes + mobileTxBytes;
	    
	    String mobileTraffic = TextFormater.getDataSize(mobileTotal);
	    traffic_manager_tv_total.setText(mobileTraffic);
	    
	    //����ֻ���������
	    long totalRx = TrafficStats.getTotalRxBytes();
	    long totalTx = TrafficStats.getTotalTxBytes();
	    long total = totalRx + totalTx;
	    
	    
	    String wifiTraffic = TextFormater.getDataSize(total - mobileTotal);
	    traffic_manager_tv_wifi_total.setText(wifiTraffic);
	  
	}
	
	
	/**
	 * slidingDrawer ����������
	 * @author jake
	 *
	 */
	private class slidingDrawerAdapter extends BaseAdapter{
		List<ResolveInfo> ResolveInfos = null;
		
		public slidingDrawerAdapter() {
			super();
			PackageManager pm = TrafficManagerActivity.this.getPackageManager();
			Intent packageIntent = new Intent();
			packageIntent.setAction("android.intent.action.MAIN");
			packageIntent.addCategory("android.intent.category.LAUNCHER");
			ResolveInfos = pm.queryIntentActivities(packageIntent, PackageManager.MATCH_DEFAULT_ONLY);
			System.out.print(ResolveInfos.size());
		}

		@Override
		public int getCount() {
			return ResolveInfos.size();
		}

		@Override
		public Object getItem(int position) {
			
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			ViewHolder holder = new ViewHolder();
			//�ж�convertView�Ƿ�������ã����Լ���new�Ĵ������������
			if(convertView == null){
				view = View.inflate(getApplicationContext(), R.layout.traffic_item, null);
			}else{
				view = convertView;
			}
			holder.traffic_manager_icon = (ImageView) view.findViewById(R.id.traffic_item_icon);
			holder.traffic_item_tv_name = (TextView) view.findViewById(R.id.traffic_item_tv_name);
			holder.traffic_item_tv_upload = (TextView) view.findViewById(R.id.traffic_item_tv_upload);
			holder.traffic_item_tv_download = (TextView) view.findViewById(R.id.traffic_item_tv_download);
			ResolveInfo info = ResolveInfos.get(position);  //���positionλ�õ���Ŀ��Ϣ
			
			String appname = info.loadLabel(pm).toString(); //�õ�app������
			holder.traffic_item_tv_name.setText("     "+appname);
			
			
		
			BitmapDrawable appicon = (BitmapDrawable) info.loadIcon(pm); //�õ�app��ͼ��
        	Bitmap resizeicon = ImageUtil.getResizedBitmap((BitmapDrawable)appicon, TrafficManagerActivity.this);//ת�����涨��С��ͼƬ
        	holder.traffic_manager_icon.setImageBitmap(resizeicon);   
			String packagename = info.activityInfo.packageName;      //�õ�����
			PackageInfo packageinfo;
			try {
				packageinfo = pm.getPackageInfo(packagename, 0);     //
				int uid = packageinfo.applicationInfo.uid;
				
				long uidRxBytes = TrafficStats.getUidRxBytes(uid);  //�õ�ĳ��uid��ĳ��app�ĵ��ϴ�������Ϣ
				long uidTxBytes = TrafficStats.getUidTxBytes(uid);  //�õ�ĳ��uid��ĳ��app�ĵ�����������Ϣ
				if(uidTxBytes == -1){
					uidTxBytes = 0;
				}
				if(uidRxBytes == -1){
					uidRxBytes = 0;
				}
				
				holder.traffic_item_tv_upload.setText(TextFormater.getKBDataSize(uidTxBytes));
				holder.traffic_item_tv_download.setText(TextFormater.getKBDataSize(uidRxBytes));
				
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return view;
		}
	}

	
	
	private class ViewHolder{
		ImageView traffic_manager_icon = null;
		TextView traffic_item_tv_name = null;
		TextView traffic_item_tv_upload = null;
		TextView traffic_item_tv_download = null;
	}
	
	

	/**
	 * ��������û��ɼ���ʱ��ִ�����·�����
	 */
	@Override
	protected void onStart() {
		super.onStart();
		TrafficManagerActivity.this.timer = new Timer();
		TrafficManagerActivity.this.task = new TimerTask(){
			
			@Override
			public void run() {
				Message msg = Message.obtain();
				handler.sendMessage(msg);
			}
		};
		this.timer.schedule(task, 1000, 2000);
	}
	
	
	/**
	 *������� �û����ɼ���ʱ��ִ�����·��� 
	 */
	@Override
	protected void onStop() {
		super.onStop();
		this.timer.cancel();
		this.timer = null;
		this.task = null;
	}
	
	
	
}
