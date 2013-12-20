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
	protected final int EXTRACOLUMNS = 1;
	protected final int EXTRAROWS = 4;
	private LayoutInflater mInflater;
	protected WholeSheet wholesheet;
	protected int digit;

	public GridCellAdapter(Context context, WholeSheet wholesheet, int digit) {
		mInflater = LayoutInflater.from(context);
		this.wholesheet = wholesheet;
		this.digit = digit;
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
		final int row = getRow(position);
		final int column = getColumn(position);
		TextView tv;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.textcell, null);
		}

		tv = (TextView) convertView.findViewById(R.id.textcell);
		tv.setBackgroundColor(Color.rgb(255, 255, 140));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 25);
		tv.setHeight((int) wholesheet.getHeight());
		tv.setWidth((int) wholesheet.getWidth());
		tv.setTextColor(Color.BLACK);
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
			}
		} else {
			tv.setGravity(Gravity.LEFT);
			if (row % 2 != 0) {
				tv.setBackgroundColor(Color.rgb(255, 255, 140));
			} else {
				tv.setBackgroundColor(Color.WHITE);
			}
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(digit);
			if (wholesheet.getColumn(column - EXTRACOLUMNS).getData(row -EXTRAROWS) != null ) {
				tv.setText(df.format(wholesheet.getColumn(column - EXTRACOLUMNS).getData(row - EXTRAROWS)));
			}
		}
		tv.refreshDrawableState();
		return convertView;
	}

	public int getRow(int position) {
		return (position / (wholesheet.getColumns() + EXTRACOLUMNS));
	}

	public int getColumn(int position) {
		return (position % (wholesheet.getColumns() + EXTRACOLUMNS));
	}

	public void setDigit(int digit) {
		this.digit = digit;
	}
}