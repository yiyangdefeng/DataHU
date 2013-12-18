package cn.edu.tsinghua.yiyangdefeng;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

public class EditCellAdapter extends BaseAdapter {
	protected WholeSheet wholesheet;
	private LayoutInflater li;
	protected static final int EXTRACOLUMNS = 1;
	protected static final int EXTRAROWS = 3;

	public EditCellAdapter(Context context, WholeSheet wholesheet) {
		li = LayoutInflater.from(context);
		this.wholesheet = wholesheet;

	}

	public EditCellAdapter(Context context) {
		li = LayoutInflater.from(context);
		wholesheet = new WholeSheet();
	}

	@Override
	public int getCount() {
		return ((wholesheet.getColumns() + EXTRACOLUMNS) * (wholesheet
				.getRows() + EXTRAROWS));
	}

	public void setItem(int column, int row, long newdata) {
		wholesheet.getColumn(column).data.set(row, newdata);
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

		if (convertView == null) {
			convertView = li.inflate(R.layout.editcell, null);
		}
		final int row = getRow(position);
		final int column = getColumn(position);

		final View finalConvertView = convertView;
		final EditText et;
		et = (EditText) convertView.findViewById(R.id.edittext);
		et.setTextSize(TypedValue.COMPLEX_UNIT_PX, 25);
		et.setHeight((int) wholesheet.getHeight());
		et.setWidth((int) wholesheet.getWidth());
		et.setTextColor(Color.BLACK);
		if (column == 0) {
			et.setBackgroundColor(Color.rgb(225,225,225));
			et.setClickable(false);
			et.setFocusable(false);
			et.setLongClickable(false);
			et.setGravity(Gravity.CENTER);
			if (row == 0) {
				et.setText("");
			} else if (row == 1) {
				et.setText("变量说明");
			} else if (row == 2) {
				et.setText("单位");
			} else {
				et.setText(String.valueOf(row + 1 - EXTRAROWS));
			}
		} else if (row == 0 || row == 1 || row == 2) {
			et.setBackgroundColor(Color.rgb(225,225,225));
			et.setGravity(Gravity.CENTER);
			switch (row) {
			case 0:
				et.setClickable(false);
				et.setFocusable(false);
				et.setLongClickable(false);
				et.setText(CommonTools.ChangeNumberintoLetter(column + 1
						- EXTRACOLUMNS));
				break;
			case 1:
				et.setClickable(true);
				et.setFocusable(true);
				et.setText(wholesheet.getColumn(column - EXTRACOLUMNS)
						.getNotes());
				et.setOnFocusChangeListener(new OnFocusChangeListener() {
					@Override
					public void onFocusChange(View arg0, boolean hasfocus) {
						if (!hasfocus) {
							wholesheet.getColumn(column - EXTRACOLUMNS)
									.setNotes(et.getText().toString());
						}
					}

				});
				break;
			case 2:
				et.setClickable(true);
				et.setFocusable(true);
				et.setText(wholesheet.getColumn(column - EXTRACOLUMNS)
						.getUnit());
				et.setOnFocusChangeListener(new OnFocusChangeListener() {
					@Override
					public void onFocusChange(View arg0, boolean hasfocus) {
						if (!hasfocus) {
							wholesheet.getColumn(column - EXTRACOLUMNS)
									.setUnit(et.getText().toString());
						}
					}
				});
				break;
			}
		} else {
			et.setFocusable(true);
			et.setClickable(true);
			if (row % 2 == 0) {
				et.setBackgroundColor(Color.WHITE);
			} else {
				et.setBackgroundColor(Color.rgb(200, 255, 255));
			}
			et.setGravity(Gravity.FILL);
			if (wholesheet.columndata.get(column - EXTRACOLUMNS).getData(
					row - EXTRAROWS) != null) {
				et.setText(String.valueOf(wholesheet.columndata.get(
						column - EXTRACOLUMNS).getData(row - EXTRAROWS)));
			} else {
				et.setText("");
			}
			et.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if (!hasFocus) {
						if (et.getText().toString() != "") {
							try {
								final long newdata = (long) Float.parseFloat(et
										.getText().toString()
										.replaceAll("\\s", ""));
								setItem(column - EXTRACOLUMNS, row - EXTRAROWS,
										newdata);
								if (!Long.toString(newdata).equals(
										et.getText().toString())) {
									finalConvertView.post(new Runnable() {
										@Override
										public void run() {
											// TODO Auto-generated method stub
											et.setText(Long.toString(newdata));
										}

									});
								}
							} catch (NumberFormatException nfe) {
								StringWriter sw = new StringWriter();
								PrintWriter pw = new PrintWriter(sw);
								nfe.printStackTrace(pw);
								pw.flush();
								Log.e("q", sw.toString());
								finalConvertView.post(new Runnable() {
									@Override
									public void run() {
										// throw a new warning dialog to remind
										// the error input
										// TODO Auto-generated method stub
										et.setText("");
										wholesheet
												.cleardata(column
														- EXTRACOLUMNS, row
														- EXTRAROWS);
									}

								});
							}
						}
					}

				}
			});
		}
		et.refreshDrawableState();

		return convertView;
	}

	public int getRow(int position) {
		return (position / (wholesheet.getColumns() + EXTRACOLUMNS));
	}

	public int getColumn(int position) {
		return (position % (wholesheet.getColumns() + EXTRACOLUMNS));
	}

}
