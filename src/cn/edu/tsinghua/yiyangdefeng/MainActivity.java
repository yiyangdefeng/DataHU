package cn.edu.tsinghua.yiyangdefeng;

import cn.edu.tsinghua.filedealer.FileDealerActivity;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class MainActivity extends Activity {
	Button createsheet;
	Button loadsheet;
	Button help;
	Button exit;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);
		createsheet = (Button) this.findViewById(R.id.createsheet);
		loadsheet = (Button) this.findViewById(R.id.loadsheet);
		help = (Button) this.findViewById(R.id.help);
		exit = (Button) this.findViewById(R.id.exit);
		createsheet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, EditViewActivity.class);
				startActivity(intent);
				finish();
			}
		});
		loadsheet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, FileDealerActivity.class);
				startActivity(intent);
				finish();
			}
		});
		help.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				builder.setCancelable(true);
				builder.setTitle("ʹ�ð���");
				builder.setMessage("��DataHU(Data Helper for You) ��һ����������ֻ���ʵ�ּ����ݴ���������\n" +
						"�����ܹ�������ʱ�������ʹ洢��õ����ݣ��������ݵؽ��м򵥲������õ�������Ϣ��\n" +
						"�������Ϊ�ĸ����棬��ʼ���棬���ݱ༭���棬���ݲ鿴��������Լ���ͼ���棬���󽫹������ü����ˣ����ʹ�õı���ԡ�\n" +
						"������������ģʽ�£������Ӧ��Ԫ�񼴿ɽ������������������������벻�ᱻ��¼���õ�Ԫ������ݻᱻ��ա�\n" +
						"�������ݲ鿴����������У������ѡ��������Ϊ�Ա������������������֮�����ͼ��Ҳ���Ը������е����ݰ���һ����ʽ�������С�\n" +
						"��ͼ�����ṩ��򵥵�ͼ������Ϲ��ܣ������Խ����õ�ͼƬ�����������ֻ��ϡ�\n" +
						"�����еĹ��ܱ������˲˵��У������ֻ���Ļ�·��ġ�MENU���򡰡ԡ���ť��\n" +
						"�񷵻ؿ�ʼ���棬�����ֻ���Ļ�·��ķ��ؼ����ڻ�ͼģʽ����᷵�����ݱ༭���档\n" +
						"��ף��ʹ����죡");
				builder.setNegativeButton("��֪����", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				builder.show();
			}
		});
		exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Session.getSession().cleanUpSession();
				finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Session.getSession().cleanUpSession();
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
}
