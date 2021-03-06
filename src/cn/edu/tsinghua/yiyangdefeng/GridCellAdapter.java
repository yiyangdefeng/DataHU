package cn.edu.tsinghua.yiyangdefeng;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridCellAdapter extends BaseAdapter {
	protected static final int EXTRACOLUMNS = 1;
	protected static final int EXTRAROWS = 4;
	private LayoutInflater li;
	protected WholeSheet wholesheet;
	
	public GridCellAdapter(Context context, WholeSheet wholesheet) {
		li = LayoutInflater.from(context);
		this.wholesheet = wholesheet;
	}

	@Override
	public int getCount() {
		return ((wholesheet.getColumns() + EXTRACOLUMNS) * (wholesheet
				.getRows() + EXTRAROWS));
	}

	@Override
	public Object getItem(int position) {
		return wholesheet.getColumn(getColumn(position) - EXTRACOLUMNS).data
				.get(getRow(position) - EXTRAROWS);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//if (convertView == null) {
		//	convertView = li.inflate(R.layout.textcell, null);
		//}
		final int row = getRow(position);
		final int column = getColumn(position);
		final TextView tv;
		//tv = (TextView) convertView.findViewById(R.id.textcell);
		tv =new TextView(li.getContext());
		tv.setBackgroundColor(Color.rgb(255, 255, 140));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 25);
		tv.setHeight((int) wholesheet.getHeight());
		tv.setWidth((int) wholesheet.getWidth());
		tv.setTextColor(Color.BLACK);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, wholesheet.getHeight() / 2);
		if (column == 0) {
			tv.setGravity(Gravity.CENTER);
			tv.setBackgroundColor(Color.rgb(225, 225, 225));
			if (row == 0) {
				tv.setText("");
			} else if (row == 1) {
				tv.setText("变量说明");
			} else if (row == 2) {
				tv.setText("变量单位");
			} else if (row == 3) {
				tv.setText("变量类型");
			} else {
				tv.setText(String.valueOf(row + 1 - EXTRAROWS));
			}
		} else if (row == 0 || row == 1 || row == 2 || row == 3) {
			tv.setGravity(Gravity.LEFT);
			tv.setBackgroundColor(Color.rgb(225, 225, 225));
			tv.setGravity(Gravity.CENTER);
			switch (row) {
			case 0:
				tv.setText(CommonTools.ChangeNumberintoLetter(column));
				break;
			case 1:
				tv.setText(wholesheet.getColumn(column - EXTRACOLUMNS)
						.getNotes());
				break;
			case 2:
				tv.setText(wholesheet.getColumn(column - EXTRACOLUMNS)
						.getUnit());
				break;
			case 3:
				tv.setText(wholesheet.getColumn(column - EXTRACOLUMNS)
						.getType());
				break;
			}
		} else {
			tv.setGravity(Gravity.CENTER);
			if (row % 2 != 0) {
				tv.setBackgroundColor(Color.rgb(255, 255, 140));
			} else {
				tv.setBackgroundColor(Color.WHITE);
			}
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(wholesheet.getDigit());
			if (wholesheet.getColumn(column - EXTRACOLUMNS).getData(
					row - EXTRAROWS) != null) {
				tv.setText(df.format(wholesheet
						.getColumn(column - EXTRACOLUMNS).getData(
								row - EXTRAROWS)));
			}
		}
		tv.refreshDrawableState();
		return tv;
		//return convertView;
	}

	public int getRow(int position) {
		return (position / (wholesheet.getColumns() + EXTRACOLUMNS));
	}

	public int getColumn(int position) {
		return (position % (wholesheet.getColumns() + EXTRACOLUMNS));
	}	
}