package cn.edu.tsinghua.yiyangdefeng;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class GridCellAdapter extends BaseAdapter {
	protected final int EXTRACOLUMNS = 1;
	protected final int EXTRAROWS = 4;
	private LayoutInflater mInflater;
	protected WholeSheet wholesheet;

	public GridCellAdapter(Context context, WholeSheet wholesheet) {
		mInflater = LayoutInflater.from(context);
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

	static java.util.Map<Integer,Boolean> G = null;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int row = getRow(position);
		final int column = getColumn(position);
		final int _pos = position;
		if (G == null) {
			G = new java.util.HashMap<Integer, Boolean>();
		}
		if (G.get(position) != null) {
			if (convertView == null) {
				throw new RuntimeException();
			}
		} else {
			G.put(position, true);
		}
		if(!(column != 0 && row == 3)) {
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
				tv.setBackgroundColor(Color.rgb(225,225,225));
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
				tv.setBackgroundColor(Color.rgb(225,225,225));
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
				}
			} else {
				tv.setGravity(Gravity.LEFT);
				if (row % 2 != 0) {
				tv.setBackgroundColor(Color.rgb(255, 255, 140));
				}
				else {
					tv.setBackgroundColor(Color.WHITE);
				}
				tv.setText(getRow(position) + "," + getColumn(position));
			}
			tv.refreshDrawableState();
		}
		else {
			if (convertView == null) {
				convertView = mInflater
						.inflate(R.layout.spinner_gridview, null);
			}
			Spinner gridspinner = (Spinner) convertView
					.findViewById(R.id.gridspinner);
			BaseAdapter ba = new BaseAdapter() {
				public int getCount() {
					return VarType.values().length;
				}

				public Object getItem(int position) {
					return null;
				}

				public long getItemId(int position) {
					return position;
				}

				public View getView(int position, View convertView,
						ViewGroup parent) {

					if (convertView == null) {
						convertView = mInflater
								.inflate(R.layout.textcell, null);
					}
					convertView.setBackgroundColor(Color.rgb(255, 255, 140));
					TextView tv = (TextView) convertView
							.findViewById(R.id.textcell);
					tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,25);
					tv.setHeight((int) wholesheet.getHeight());
					tv.setWidth((int) wholesheet.getWidth());
					tv.setTextColor(Color.BLACK);
					tv.setGravity(Gravity.CENTER);
					
					switch (position) {
					case 0:
						tv.setText("X," + _pos + "," + row + "," + column);
						break;
					case 1:
						tv.setText("Y," + _pos + "," + row + "," + column);
						break;
					}
					return convertView;
				}
			};
			ViewGroup.LayoutParams lp = gridspinner.getLayoutParams();
			lp.width = (int)wholesheet.getWidth();
			lp.height = (int)wholesheet.getHeight();
			gridspinner.setLayoutParams(lp);
			gridspinner.setBackgroundColor(Color.rgb(255, 255, 140));
			gridspinner.setAdapter(ba);
			gridspinner
					.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int position, long arg3) {
							switch (position) {
							case 0:
								wholesheet.getColumn(column - EXTRACOLUMNS).setType(VarType.X);
								break;
							case 1:
								wholesheet.getColumn(column - EXTRACOLUMNS).setType(VarType.Y);
								break;
							}

						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}

					});
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