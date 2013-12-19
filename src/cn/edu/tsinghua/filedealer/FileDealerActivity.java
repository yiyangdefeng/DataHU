package cn.edu.tsinghua.filedealer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.edu.tsinghua.graphdealer.GraphDealActivity;
import cn.edu.tsinghua.yiyangdefeng.EditViewActivity;
import cn.edu.tsinghua.yiyangdefeng.GridViewActivity;
import cn.edu.tsinghua.yiyangdefeng.MainActivity;
import cn.edu.tsinghua.yiyangdefeng.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("DefaultLocale")
public class FileDealerActivity extends Activity {
	protected List<String> items = null;
	protected List<String> paths = null;
	protected String rootpath = Environment.getExternalStorageDirectory().getName();
	protected TextView pathtextview;
	protected EditText et;
	protected ListView lv;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fileview);
		pathtextview = (TextView) findViewById(R.id.filetextview);
		lv = (ListView)findViewById(R.id.list);
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
		lv.setAdapter(new FileAdapter(this, items, paths));
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
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
			
		});
	}

	protected void fileHandler(final File selectedfile) {
		DialogInterface.OnClickListener filelistener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("FILENAME", selectedfile.getName());
				if (which == 0) {
					intent.setClass(FileDealerActivity.this, EditViewActivity.class);
					startActivity(intent,bundle);
					finish();
				}
				if (which == 1) {
					intent.setClass(FileDealerActivity.this, GridViewActivity.class);
					startActivity(intent,bundle);
					finish();
				}
			}
		};
		String[] openway = { "�Ա༭ģʽ��", "�Բ鿴ģʽ��" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("��ѡ���ģʽ");
		builder.setItems(openway, filelistener);
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
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
