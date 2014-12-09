package com.example.androiddefprot.ui;


import java.io.File;

import com.example.androiddefprot.R;
import com.example.androiddefprot.domain.UpdateInfo;
import com.example.androiddefprot.engine.DownLoadFileTask;
import com.example.androiddefprot.engine.UpdateInfoService;
import com.example.androiddefprot.logmanagement.LogManagement;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
* @Title: SplashActivity.java 
* @Package com.example.androiddefprot.ui 
* @Description: Splahs����
* @author lian_weijian@163.com   
* @version V1.0
 */
public class SplashActivity extends ActionBarActivity {

	private TextView tv_splash_version = null;
	private LinearLayout ll_splash_main = null;
	private UpdateInfo updateInfo = null; //���������Ϣ
	private static final String TAG = "SplashActivity";  
	private  String version = null;        //
	private ProgressDialog pd = null;
	private Handler handler = null;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //��ȥ����
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);

        this.tv_splash_version = (TextView) this.findViewById(R.id.tv_splash_version);
        this.ll_splash_main = (LinearLayout) this.findViewById(R.id.ll_splash_main);
        this.pd = new ProgressDialog(this);
        this.pd.setMessage("�������ء�������");
        this.pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        
        version = getVersion();
        
        this.tv_splash_version.setText(version);
        AlphaAnimation animation = new AlphaAnimation(0.0f,1.0f);
        animation.setDuration(3000);
        ll_splash_main.setAnimation(animation);
        animation.start();
        
        //�жϱ��ذ汾�� �Ϳͻ��˰汾���Ƿ���ͬ
        isNeedUpdate();
        
        //����ȫ��
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    
    
   /**
    * �����°汾��ʱ�򣬵������� ѯ���Ƿ����apk
    */
    private void showUpdateDialog() {
		Dialog dialog = new AlertDialog.Builder(this).setCancelable(false)//�û�����ȡ���Ի���
				.setIcon(R.drawable.icon5).setTitle("��������")
				.setMessage(updateInfo.getDescription())
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						LogManagement.i(TAG, "����apk");
						if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
							pd.show();
							new DownLaodAPKThreadS(updateInfo.getApkurl(),"/sdcard/new.apk").execute("test");
							//new Thread(new DownLaodAPKThread(updateInfo.getApkurl(),"/sdcard/new.apk")).start();
						}else{
							Toast.makeText(SplashActivity.this, "SD��������", Toast.LENGTH_LONG).show();
							SplashActivity.this.loadMainScreen();
						}
					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						LogManagement.i(TAG, "�û�ȡ�� �������������");
						SplashActivity.this.loadMainScreen();
						
					}
				}).create();
		dialog.show();
	}



	/**
     * 
     * @param version ��ǰ�汾��
     * @return �Ƿ���Ҫ����
     */
	private void isNeedUpdate() {
		  new Thread(new ThreadTe()).start();
	        this.handler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					SplashActivity.this.updateInfo = (UpdateInfo) msg.obj;
					if(version.equals(updateInfo.getVersion())){
						LogManagement.i(TAG, "�汾��һ�£���������");
						//Toast.makeText(SplashActivity.this, "�汾��һ�£���������", Toast.LENGTH_LONG).show();
						SplashActivity.this.loadMainScreen();
					}else{
						LogManagement.i(TAG, "�汾�Ų�һ��");
						//Toast.makeText(SplashActivity.this, "�汾�Ų�һ��", Toast.LENGTH_LONG).show();
						showUpdateDialog();
					}
				}
	        };
	}

	
	/*private boolean isNeedUpdate(String versiontext){
		UpdateInfoService service = new UpdateInfoService(this);
		try {
			updateInfo = service.getUpdateInfo(R.string.apkurl);
			String version = updateInfo.getVersion();
			if(versiontext.equals(version)){
				LogManagement.i(TAG, "�汾��ͬ,��������");
				SplashActivity.this.loadMainScreen();
				return false;
			}else{ 
				LogManagement.i(TAG, "�治ͬ����Ҫ����");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "��ȡ������Ϣ�쳣", 0).show();
			LogManagement.i(TAG, "��ȡ������Ϣ�쳣, ����������");
			SplashActivity.this.loadMainScreen();
			return false;
		}
	}*/
	
	
	/**��
	 * ���߳��࣬�����ں�̨����һ���µ��̺߳ͷ���������ͨ�š��жϷ������汾���Ƿ�ͱ��ذ汾��һ�£�ͨ���̳�AsyncTask ��ʵ�֣�
	 * @author jake
	 *
	 */
	
	class getServiceVersionThread extends AsyncTask<Integer, String, UpdateInfo>{
		
		@Override
		protected void onPostExecute(UpdateInfo result) {
			super.onPostExecute(result);
			if(SplashActivity.this.version.equals(result.getVersion())){
				LogManagement.i(TAG, "�汾��һ�£���������");
				Toast.makeText(SplashActivity.this, "�汾��һ�£���������", Toast.LENGTH_LONG).show();
			}else{
				LogManagement.i(TAG, "�汾�Ų�һ��");
				Toast.makeText(SplashActivity.this, "�汾�Ų�һ��", Toast.LENGTH_LONG).show();
				showUpdateDialog();
			}
		}

		@Override
		protected UpdateInfo doInBackground(Integer... params) {
			UpdateInfoService service = new UpdateInfoService(SplashActivity.this);
			UpdateInfo updateinfo = null;
			try {
				updateinfo = service.getUpdateInfo(R.string.apkurl);
				LogManagement.i("SplashActivity", updateinfo.getVersion());
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return updateinfo;
		}
    }

	
	
	/**
	 * ���߳��࣬�����ں�̨����һ���µ��̺߳ͷ���������ͨ�š��жϷ������汾���Ƿ�ͱ��ذ汾��һ�£�ͨ���̳�Runnable�ӿ� ʵ�֣�
	 * @author jake
	 *
	 */
	class ThreadTe implements Runnable{
		@Override
		public void run() {
			UpdateInfoService service = new UpdateInfoService(SplashActivity.this);
			try {
				UpdateInfo updateinfo = service.getUpdateInfo(R.string.apkurl);
				
					Message msg = new Message();
					msg.obj = updateinfo;
					SplashActivity.this.handler.sendMessage(msg);
					//SplashActivity.this.handler.sendMessageDelayed(msg, 1000);
				
			} catch (Exception e) {
				e.printStackTrace();
				SplashActivity.this.loadMainScreen();
			}
		}
	}


	/**
     * ��ð汾��
     * @return
     */
    private String getVersion(){
    	String version = null;
    	PackageManager manager = this.getPackageManager();
    	try {
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			return info.versionName;  
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
    	return this.getResources().getString(R.string.unknow_verson);
    }

    
    /**
     * ���ؽ������������
     */
    private void loadMainScreen(){
    	Intent intent = new Intent(this,MainScreenActivity.class);
    	this.startActivity(intent);
    	this.finish();//��Splash��Activity��ջ���Ƴ�����ֹ�û������˰�ť ���ٳ���Splash����
    }

    
    
    
    /**
     * ����һ���µ��߳�,�����µ�apk�ļ� ����װ(����1 �̳�Runnable�ӿ�)
     * @author jake
     *
     */
   /* private class DownLaodAPKThread implements Runnable{

    	private String path;//������uri
    	private String filePath; //�ļ�·��
    	
		public DownLaodAPKThread(String path, String filePath) {
			super();
			this.path = path;
			this.filePath = filePath;
		}

		@Override
		public void run() {
			try {
				//Looper.prepare();
				File file = DownLoadFileTask.getFile(path, filePath, pd);
				LogManagement.i(SplashActivity.this.TAG, "�������");
				SplashActivity.this.pd.dismiss();
	
				SplashActivity.this.install(file);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LogManagement.i(SplashActivity.this.TAG, "���ش���");
				Toast.makeText(SplashActivity.this, "���ش���", Toast.LENGTH_LONG).show();
				pd.dismiss();
				SplashActivity.this.loadMainScreen();
			}
		}
    }*/
    
    /**
     * ����һ���µ��߳�,�����µ�apk�ļ� ����װ(����2 �̳�AsyncTask<String, String, File>)
     * @author jake
     *
     */
    private class DownLaodAPKThreadS extends AsyncTask<String, String, File>{
    	
    	private String path;
    	private String filePath;

    	
		public DownLaodAPKThreadS(String path, String filePath) {
			super();
			this.path = path;
			this.filePath = filePath;
		}
		

		@Override
		protected void onPostExecute(File result) {
			//LogManagement.i(SplashActivity.this.TAG,result.toString());
			SplashActivity.this.pd.dismiss();
			SplashActivity.this.install(result);
		}


		@Override
		protected File doInBackground(String... params) {
			File file = null;
			try {
				
				file = DownLoadFileTask.getFile(path, filePath, pd);
				LogManagement.i(SplashActivity.this.TAG, "�������");
				SplashActivity.this.pd.dismiss();
				SplashActivity.this.install(file);
			} catch (Exception e) {
				e.printStackTrace();
				LogManagement.i(SplashActivity.this.TAG, "���ش���");
				pd.dismiss();
				SplashActivity.this.loadMainScreen();
			}
			return file;
		}
    }

    
    
    /**
     * ��������µ�apk�� �Զ���װ
     */
	public void install(File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
		this.startActivity(intent);
		this.finish();
		
	}
    
    
}
