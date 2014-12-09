package com.example.androiddefprot.ui;

import java.util.ArrayList;
import java.util.List;

import com.example.androiddefprot.MyApplication;
import com.example.androiddefprot.R;
import com.example.androiddefprot.domain.TaskInfo;
import com.example.androiddefprot.engine.AppInfoProvider;
import com.example.androiddefprot.engine.TaskInfoProvider;
import com.example.androiddefprot.ui.stub.MyToast;
import com.example.androiddefprot.util.TextFormater;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



/**
 * 
* @Title: TaskManagerActivity.java 
* @Package com.example.androiddefprot.ui 
* @Description: �̹߳���Activity
* @author lian_weijian@163.com   
* @version V1.0
 */
public class TaskManagerActivity extends Activity {

	private TextView task_manager_title_tv_count;
	private TextView task_manager_title_tv_availablememory;
	private ListView task_manager_lv;
	private LinearLayout task_manager_loading_ll;
	private ActivityManager am = null;
	private TaskInfoProvider taskInfoProvider = null;
	private List<RunningAppProcessInfo> runningappinfos;
	private List<TaskInfo> taskInfos = null;
	private TaskInfoAdapter adapter = null;
	private List<TaskInfo> userTaskInfos = null;
	private List<TaskInfo> systemTaskInfos = null;
	private View view = null;
	private int totalUsedMemorySize = 0; 
	private Button task_manager_setting_bn = null;

	
	//���߳�ȡ������
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			task_manager_loading_ll.setVisibility(View.INVISIBLE);
			long totalmemoryinfo = totalUsedMemorySize * 1024 + TaskManagerActivity.this.getAvailableMemory();
			String strtotalmemory = TextFormater.getDataSize(totalmemoryinfo);
			String str = TaskManagerActivity.this.task_manager_title_tv_availablememory
					.getText().toString() + "  ���ڴ棺" + strtotalmemory;
			TaskManagerActivity.this.task_manager_title_tv_availablememory.setText(str);
			adapter = new TaskInfoAdapter();
			task_manager_lv.setAdapter(adapter);
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
		//�Ƿ�֧�־Ϳ����Զ������
		boolean flag = this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		
		this.setContentView(R.layout.task_manager);
		this.taskInfos = new ArrayList<TaskInfo>();
		this.task_manager_lv = (ListView) this.findViewById(R.id.task_manager_lv);
		this.task_manager_loading_ll = (LinearLayout) this.findViewById(R.id.task_manager_loading_ll);
		this.task_manager_setting_bn = (Button) this.findViewById(R.id.task_manager_setting_bn);
		//
		if(flag){
			//��һ���Զ������ʽ�����ø�����
			this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.task_manager_title);
		}
		this.task_manager_title_tv_count = (TextView) this.findViewById(R.id.task_manager_title_tv_count);
		this.task_manager_title_tv_availablememory = (TextView) this.findViewById(R.id.task_manager_title_tv_availablememory);
		
		this.fillData();
		
		//ΪListView��ÿһ��item���õ������
		task_manager_lv.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Object obj = task_manager_lv.getItemAtPosition(position);
				 
				if(obj instanceof TaskInfo){
					TaskInfo taskinfo = (TaskInfo) obj;
					CheckBox cb = (CheckBox) view.findViewById(R.id.task_manager_item_memory_cb);
					String packagename = taskinfo.getPackagename();
					if ("com.example.androiddefprot".equals(packagename)
							|| "android.process.media".equals(packagename)
							|| "system".equals(packagename)
							|| "android.process.acore".equals(packagename)){
						cb.setVisibility(View.INVISIBLE);
						return;
					}
					//��Ϊcheckbox�����߽������⣬�����ֶ�����CheckBox��״̬
					if(taskinfo.isCheck()){
						taskinfo.setCheck(false);
						cb.setChecked(false);
					}else{
						taskinfo.setCheck(true);
						cb.setChecked(true);
					}
				}
			}
		});
		
		
		//ΪListView��ÿһ��item���ó�������
		TaskManagerActivity.this.task_manager_lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
						Intent detailIntent = new Intent(TaskManagerActivity.this,AppDetailActivity.class);
						MyApplication myApplication = (MyApplication) TaskManagerActivity.this.getApplication();
					    Object obj = task_manager_lv.getItemAtPosition(position);
						if(obj instanceof TaskInfo){
							myApplication.taskinfo = (TaskInfo) obj;
							TaskManagerActivity.this.startActivity(detailIntent);
						}
				return false;
			}
		});
		
		/**
		 * Ϊ���ð�ť���ü���
		 */
		TaskManagerActivity.this.task_manager_setting_bn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TaskManagerActivity.this.appSetting();
			}
		});
	}
	

	
	/**
	 * ����title������
	 */
	public void setTitleData(){
		this.task_manager_title_tv_count.setText("������Ŀ:" + this.getProcessCount());
		this.task_manager_title_tv_availablememory.setText("ʣ���ڴ�:"
				+ TextFormater.getDataSize(this.getAvailableMemory()));
	}
	
	
	/**
	 * ��ȡϵͳ��ǰ���еĽ�����Ŀ
	 * @return ��ǰ���̵�����
	 */
	public int getProcessCount(){ 
		runningappinfos = this.am.getRunningAppProcesses();
		return runningappinfos.size();
	}
	
	
	
	/**
	 * ��ȡ��ǰ���õ�ϵͳʣ���ڴ�
	 * @return �����õ��ڴ�
	 */
	public long getAvailableMemory(){
		MemoryInfo outInfo = new ActivityManager.MemoryInfo();
		this.am.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}
	
	
	
	//
	public void fillData(){
		TaskManagerActivity.this.setTitleData();
		TaskManagerActivity.this.task_manager_loading_ll.setVisibility(View.VISIBLE);
		new Thread(){
			@Override
			public void run() {
				super.run();
				TaskInfoProvider taskInfoProvider = new TaskInfoProvider(TaskManagerActivity.this);
				TaskManagerActivity.this.taskInfos = taskInfoProvider
						.getAllTasks(TaskManagerActivity.this.am
								.getRunningAppProcesses());
				TaskManagerActivity.this.handler.sendEmptyMessage(0); 
			}
		}.start();
	}
	

	
	/**
	 * �߳���Ϣ����������
	 * @author jake
	 *
	 */
	private class TaskInfoAdapter extends BaseAdapter{

		public TaskInfoAdapter() {
			super();
			TaskManagerActivity.this.userTaskInfos = new ArrayList<TaskInfo>();
			TaskManagerActivity.this.systemTaskInfos = new ArrayList<TaskInfo>();
			for(TaskInfo info:taskInfos){
				if(info.isSystemapp()){
					systemTaskInfos.add(info);
				}else{
					userTaskInfos.add(info);
				}
			}
		}

		
		@Override
		public int getCount() {
			SharedPreferences sp = TaskManagerActivity.this
					.getSharedPreferences("config", Context.MODE_PRIVATE);
			boolean showsystemapp = sp.getBoolean("showsystemapp", false);
			if(showsystemapp){
				return taskInfos.size() + 2;   //2 �������������ǩ ������ʾ����ĸ���
			}else{
				return userTaskInfos.size() + 1;   //1 �������������ǩ ������ʾ����ĸ���
			}
		}

		
		@Override
		public Object getItem(int position) {
			if(position == 0){
				return 0;
			}else if(position <= userTaskInfos.size()){
				return userTaskInfos.get(position-1);
			}else if(position == userTaskInfos.size()+1){
				return position;
			}else if(position <= taskInfos.size()+2){
				return systemTaskInfos.get(position - userTaskInfos.size() - 2);
			}else{
				return position;
			}
		}

		
		
		@Override
		public long getItemId(int position) {
			if(position == 0){
				return -1;
			}else if(position <= userTaskInfos.size()){
				return (position-1);
			}else if(position == userTaskInfos.size()+1){
				return -1;
			}else if(position <= userTaskInfos.size()+2){
				return  position - userTaskInfos.size()-2; 
			}else{
				return -1;
			}
		}

		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// ����Щ��Ŀ��Ϣ ��һ�·��� ϵͳ���̺��û��������ֳ���
		
			if (position == 0) {
				TextView tv_userapp = new TextView(TaskManagerActivity.this);
				tv_userapp.setText("�û����� " + userTaskInfos.size() + "��");
				return tv_userapp;
			} else if (position <= userTaskInfos.size()) {
				int currentpositon = (position - 1);
				TaskInfo taskinfo = userTaskInfos.get(currentpositon);
				View view = View.inflate(TaskManagerActivity.this,
						R.layout.task_manager_item, null);
				ViewHolder holder = new ViewHolder();
				holder.task_manager_item_iv = (ImageView) view.findViewById(R.id.task_manager_item_iv);
				holder.task_manager_item_tv = (TextView) view.findViewById(R.id.task_manager_item_tv);
				holder.task_manager_memory_size_tv = (TextView) view
						.findViewById(R.id.task_manager_item_memory_size_tv);
				holder.task_manager_memory_cb = (CheckBox) view
						.findViewById(R.id.task_manager_item_memory_cb);
				if ("com.example.androiddefprot".equals(taskinfo
						.getPackagename())
						|| "android.process.media".equals(taskinfo
								.getPackagename())
						|| "system".equals(taskinfo.getPackagename())
						|| "android.process.acore".equals(taskinfo
								.getPackagename())){
					holder.task_manager_memory_cb.setVisibility(View.INVISIBLE);
					
				}
				holder.task_manager_item_iv.setImageDrawable(taskinfo.getAppicon());
				holder.task_manager_item_tv.setText(taskinfo.getAppname());
				holder.task_manager_memory_size_tv.setText("�ڴ�ռ��: "
						+ TextFormater.getKBDataSize(taskinfo.getMemorysize()));
				holder.task_manager_memory_cb.setChecked(taskinfo.isCheck());
				return view;

			} else if (position == userTaskInfos.size() + 1) {
				TextView tv_systemapp = new TextView(TaskManagerActivity.this);
				tv_systemapp.setText("ϵͳ���� " + systemTaskInfos.size() + "��");
				return tv_systemapp;

			} else if (position <= systemTaskInfos.size() + 2) {
				int systemposition = (position - userTaskInfos.size() - 2);
				TaskInfo taskinfo = systemTaskInfos.get(systemposition);
				View view = View.inflate(TaskManagerActivity.this,
						R.layout.task_manager_item, null);
				ViewHolder holder = new ViewHolder();
				holder.task_manager_item_iv = (ImageView) view.findViewById(R.id.task_manager_item_iv);
				holder.task_manager_item_tv = (TextView) view.findViewById(R.id.task_manager_item_tv);
				holder.task_manager_memory_size_tv = (TextView) view
						.findViewById(R.id.task_manager_item_memory_size_tv);
				holder.task_manager_memory_cb = (CheckBox) view
						.findViewById(R.id.task_manager_item_memory_cb);
			
				if ("com.example.androiddefprot".equals(taskinfo
						.getPackagename())
						|| "android.process.media".equals(taskinfo
								.getPackagename())
						|| "system".equals(taskinfo.getPackagename())
						|| "android.process.acore".equals(taskinfo
								.getPackagename())){
					holder.task_manager_memory_cb.setVisibility(View.INVISIBLE);
					
				}
				holder.task_manager_item_iv.setImageDrawable(taskinfo.getAppicon());
				holder.task_manager_item_tv.setText(taskinfo.getAppname());
				holder.task_manager_memory_size_tv.setText("�ڴ�ռ��: "
						+ TextFormater.getKBDataSize(taskinfo.getMemorysize()));
				holder.task_manager_memory_cb.setChecked(taskinfo.isCheck());
				return view;

			} else {// �϶�����ִ��
				return null;
			}				

		}

		
	}
	

	/*View view = View.inflate(TaskManagerActivity.this, R.layout.task_manager_item, null);
	ViewHolder holder = new ViewHolder();
	holder.task_manager_item_iv = (ImageView) view.findViewById(R.id.task_manager_item_iv);
	holder.task_manager_item_tv = (TextView) view.findViewById(R.id.task_manager_item_tv);	
	holder.task_manager_memory_size_tv = (TextView) view.findViewById(R.id.task_manager_memory_size_tv);
	holder.task_manager_memory_cb = (CheckBox) view.findViewById(R.id.task_manager_memory_cb);
	
	
	int usedmemory = taskInfos.get(position).getMemorysize();
	
	holder.task_manager_item_iv.setImageDrawable(taskInfos.get(position).getAppicon());
	holder.task_manager_item_tv.setText(taskInfos.get(position).getAppname());
	holder.task_manager_memory_size_tv.setText(String.valueOf(TextFormater.getKBDataSize(usedmemory)));
	holder.task_manager_memory_cb.setChecked(taskInfos.get(position).isCheck());*/
	
	
	static class ViewHolder{
		public ImageView task_manager_item_iv;
		public TextView task_manager_item_tv;
		public TextView task_manager_memory_size_tv;
		public CheckBox task_manager_memory_cb;
	}
	
	
	/**
	 * ������̹���������
	 */
	public void appSetting(){
		Intent appSettingIntent = new Intent(TaskManagerActivity.this,TaskSettingActivity.class);
		TaskManagerActivity.this.startActivityForResult(appSettingIntent, 0);
	}

	
	
	/**
	 * ֪ͨListView��������
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 200){
			TaskManagerActivity.this.fillData();
		}
	}
	
	
	
	/**
	 * ɱ������
	 * @param view
	 */
	public void killTask(View view){
		int total = 0;
		for(TaskInfo taskinfo:userTaskInfos){
			if(taskinfo.isCheck()){
				totalUsedMemorySize += taskinfo.getMemorysize();
				TaskManagerActivity.this.am.killBackgroundProcesses(taskinfo.getPackagename());
				total++;
			}
		}
		
		for(TaskInfo taskinfo:systemTaskInfos){
			if(taskinfo.isCheck()){
				totalUsedMemorySize += taskinfo.getMemorysize();
				TaskManagerActivity.this.am.killBackgroundProcesses(taskinfo.getPackagename());
				total++;
			}
		}
		
		String size = TextFormater.getKBDataSize(totalUsedMemorySize);
		//Toast.makeText(TaskManagerActivity.this, "һ��ɱ����"+total+"������"+"�ͷ���"+size+"�ռ�", Toast.LENGTH_LONG).show();
		MyToast.showToast(TaskManagerActivity.this, R.drawable.notification, "һ��ɱ����"+total+"������"+"�ͷ���"+size+"�ռ�");
		//֪ͨui����
		adapter = new TaskInfoAdapter();
		task_manager_lv.setAdapter(adapter);
		this.fillData();
	}
	
}
