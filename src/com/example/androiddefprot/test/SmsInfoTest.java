package com.example.androiddefprot.test;

import java.util.List;

import com.example.androiddefprot.domain.SmsInfo;
import com.example.androiddefprot.engine.SmsInfoService;

import android.test.AndroidTestCase;


/*
 * ≤‚ ‘
 */
public class SmsInfoTest extends AndroidTestCase {
	
	
	public void SmsInfoTest(){
		SmsInfoService service = new SmsInfoService(this.getContext());
		List<SmsInfo> list = service.getSmsInfos();
		System.out.println(list.size());
		
	}

}
