package com.example.androiddefprot.ui;



import com.example.androiddefprot.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


/**
 * 
* @Title: SetupGuideOneActivity.java 
* @Package com.example.androiddefprot.ui 
* @Description: �������õ�һ��
* @author lian_weijian@163.com   
* @version V1.0
 */
public class SetupGuideOneActivity extends Activity implements OnClickListener {

	private Button bt_setupguide_next = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.setupguideone);
		this.bt_setupguide_next = (Button) this.findViewById(R.id.bt_setupguide_first_next);
		this.bt_setupguide_next.setOnClickListener(this);
	}

	
	@Override
	public void onClick(View v) {

		switch(v.getId()){
			case R.id.bt_setupguide_first_next:
				Intent setupGuideIntent = new Intent(SetupGuideOneActivity.this,SetupGuideSecondActivity.class);
				
				SetupGuideOneActivity.this.finish();    //�ѵ�ǰActivity������ջ���Ƴ�
				
				SetupGuideOneActivity.this.startActivity(setupGuideIntent);
				//�����л�Activityʱ��Ķ���Ч��
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		}
	}
}
