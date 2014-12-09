package com.example.androiddefprot.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import com.example.androiddefprot.R;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * 
* @Title: ClearSdcardActivity.java 
* @Package com.example.androiddefprot.ui 
* @Description: ����sd��Activity  ͨ����ѯ���ݿ�ʵ�֣�
* ͨ����ѯ���ݿ��бȽϳ��õ�Ӧ�õĻ����ļ��У������ѯ���оͰѶ�Ӧ��sd���ļ���ɾ��
* @author lian_weijian@163.com   
* @version V1.0
 */
public class ClearSdcardActivity extends Activity {
	
	private TextView clearsdcard_tv_title = null;
	private ProgressBar clearsdcard_pb = null;
	private Button clearsdcard_bn = null;
	private SQLiteDatabase db = null;
	private String db_name = null ;
	private String db_path = null;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.clearsdcard);
		this.clearsdcard_tv_title = (TextView) this.findViewById(R.id.clearsdcard_tv_title);
		this.clearsdcard_pb = (ProgressBar) this.findViewById(R.id.clearsdcard_pb);
		this.clearsdcard_bn = (Button) this.findViewById(R.id.clearsdcard_bn);
		this.db_name = this.getResources().getString(R.string.clearsdcard_db_name);
		this.db_path = this.getResources().getString(R.string.clearsdcard_db_path);
		copyFile();
		
	}

	
	
	
	//��������ڳ��ú������ݿ⣬����apk�и��Ƶ�sd����
	private void copyFile() {
		AssetManager am = this.getAssets();
		try {
			File file = new File(db_path);
			if(!file.exists()){
				InputStream is = am.open(db_name);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				while((len = is.read(buffer))!= -1){
					fos.write(buffer, 0, len);
				}
				fos.flush();
				is.close();
				fos.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//��ť����¼�
	public void start(View view){
		this.db = SQLiteDatabase.openDatabase(db_path, null, SQLiteDatabase.OPEN_READONLY);
		new Thread(){        //��ȡƥ���ļ���ȽϺ�ʱ����������һ���µ��߳̽��д���

			@Override
			public void run() {
				super.run();
				PackageManager pm = ClearSdcardActivity.this.getPackageManager();
				List<PackageInfo> infos = pm.getInstalledPackages(0);
				ClearSdcardActivity.this.clearsdcard_pb.setMax(infos.size());
				int total = 0;
				for(PackageInfo info: infos){
					String packagename = info.packageName;
					String sql = "select filepath from softdetail where apkname = ?";
					Cursor cursor = ClearSdcardActivity.this.db.rawQuery(sql, new String[]{packagename});
					if(cursor.moveToNext()){
						String path = cursor.getString(0);
						File delFile = new File(Environment.getExternalStorageDirectory(),path);
						delDir(delFile);//ɾ���ļ��ķ���
					
						try {
							this.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						total++; //���Ľ���������
						ClearSdcardActivity.this.clearsdcard_pb.setProgress(total);
						cursor.close();
						Message msg = Message.obtain(); //����Ϣ�����л�ȡ���е�message�����Խ�ʡ��Դ����
						msg.obj = "���" + packagename; 
						handler.sendMessage(msg);
					}
				}
				Message msg = Message.obtain();
				msg.obj = "�������";
				handler.sendMessage(msg);
				db.close();
			}
		};
	}




	/**
	 * �õݹ�ʵ���ļ���ɾ��
	 * @param delFile
	 */
	private void delDir(File delFile) {
		if(delFile.isDirectory()){ //�������2����һ���ļ��У��͵ݹ飬ֱ�����ļ�Ϊֹ
			File[] files = delFile.listFiles(); 
			for(int i = 0; i < files.length; i++){
				delDir(files[i]);
			}
		}else{
			delFile.delete();
		}
	}

	
}
