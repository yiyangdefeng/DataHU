package cn.edu.tsinghua.yiyangdefeng;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CellAdapter extends BaseAdapter {
	//private Context mContext;
	private LayoutInflater mInflater;
	protected WholeSheet wholesheet;

	public CellAdapter(Context context,WholeSheet wholesheet) {
		//mContext = c;
		mInflater = LayoutInflater.from(context);
		this.wholesheet = wholesheet;
	}

	@Override
	public int getCount() {
		return (wholesheet.getColumns() * wholesheet.getMaxRowNumber());
	}

	@Override
	public Object getItem(int position) {
		return null; // do nothing now
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int row = getRow(position); 
		int column = getColumn(position);

		TextView tv;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.imagecell, null);
		}
		tv = (TextView) convertView.findViewById(R.id.textcell); 
		tv.setWidth((int)(wholesheet.getColumn(0).getWidth()));
		tv.setHeight((int)wholesheet.getHeight());
		tv.setBackgroundColor(Color.WHITE);
		tv.setTextColor(Color.BLACK);
		tv.setText(row + "," + column);
		tv.refreshDrawableState();

		return convertView;
	}

	public int getRow(int position) {
		return (position / wholesheet.getColumns());
	}

	public int getColumn(int position) {
		return (position % wholesheet.getColumns());
	}
}