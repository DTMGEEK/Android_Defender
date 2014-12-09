package com.example.androiddefprot.ui;

import java.util.List;






import com.example.androiddefprot.R;
import com.example.androiddefprot.db.dao.AppLockDao;
import com.example.androiddefprot.domain.AppInfo;
import com.example.androiddefprot.engine.AppInfoProvider;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
* @Title: AppLockActivity.java 
* @Package com.example.androiddefprot.ui 
* @Description: ������Activity 
* @author lian_weijian@163.com   
* @version V1.0
 */
public class AppLockActivity extends Activity {
	
	private AppInfoProvider provider = null; //����app��Ϣ�ṩ��
	private List<AppInfo> AppInfos = null;   //��������app����Ϣ
	private AppLockerAdapter adapter = null; 
	private AppLockDao dao = null;           //app���ݿ������
	private ListView lv = null;				 
	private LinearLayout ll_app_manager_loading = null;
	private List<String> lockAppInfos = null;     //���汻������app��Ϣ
	private ImageView lock_app_iv = null;
	private View view = null;
	
	
	//����һ�����̻߳�ȡapp����Ϣ
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			adapter = new AppLockerAdapter();
			AppLockActivity.this.ll_app_manager_loading.setVisibility(View.INVISIBLE);
			AppLockActivity.this.lv.setAdapter(adapter);
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.app_lock);
		this.dao = new AppLockDao(AppLockActivity.this);
		this.lv = (ListView) this.findViewById(R.id.app_lock_lv);
		this.ll_app_manager_loading = (LinearLayout) this.findViewById(R.id.ll_app_manager_loading);
		
		//��ʼ��UI
		this.initUI();
		//�õ����е�app����
		lockAppInfos = this.dao.findAll();
		
		//����ListView��ÿһ����ļ���
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//���ö���
				TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0.0f,
															   Animation.RELATIVE_TO_SELF,0.5f,
															   Animation.RELATIVE_TO_SELF,0.0f, 
															   Animation.RELATIVE_TO_SELF,0.5f);
				ta.setDuration(200);
				view.setAnimation(ta);
				ta.start();
				//�õ�����
				String packagename = AppInfos.get(position).getPackname();
				ImageView iv = (ImageView) view.findViewById(R.id.lock_app_iv);
				if(AppLockActivity.this.dao.find(packagename)){
					//AppLockActivity.this.dao.delete(packagename);
					
					AppLockActivity.this
							.getContentResolver()
							.delete(Uri
									.parse("content://com.text.androiddefprot.provider/delete"),
									null, new String[] { packagename });
					lockAppInfos.remove(packagename);
					iv.setImageResource(R.drawable.unlock);
				}else{
					//AppLockActivity.this.dao.add(packagename);
					ContentValues values = new ContentValues();
					values.put("packagename", packagename);
					AppLockActivity.this
							.getContentResolver()
							.insert(Uri
									.parse("content://com.text.androiddefprot.provider/insert"),
									values);
					iv.setImageResource(R.drawable.lock);
				}
			}
		});
	}
	
	
	/**
	 * ��ʼ��UI
	 */
	private void initUI(){
		new Thread(){
			@Override
			public void run() {
				super.run();
				AppLockActivity.this.ll_app_manager_loading.setVisibility(View.VISIBLE);
				AppInfos = new AppInfoProvider(AppLockActivity.this).getAllApp();
				AppLockActivity.this.handler.sendEmptyMessage(0);
			}
		}.start();
	}
	
	
	/**
	 * ����������������
	 * @author jake
	 *
	 */
	private class AppLockerAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return AppInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return AppInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if(convertView == null){
				view = View.inflate(AppLockActivity.this, R.layout.lock_app_item, null);
			}else{
				view = convertView;
			}
			AppInfo appinfo = AppInfos.get(position);
			ImageView lock_app_icon = (ImageView) view.findViewById(R.id.lock_app_icon);
			TextView lock_app_tv_name = (TextView) view.findViewById(R.id.lock_app_tv_name);
			TextView lock_app_tv_packname = (TextView) view.findViewById(R.id.lock_app_tv_packname);
			ImageView lock_app_iv = (ImageView) view.findViewById(R.id.lock_app_iv);
			
			lock_app_tv_packname.setText(appinfo.getPackname());
			lock_app_icon.setImageDrawable(appinfo.getIcon());
			lock_app_tv_name.setText(appinfo.getAppname());
			if(dao.find(appinfo.getPackname())){
				lock_app_iv.setImageResource(R.drawable.lock);
			}else{
				lock_app_iv.setImageResource(R.drawable.unlock);
			}
			return view;
		}
		
	}
	
	
	
}
