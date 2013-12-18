package cn.edu.tsinghua.filedealer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.edu.tsinghua.yiyangdefeng.EditViewActivity;
import cn.edu.tsinghua.yiyangdefeng.GridViewActivity;
import cn.edu.tsinghua.yiyangdefeng.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("DefaultLocale")
public class FileDealer extends ListActivity {
	protected List<String> items = null;
	protected List<String> paths = null;
	private String rootpath = "/";
	protected File file = Environment.getExternalStorageDirectory();
	protected TextView pathtextview;
	protected EditText et;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
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
		setListAdapter(new FileAdapter(this, items, paths));
	}

	@SuppressLint("DefaultLocale")
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
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
				Bundle bundle = new Bundle();
				bundle.putString("FILENAME", selectedfile.getName());
				if (which == 0) {
					intent.setClass(FileDealer.this, EditViewActivity.class);
					startActivity(intent,bundle);
					finish();
				}
				if (which == 1) {
					intent.setClass(FileDealer.this, GridViewActivity.class);
					startActivity(intent,bundle);
					finish();
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

}
