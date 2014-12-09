package com.example.androiddefprot.engine;

import java.util.ArrayList;
import java.util.List;

import com.example.androiddefprot.R;
import com.example.androiddefprot.domain.ContactInfo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;


/**
 * 
* @Title: ContactInfoService.java 
* @Package com.example.androiddefprot.engine 
* @Description: ͨ��ContentResolver ��ѯ��ϵͳ����ϵ����Ϣ��
* @author lian_weijian@163.com   
* @version V1.0
 */
public class ContactInfoService{
	
	private Context context;

	
	public ContactInfoService() {
		super();
	}


	public ContactInfoService(Context context) {
		super();
		this.context = context;
	}
	

    //1.��ȡ��ϵ�˵�id
	//2.������ϵ�˵�id ��ȡ��ϵ������
	//3.������ϵ�˵�id ���ݵ�type ��ȡ����Ӧ������(�绰,email);
	public  List<ContactInfo> getContactInfos(){
		Cursor cursor = null;
		ContactInfo contactinfo = null;
		ContentResolver resolver = this.context.getContentResolver();
		List<ContactInfo> infos = new ArrayList<ContactInfo>();           //����һ��List���ڱ��棬ContactInfo����Ϣ��
		Uri uri = Uri.parse(context.getString(R.string.ContactInfo_uri_raw_contacts));
		Uri datauri = Uri.parse(context.getString(R.string.ContactInfo_uri_data));
		cursor = resolver.query(uri, null, null, null, null);
		while(cursor.moveToNext()){
			contactinfo = new ContactInfo();
			String id = cursor.getString(cursor.getColumnIndex("_id"));
			String name = cursor.getString(cursor.getColumnIndex("display_name"));
			contactinfo.setName(name);
			Cursor datacursor = resolver.query(datauri, null, "raw_contact_id=?", new String[]{id}, null);
			//���ϲ�ѯ����ѯ������ϵ�˵��������Լ����������ѯ��id��
			 while (datacursor.moveToNext()) {
					//mimetype
					String type = datacursor.getString(datacursor.getColumnIndex("mimetype"));
					if("vnd.android.cursor.item/phone_v2".equals(type)){
						String number = datacursor.getString(datacursor.getColumnIndex("data1"));
						contactinfo.setPhone(number);
					}
			    }
			datacursor.close();
			infos.add(contactinfo);
			contactinfo = null;
		}
		return infos;
	}

}
