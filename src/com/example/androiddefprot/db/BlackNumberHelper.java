package com.example.androiddefprot.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * 
* @Title: BlackNumberHelper.java 
* @Package com.example.androiddefprot.db 
* @Description:�������������ݿ�����ݿⴴ���͸�����
* @author lian_weijian@163.com   
* @version V1.0
 */
public class BlackNumberHelper extends SQLiteOpenHelper{
	
	private static final String DBNAME = "blacknumber.db";   //���ݿ�����
	private static final int DAVERSION = 1;					 //���ݿ�汾��
	private static final String TABLENAME = "blacknumber";	 //���ݱ�����
	
	
	
	public BlackNumberHelper(Context context){
		super(context, DBNAME, null, DAVERSION);
		
	}

	
	
	//��ϵͳ��û�к��������ݿ��ʱ����ã�����һ�����������ݿ�
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table " + TABLENAME 
				+ "("
				+ "_id integer primary key ,"
				+ "number varchar(20)"
				+ ")";
		db.execSQL(sql);
	}

	
	//���ݿ�汾�Ÿ��º������������ݿ�
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "DROP TABLE IF EXISTS" + TABLENAME;
		db.execSQL(sql);
		this.onCreate(db);
		
	}

}
