package cn.edu.tsinghua.filedealer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.edu.tsinghua.yiyangdefeng.DataManager;
import cn.edu.tsinghua.yiyangdefeng.EditViewActivity;
import cn.edu.tsinghua.yiyangdefeng.GridViewActivity;
import cn.edu.tsinghua.yiyangdefeng.MainActivity;
import cn.edu.tsinghua.yiyangdefeng.R;
import cn.edu.tsinghua.yiyangdefeng.Session;
import cn.edu.tsinghua.yiyangdefeng.WholeSheet;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("DefaultLocale")
public class FileDealerActivity extends ListActivity {
	protected List<String> items = null;
	protected List<String> paths = null;
	protected String rootpath = Environment.getExternalStorageDirectory()
			.getName();
	protected TextView pathtextview;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.fileview);
		pathtextview = (TextView) findViewById(R.id.filetextview);
		getFileDir(rootpath);
	}

	protected void getFileDir(String filepath) {
		pathtextview.setText(filepath);
		items = new ArrayList<String>();
		paths = new ArrayList<String>();
		File myfile = new File(filepath);
		File[] files = myfile.listFiles();
		if (!filepath.equals(rootpath)) {
			items.add("root");
			paths.add(rootpath);
			items.add("up");
			paths.add(myfile.getParent());
		}
		for (int i = 0; i < files.length; i++) {
			File tempfile = files[i];
			items.add(tempfile.getName());
			paths.add(tempfile.getPath());
		}
		FileAdapter fa = new FileAdapter(this, items, paths);
		this.setListAdapter(fa);
	}

	@Override
	protected void onListItemClick(ListView l, View arg1, int position, long id) {
		Log.e("test", "touch valid at:" + position);
		File selectedfile = new File(paths.get(position));
		if (selectedfile.isDirectory()) {
			getFileDir(paths.get(position));
		} else {
			String filename = selectedfile.getName();
			String type = filename.substring(filename.lastIndexOf(".") + 1,
					filename.length()).toLowerCase();
			if (type.equals("csv") || type.equals("txt")) {
				fileHandler(selectedfile);
			}
		}
	}

	protected void fileHandler(final File selectedfile) {
		DialogInterface.OnClickListener filelistener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
				DataManager dm = new DataManager();
				try {
					WholeSheet wholesheet = dm.openFile(selectedfile.getName());
					Session.getSession().cleanUpSession();
					Session session = Session.getSession();
					session.put("wholesheet", wholesheet);
					if (which == 0) {
						intent.setClass(FileDealerActivity.this,
								EditViewActivity.class);
						startActivity(intent);
						finish();
					}
					if (which == 1) {
						intent.setClass(FileDealerActivity.this,
								GridViewActivity.class);
						startActivity(intent);
						finish();
					}
				} catch (Exception e) {
					e.printStackTrace();
					Toast toast = Toast.makeText(FileDealerActivity.this,
							"读取文件出现问题，请确认文件格式，并确认此文件确实由本软件保存。",
							Toast.LENGTH_LONG);
					toast.show();
				}

			}
		};
		String[] openway = { "以编辑模式打开", "以查看模式打开" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("请选择打开模式");
		builder.setItems(openway, filelistener);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(FileDealerActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
