package com.example.androiddefprot.ui;


import com.example.androiddefprot.R;
import com.example.androiddefprot.logmanagement.LogManagement;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;


/**
 * 
* @Title: DragViewActivity.java 
* @Package com.example.androiddefprot.ui 
* @Description: ʵ�������������ʾ������Ľ��� ͨ���϶�����λ��������ʾ
* @author lian_weijian@163.com   
* @version V1.0
 */

public class DragViewActivity extends Activity implements OnTouchListener {

	private static final String TAG = "DragViewActivity";
	private ImageView drag_view_iv = null; 
	private TextView drag_view_tv = null; 
	private SharedPreferences sp = null;   
	private int startx;     //��¼x�������
	private int starty;     //��¼y������� 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.drag_view);
		this.drag_view_iv = (ImageView) this.findViewById(R.id.drag_view_iv);
		this.drag_view_tv = (TextView) this.findViewById(R.id.drag_view_tv);
		this.sp = this.getSharedPreferences("config", Context.MODE_PRIVATE);
		drag_view_iv.setOnTouchListener(this);
	}
	
	
	//��ָ�����¼�
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(v.getId()){
			case R.id.drag_view_iv:
				
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:  //����ָ������Ļ�����ϼ�¼�µ�ǰ�������
						this.startx = (int) event.getRawX();
						this.starty = (int) event.getRawY();
					break;
					
					case MotionEvent.ACTION_MOVE:
						int x = (int) event.getRawX();
						int y = (int) event.getRawY();
						
						/*if(y < 240){
							drag_view_tv.layout(drag_view_tv.getLeft(), 260, drag_view_tv.getRight(), 280);
						}else{
							drag_view_tv.layout(drag_view_tv.getLeft(), 60, drag_view_tv.getRight(), 80);
						}
						*/
						
						//��ָ�ƶ��ľ���
						int dx = x - startx;
						int dy = y - starty;
						int l = drag_view_iv.getLeft();
						int t = drag_view_iv.getTop();
						int r = drag_view_iv.getRight();
						int b = drag_view_iv.getBottom();
						
						drag_view_iv.layout(l+dx, t+dy, r+dx, b+dy);
						
						startx = (int) event.getRawX(); //��ȡ���ƶ����λ��
						starty = (int) event.getRawY();
					break;
					
					
					case MotionEvent.ACTION_UP: //����ָ�뿪��Ļ��Ӧ���¼�
						LogManagement.i(TAG, "��ָ�뿪��Ļ");
						//��¼�����ͼƬ�ڴ����λ��
						int lasty = this.drag_view_iv.getTop();
						int lastx = this.drag_view_iv.getLeft();
						SharedPreferences.Editor editor = this.sp.edit();
						editor.putInt("lastx", lastx);
						editor.putInt("lasty", lasty);
						editor.commit();
					break;
				}
			 break;
		}
		return true;//����ture�Ͳ����ն�touch�¼��ķ���
	}
	
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		// ���¼����ϴ�imageview�ڴ����е�λ�� 
		int x = this.sp.getInt("lastx", 0);
		int y = this.sp.getInt("lasty", 0);
		
		LogManagement.i(TAG, "x=" + x );
		LogManagement.i(TAG, "y=" + y );
		
		//ͨ�������ļ�����drag_view_iv�ڴ����λ��
		LayoutParams params = (LayoutParams) drag_view_iv.getLayoutParams();
		params.leftMargin = x;
		params.topMargin = y;
		drag_view_iv.setLayoutParams(params);
	}


	
}
