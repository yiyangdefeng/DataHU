package cn.edu.tsinghua.filedealer;

import java.io.File;
import java.util.List;

import cn.edu.tsinghua.yiyangdefeng.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
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
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = li.inflate(R.layout.filecell, null);
			holder = new ViewHolder();
			holder.tv = (TextView)convertView.findViewById(R.id.filecell);
			convertView.setTag(holder);
		}
		
		else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		File file = new File(paths.get(position).toString());
		if(items.get(position).toString().equals("root")) {
			holder.tv.setText("��Ŀ¼");
		} else if (items.get(position).toString().equals("up")) {
			holder.tv.setText("��һ��");
		} else {
			//String filename = file.getName();
			String filename = items.get(position);
			String type = filename.substring(filename.lastIndexOf(".")+1,filename.length()).toLowerCase();
			if (file.isDirectory()) {
				holder.tv.setText(file.getName());
			}
			else if (type.equals("csv") || type.equals("txt")) {
				holder.tv.setText(filename);
			}
			else {
				holder.tv.setText("��֧�ֵ��ļ���ʽ");
			}
			
		}
		return convertView;
	}
	
	protected class ViewHolder {
		TextView tv;
	}
}
