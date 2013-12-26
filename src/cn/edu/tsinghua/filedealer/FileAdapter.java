package cn.edu.tsinghua.filedealer;

import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("DefaultLocale")
public class FileAdapter extends BaseAdapter {
	protected LayoutInflater li;
	protected List<String> items;
	protected List<String> paths;
	
	public FileAdapter(Context context,List<String> items,List<String> paths) {
		li = LayoutInflater.from(context);
		this.items = items;
		this.paths = paths;
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tv = new TextView(li.getContext());
		tv.setWidth(LayoutParams.WRAP_CONTENT);
		tv.setTextColor(Color.BLACK);
		tv.setText("testtsasda");
		return tv;
		//if (convertView == null) {
		//	convertView = li.inflate(R.layout.filecell,null);
		//}
		//tv = (TextView) convertView.findViewById(R.id.filecell);
		//File file = new File(paths.get(position).toString());
		/*if(items.get(position).toString().equals("root")) {
			tv.setText("根目录");
		} else if (items.get(position).toString().equals("up")) {
			tv.setText("上一级");
		} else {
			//String filename = file.getName();
			String filename = items.get(position);
			String type = filename.substring(filename.lastIndexOf(".")+1,filename.length()).toLowerCase();
			if (type.equals("csv") || type.equals("txt")) {
				tv.setText(filename);
			}
			else {
				tv.setText("不支持的文件格式");
			}
			
		}
		Log.e("test",tv.getText().toString());
		return tv;*/
	}
}
