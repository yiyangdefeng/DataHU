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
				builder.setTitle("使用帮助");
				builder.setMessage("●DataHU(Data Helper for You) 是一款帮助你在手机上实现简单数据处理的软件。\n" +
						"●它能够让你随时随地输入和存储获得的数据，并方便快捷地进行简单操作，得到初步信息。\n" +
						"●软件分为四个界面，开始界面，数据编辑界面，数据查看处理界面以及画图界面，力求将功能做得简单明了，提高使用的便捷性。\n" +
						"●在数据输入模式下，点击相应单元格即可进行数据输入操作，错误的输入不会被记录，该单元格的数据会被清空。\n" +
						"●在数据查看及处理界面中，你可以选择数据作为自变量或是因变量，便于之后的作图，也可以根据现有的数据按照一定公式生成新列。\n" +
						"●画图界面提供最简单的图像处理及拟合功能，并可以将画好的图片保存在您的手机上。\n" +
						"●所有的功能被放在了菜单中，请点击手机屏幕下方的“MENU”或“≡”按钮。\n" +
						"●返回开始界面，请点击手机屏幕下方的返回键，在画图模式下则会返回数据编辑界面。\n" +
						"●祝您使用愉快！");
				builder.setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
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
