package cn.edu.tsinghua.yiyangdefeng;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CellAdapter extends BaseAdapter {
	// private Context mContext;
	private LayoutInflater mInflater;
	protected WholeSheet wholesheet;

	public CellAdapter(Context context, WholeSheet wholesheet) {
		// mContext = c;
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
		tv.setTextSize(10);
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
			tv.setWidth((int) (wholesheet.getColumn(column).getWidth()));
			tv.setText(ChangeNumberintoLetter(column));
		} else {
			tv.setWidth((int) (wholesheet.getColumn(0).getWidth()));
			tv.setBackgroundColor(Color.WHITE);
			tv.setText(row + "," + column);
		}
		tv.refreshDrawableState();
		return convertView;
	}

	public int getRow(int position) {
		return (position / wholesheet.getColumns());
	}

	public int getColumn(int position) {
		return (position % wholesheet.getColumns());
	}

	public String ChangeNumberintoLetter(int number) {
		if (number > 0 && number < 27) {
			return String.valueOf((char) (number + 64));
		} else if (number > 26 && number < 26 * 27 + 1) {
			char[] tempchar = new char[2];
			tempchar[1] = (char) ((number % 26) + 65);
			if (tempchar[1] == '@') {
				tempchar[1] = 'Z';
			}
			tempchar[0] = (char) ((number - number % 26) / 26 + 64);
			return String.valueOf(tempchar);
		} else
			return "Unknown";
	}
}