package cn.edu.tsinghua.yiyangdefeng;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CellAdapter extends BaseAdapter {
	protected final int EXTRACOLUMNS = 1;
	protected final int EXTRAROWS = 1;
	// private Context mContext;
	private LayoutInflater mInflater;
	protected WholeSheet wholesheet;
	protected int titlewidth;
	

	public CellAdapter(Context context, WholeSheet wholesheet) {
		// mContext = c;
		mInflater = LayoutInflater.from(context);
		this.wholesheet = wholesheet;
	}

	@Override
	public int getCount() {
		return ((wholesheet.getColumns() + EXTRACOLUMNS) * (wholesheet.getRows()+ EXTRAROWS));
	}
	
	public void setItem(int column,int row,long newdata) {
		wholesheet.getColumn(column).data.set(row,newdata); 
	}

	@Override
	public Object getItem(int position) {
		return wholesheet.getColumn(getColumn(position) - EXTRACOLUMNS).data.get(getRow(position) - EXTRAROWS);
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
			convertView = mInflater.inflate(R.layout.textcell, null);
		
		tv = (TextView) convertView.findViewById(R.id.textcell);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,40);
		tv.setHeight((int) wholesheet.getHeight());
		tv.setTextColor(Color.BLACK);
		if (column == 0) {
			tv.setWidth((int) (wholesheet.getColumn(0).getWidth()));
			tv.setBackgroundColor(Color.GRAY);
			if (row == 0) {
				tv.setText("");
			} else {
				tv.setText(String.valueOf(row));
			}
		} else if (row == 0) {
			tv.setBackgroundColor(Color.GRAY);
			tv.setWidth((int) (wholesheet.getColumn(column - EXTRACOLUMNS).getWidth()));
			tv.setText(CommonTools.ChangeNumberintoLetter(column));
		} else {
			tv.setWidth((int) (wholesheet.getColumn(0).getWidth()));
			tv.setBackgroundColor(Color.WHITE);
			tv.setText(row + "," + column);
		}
		tv.refreshDrawableState();
		}
		return convertView;
	}

	public int getRow(int position) {
		return (position / (wholesheet.getColumns() + EXTRACOLUMNS));
	}

	public int getColumn(int position) {
		return (position % (wholesheet.getColumns() + EXTRACOLUMNS));
	}

	
}