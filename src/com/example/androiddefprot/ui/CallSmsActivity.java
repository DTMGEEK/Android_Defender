package com.example.androiddefprot.ui;

import java.util.List;

import junit.runner.BaseTestRunner;

import com.example.androiddefprot.R;
import com.example.androiddefprot.db.dao.BlackNumberDao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 
* @Title: CallSmsActivity.java 
* @Package com.example.androiddefprot.ui 
* @Description: ����������Activity 
* @author lian_weijian@163.com   
* @version V1.0
 */
public class CallSmsActivity extends Activity {
	
	private Button add_black_number_bn = null;
	private ListView call_sms_safe_lv = null;
	private BlackNumberDao blacknumberdao = null; //���ݿ���������CRUD����
	private List<String> numbers = null;          //�������
	private CallAdapter adapter = null;			  //�Զ���������������adapter������ʹ��ArrayAdapter�Ļ���
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.call_sms_safe);
		this.blacknumberdao = new BlackNumberDao(this); 
		this.add_black_number_bn = (Button) this.findViewById(R.id.add_black_number_bn);
		this.call_sms_safe_lv = (ListView) this.findViewById(R.id.call_sms_safe_lv);
		
		this.registerForContextMenu(call_sms_safe_lv);//��ListVIew�ؼ���ע��һ�������Ĳ˵�
		
		add_black_number_bn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(CallSmsActivity.this);
				builder.setTitle("��Ӻ���������");
				final EditText editText = new EditText(CallSmsActivity.this);
				editText.setInputType(InputType.TYPE_CLASS_PHONE);
				builder.setView(editText);
				builder.setPositiveButton("���", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String number = editText.getText().toString().trim();
						if(TextUtils.isEmpty(number)){
							Toast.makeText(CallSmsActivity.this,CallSmsActivity.this.getResources().getString(R.string.call_sms_safe_not_null) , Toast.LENGTH_LONG).show();
							return;
						}else{
							blacknumberdao.add(number);
							
							//Ҫ����ListView���������ַ���
							// 1.���½���һ���µ�Adapter�����Ƽ�ʹ�ã�
							//todo: ֪ͨlistview��������
							// ȱ��: ����ˢ������listview 
							//numbers = dao.getAllNumbers();
							//lv_call_sms_safe.setAdapter(new ArrayAdapter<String>(CallSmsActivity.this, R.layout.blacknumber_item, R.id.tv_blacknumber_item, numbers));
							
							
							//2.֪ͨ������������������
							numbers = blacknumberdao.getAllNumber();
							adapter.notifyDataSetChanged();
						}
					}
				});
				
				builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
				builder.create().show();
			}
		});
		numbers = blacknumberdao.getAllNumber();
		
		adapter = new CallAdapter();
		this.call_sms_safe_lv.setAdapter(adapter);
	}

	
	
	/*ʹ��ArrayAdapter ʱ�� ���ڻ���������ʹ��adapter.notifyDataSetChanged()��ʱ�򣬻����û�취��̬��
	��ArrayAdapter�������������ԣ�Ҫ���������⣬Ӧ��ͨ���̳�BaseAdapter   ��д����ķ������Ӷ�����ArrayAdapter�Ļ������⡣*/
	private class CallAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return numbers.size();
		}

		
		@Override
		public Object getItem(int position) {
			return numbers.get(position);
		}

		
		@Override
		public long getItemId(int position) {
			return position;
		}

		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(CallSmsActivity.this, R.layout.blacknumber_item, null);
			TextView tv = (TextView) view.findViewById(R.id.blacknumber_item_tv);
			tv.setText(numbers.get(position));
			return view;
		}
	}



	/**
	 * ����һ�������Ĳ˵�
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = this.getMenuInflater(); //��menu��Դת��Ϊһ��menu����
		inflater.inflate(R.menu.context_menu, menu);
	}


	/**
	 * �������Ĳ˵�ĳ����Ŀ��ѡ�к󣬼���ķ���
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
      AdapterContextMenuInfo info =	(AdapterContextMenuInfo)item.getMenuInfo();
	  int id = (int) info.id;
	  String number = numbers.get(id);
	  switch(item.getItemId()){
		  case R.id.update_blacknumber:
			  updateNumber(number);
		  break;
		  
		  case R.id.delete_blacknumber:
			  //��ǰ��Ŀid
			  blacknumberdao.delete(number);
			  //���»�ȡ�绰������
			  numbers = blacknumberdao.getAllNumber();
			  //֪ͨlistview��������
			  adapter.notifyDataSetChanged();
		  break;
	  }
	  return false;
	}


	
	/**
	 * �û����º��������ݿⷽ��
	 * @param number
	 */
	private void updateNumber(final String oldNumber) {
		final EditText et = new EditText(CallSmsActivity.this);
		et.setInputType(InputType.TYPE_CLASS_PHONE);
		AlertDialog.Builder builder = new Builder(CallSmsActivity.this);
		builder.setTitle("���ĺ���������");
		builder.setView(et);
		builder.setPositiveButton("����", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String newNumber = et.getText().toString().trim();
				if(TextUtils.isEmpty(newNumber)){
					Toast.makeText(CallSmsActivity.this, CallSmsActivity.this.getResources().getString(R.string.call_sms_safe_not_null), Toast.LENGTH_LONG).show();
					return;
				}else{
					blacknumberdao.update(oldNumber, newNumber);
					
					//Ҫ����ListView���������ַ���
					// 1.���½���һ���µ�Adapter�����Ƽ�ʹ�ã�
					//todo: ֪ͨlistview��������
					// ȱ��: ����ˢ������listview 
					//numbers = dao.getAllNumbers();
					//lv_call_sms_safe.setAdapter(new ArrayAdapter<String>(CallSmsActivity.this, R.layout.blacknumber_item, R.id.tv_blacknumber_item, numbers));
					
					
					//2.֪ͨ������������������
					numbers = blacknumberdao.getAllNumber();
					adapter.notifyDataSetChanged();
				}
			}
		});
		
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.create().show();
	}
	
	
	
	
	
	
}
