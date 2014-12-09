package com.example.androiddefprot.engine;


import com.example.androiddefprot.dao.AddressDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * 
* @Title: NumberAddressService.java 
* @Package com.example.androiddefprot.engine 
* @Description: ���������ҵ���࣬�������ݿ⣬����ѯ
* @author lian_weijian@163.com   
* @version V1.0
 */
public class NumberAddressService {
	
	public static String getAddressByNumber(String number){
		String pattern = "1[3458]\\d{9}$";
		String address = null;
		if(number.matches(pattern)){
		
			SQLiteDatabase db = AddressDao.getAddressDB("/sdcard/address.db");
			String sql = "select city from info where mobileprefix = ?";
			String[] params = new String[]{
				number.substring(0,7)   //�и������ǰ7λ�����ж�����һ��������
			};
			Cursor cursor = db.rawQuery(sql, params);
			while(cursor.moveToNext()){
				address = cursor.getString(0);
			}
			cursor.close();
			db.close();
		}else{
			SQLiteDatabase db = null;
			switch(number.length()){
				case 4://4λ����
					address = "ģ����";
				break;
				
				case 7://7λ����
					address = "���غ���";
				break;
				
				case 8://8λ����
					address = "���غ���";
				break;
				
				case 10://10λ����
					db = AddressDao.getAddressDB("/sdcard/address.db");
					String sql = "select city from info where area = ? limit 1";
					String[] params = new String[]{
						number.substring(0,4)
					};
					Cursor cursor = db.rawQuery(sql, params);
					while(cursor.moveToNext()){
						address = cursor.getString(0);
					}
					cursor.close();
					db.close();
				break;
				
				case 11://11λ����
					db = AddressDao.getAddressDB("/sdcard/address.db");
					String sqls = "select city from info where area = ? limit 1";
					String[] paramss = new String[]{
							number.substring(0, 3)
					};
					Cursor cursors = db.rawQuery(sqls, paramss);
					while(cursors.moveToNext()){
						address = cursors.getString(0);
					}
					
					String sqlf= "select city from info where area = ? limit 1";
					String[] paramf = new String[]{
							number.substring(0, 4)
					};
					
					Cursor cursorf = db.rawQuery(sqls, paramf);
					while(cursorf.moveToNext()){
						address = cursorf.getString(0);
					}
					cursors.close();
					cursorf.close();
					db.close();
				break;
				
				case 12://12λ����
					SQLiteDatabase dbf = AddressDao.getAddressDB("/sdcard/address.db");
					String sqlfi = "select city from info where area = ? limit 1 ";
					String[] paramfi = new String[]{
						number.substring(0, 4)
					};
					Cursor cursorfi = dbf.rawQuery(sqlfi, paramfi);
					while(cursorfi.moveToNext()){
						address = cursorfi.getString(0);
					}
					cursorfi.close();
					dbf.close();
				break;
			}
		}
		return address;
		
		
	}

}
