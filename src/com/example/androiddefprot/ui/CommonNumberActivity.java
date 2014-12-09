package com.example.androiddefprot.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.example.androiddefprot.R;

import android.app.Activity;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

/**
 * 
* @Title: CommonNumberActivity.java 
* @Package com.example.androiddefprot.ui 
* @Description: ���ú����ѯActivity
* @author lian_weijian@163.com   
* @version V1.0
 */
public class CommonNumberActivity extends Activity {
	
	private ExpandableListView common_num_query_elv = null;
	private SQLiteDatabase database = null;
	private ExpendAdapter adapter = null;
	private String comm_num_db_path = null;
	private int count = 0;
	private Cursor cursor = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.common_num_query);
		common_num_query_elv = (ExpandableListView) this.findViewById(R.id.common_num_query_elv);
		comm_num_db_path = this.getResources().getString(R.string.comm_num_db_path);
		
		copyFile();
		
		this.database = SQLiteDatabase.openDatabase(comm_num_db_path, null, SQLiteDatabase.OPEN_READONLY);
		this.adapter = new ExpendAdapter();
		this.common_num_query_elv.setAdapter(adapter);
		
	}


	//��������ڳ��ú������ݿ⣬����apk�и��Ƶ�sd����
	private void copyFile() {
		try {
			
			File commnumDbFile = new File(comm_num_db_path);
			if(!commnumDbFile.exists()){
				String comm_num_db_name = this.getResources().getString(R.string.comm_num_db_name);
				AssetManager assetmanager = this.getAssets();
				InputStream is = assetmanager.open(comm_num_db_name);
				FileOutputStream fos = new FileOutputStream(commnumDbFile);
				byte[] buffer = new byte[1024];
				int len = 0;
				while((len = is.read(buffer)) != -1){
					fos.write(buffer, 0, len);
				}
				fos.flush();
				is.close();
				fos.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//ExpandableListView ����������
	private class ExpendAdapter extends BaseExpandableListAdapter{

		//һ���м�����
		@Override
		public int getGroupCount() {
			if(CommonNumberActivity.this.database.isOpen()){
				String groupName = CommonNumberActivity.this.getResources()
						.getString(R.string.comm_num_db_group_tablename);
				String sql = "select count(*) from " + groupName;
				CommonNumberActivity.this.cursor = CommonNumberActivity.this.database.rawQuery(sql, null);
				while(cursor.moveToNext()){
					count = cursor.getInt(0);
				}
				cursor.close();
			}
			return count; 
		}

		//ÿ���������м�����Ŀ
		@Override
		public int getChildrenCount(int groupPosition) {
			int count = 0;
			if(CommonNumberActivity.this.database.isOpen()){
				String sql = "select count(*) from table" + (groupPosition + 1);
				CommonNumberActivity.this.cursor =  CommonNumberActivity.this.database.rawQuery(sql, null);
				while(cursor.moveToNext()){
					count = cursor.getInt(0);
				}
				cursor.close();
			}
			return count;
		}

		
		// ���ص�ǰgroupPosition ��Ӧλ�õĶ���
		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return null;
		}

		// ����groupPosition��childPosition���Ӻ��Ӷ�Ӧ����Ŀ
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return null;
		}

		// ��ȡ�����id
		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		// ��ȡ�������Ӻ���id
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		
		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		//�����Ķ���
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView tv = null;
			String text = null;
			if(convertView != null){
				 tv = (TextView) convertView;
			}else{
				tv = new TextView(CommonNumberActivity.this);
			}
			
			if(CommonNumberActivity.this.database.isOpen()){
				int currentPosition = groupPosition + 1;
				String tablename = CommonNumberActivity.this.getResources().getString(R.string.comm_num_db_group_tablename);
				String sql = "select name from " + tablename + " where idx = ?";
				CommonNumberActivity.this.cursor = CommonNumberActivity.this.database
						.rawQuery(sql, new String[] { currentPosition + "" });
				while(CommonNumberActivity.this.cursor.moveToNext()){
					text = CommonNumberActivity.this.cursor.getString(0);
				}
				cursor.close();
			}
			tv.setText("              "+text);
			return tv;
		}

		//���ÿ�����еĺ��ӵĶ���
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			TextView tv = null;
			StringBuilder sb = new StringBuilder("");
			int groupIndex = groupPosition + 1;
			int childIndex = childPosition + 1;
			if(convertView != null){
				tv = (TextView) convertView;
			}else{
				tv = new TextView(CommonNumberActivity.this);
			}
			String sql = "select number,name from table" + groupIndex + " where _id = ?";
			if(CommonNumberActivity.this.database.isOpen()){
				CommonNumberActivity.this.cursor = CommonNumberActivity.this.database
						.rawQuery(sql, new String[] { childIndex + "" });
				while(CommonNumberActivity.this.cursor.moveToNext()){
					sb.append(CommonNumberActivity.this.cursor.getString(0));
					sb.append(":");
					sb.append(CommonNumberActivity.this.cursor.getString(1));
				}
				cursor.close();
			}
			tv.setText("           "+sb.toString());
			return tv;
		}

		//�ж�ÿ����ĵĶ��� �Ƿ���Խ��յ���¼���Ĭ���ǲ����ԡ����Է���false�����Ҫ���յ���¼������Ը�Ϊ����true
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return false;
		}
	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(this.database != null){
			this.database.close();
			cursor.close();
		}
		
	}

}
