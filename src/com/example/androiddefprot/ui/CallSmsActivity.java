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
* @Description: 黑名单管理Activity 
* @author lian_weijian@163.com   
* @version V1.0
 */
public class CallSmsActivity extends Activity {
	
	private Button add_black_number_bn = null;
	private ListView call_sms_safe_lv = null;
	private BlackNumberDao blacknumberdao = null; //数据库服务类进行CRUD操作
	private List<String> numbers = null;          //保存号码
	private CallAdapter adapter = null;			  //自定义数据适配器（adapter）避免使用ArrayAdapter的缓存
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.call_sms_safe);
		this.blacknumberdao = new BlackNumberDao(this); 
		this.add_black_number_bn = (Button) this.findViewById(R.id.add_black_number_bn);
		this.call_sms_safe_lv = (ListView) this.findViewById(R.id.call_sms_safe_lv);
		
		this.registerForContextMenu(call_sms_safe_lv);//在ListVIew控件上注册一个上下文菜单
		
		add_black_number_bn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(CallSmsActivity.this);
				builder.setTitle("添加黑名单号码");
				final EditText editText = new EditText(CallSmsActivity.this);
				editText.setInputType(InputType.TYPE_CLASS_PHONE);
				builder.setView(editText);
				builder.setPositiveButton("添加", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String number = editText.getText().toString().trim();
						if(TextUtils.isEmpty(number)){
							Toast.makeText(CallSmsActivity.this,CallSmsActivity.this.getResources().getString(R.string.call_sms_safe_not_null) , Toast.LENGTH_LONG).show();
							return;
						}else{
							blacknumberdao.add(number);
							
							//要更新ListView可以有两种方法
							// 1.重新建立一个新的Adapter（不推荐使用）
							//todo: 通知listview更新数据
							// 缺点: 重新刷新整个listview 
							//numbers = dao.getAllNumbers();
							//lv_call_sms_safe.setAdapter(new ArrayAdapter<String>(CallSmsActivity.this, R.layout.blacknumber_item, R.id.tv_blacknumber_item, numbers));
							
							
							//2.通知数据适配器更新数据
							numbers = blacknumberdao.getAllNumber();
							adapter.notifyDataSetChanged();
						}
					}
				});
				
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
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

	
	
	/*使用ArrayAdapter 时候 存在缓存所以在使用adapter.notifyDataSetChanged()的时候，会出现没办法动态更
	新ArrayAdapter的数据问题所以，要解决这个问题，应该通过继承BaseAdapter   重写里面的方法，从而避免ArrayAdapter的缓存问题。*/
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
	 * 创建一个上下文菜单
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = this.getMenuInflater(); //把menu资源转换为一个menu对象
		inflater.inflate(R.menu.context_menu, menu);
	}


	/**
	 * 当上下文菜单某个条目被选中后，激活的方法
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
			  //当前条目id
			  blacknumberdao.delete(number);
			  //重新获取电话黑名单
			  numbers = blacknumberdao.getAllNumber();
			  //通知listview更新数据
			  adapter.notifyDataSetChanged();
		  break;
	  }
	  return false;
	}


	
	/**
	 * 用户更新黑名单数据库方法
	 * @param number
	 */
	private void updateNumber(final String oldNumber) {
		final EditText et = new EditText(CallSmsActivity.this);
		et.setInputType(InputType.TYPE_CLASS_PHONE);
		AlertDialog.Builder builder = new Builder(CallSmsActivity.this);
		builder.setTitle("更改黑名单号码");
		builder.setView(et);
		builder.setPositiveButton("更改", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String newNumber = et.getText().toString().trim();
				if(TextUtils.isEmpty(newNumber)){
					Toast.makeText(CallSmsActivity.this, CallSmsActivity.this.getResources().getString(R.string.call_sms_safe_not_null), Toast.LENGTH_LONG).show();
					return;
				}else{
					blacknumberdao.update(oldNumber, newNumber);
					
					//要更新ListView可以有两种方法
					// 1.重新建立一个新的Adapter（不推荐使用）
					//todo: 通知listview更新数据
					// 缺点: 重新刷新整个listview 
					//numbers = dao.getAllNumbers();
					//lv_call_sms_safe.setAdapter(new ArrayAdapter<String>(CallSmsActivity.this, R.layout.blacknumber_item, R.id.tv_blacknumber_item, numbers));
					
					
					//2.通知数据适配器更新数据
					numbers = blacknumberdao.getAllNumber();
					adapter.notifyDataSetChanged();
				}
			}
		});
		
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.create().show();
	}
	
	
	
	
	
	
}
