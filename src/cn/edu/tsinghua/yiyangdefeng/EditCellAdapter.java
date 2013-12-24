package cn.edu.tsinghua.yiyangdefeng;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Color;
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

	public void setItem(int column, int row, double newdata) {
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

		//if (convertView == null) {
		//	convertView = li.inflate(R.layout.editcell, null);
		//}
		final int row = getRow(position);
		final int column = getColumn(position);

		//final View finalConvertView = convertView;
		final EditText et;
		et = new EditText(li.getContext());
		//(EditText) convertView.findViewById(R.id.edittext);
		et.setTextSize(TypedValue.COMPLEX_UNIT_PX, wholesheet.getHeight() / 2);
		et.setHeight((int) wholesheet.getHeight());
		et.setWidth((int) wholesheet.getWidth());
		et.setTextColor(Color.BLACK);
		if (column == 0) {
			et.setBackgroundColor(Color.rgb(225, 225, 225));
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
			et.setBackgroundColor(Color.rgb(233, 233, 233));
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
							et.setBackgroundColor(Color.rgb(233, 233, 233));
						}
						if (hasfocus) {
							et.setBackgroundColor(Color.rgb(255, 210, 166));
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
							et.setBackgroundColor(Color.rgb(233, 233, 233));
						}
						if (hasfocus) {
							et.setBackgroundColor(Color.rgb(255, 210, 166));
						}
					}
				});
				break;
			}
		} else {
			et.setFocusable(true);
			et.setClickable(true);
			final boolean iseven;
			if (row % 2 == 0) {
				et.setBackgroundColor(Color.WHITE);
				iseven = true;
			} else {
				et.setBackgroundColor(Color.rgb(225, 255, 255));
				iseven = false;
			}
			et.setGravity(Gravity.FILL);
			if (wholesheet.columndata.get(column - EXTRACOLUMNS).getData(
					row - EXTRAROWS) != null) {
				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(wholesheet.getDigit());
				et.setText(df.format(wholesheet.columndata.get(
						column - EXTRACOLUMNS).getData(row - EXTRAROWS)));
			} else {
				et.setText("");
			}
			et.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if (!hasFocus) {
						if (iseven) {
							et.setBackgroundColor(Color.WHITE);
						} else {
							et.setBackgroundColor(Color.rgb(225, 255, 255));
						}
					}
					if (hasFocus) {
						et.setBackgroundColor(Color.rgb(255, 210, 166));
					}
					if (!hasFocus) {
						if (et.getText().toString() != "") {
							try {
								final double newdata = (double) Float
										.parseFloat(et.getText().toString()
												.replaceAll("\\s", ""));
								setItem(column - EXTRACOLUMNS, row - EXTRAROWS,
										newdata);
								if (!Double.toString(newdata).equals(
										et.getText().toString())) {
										et.post(new Runnable() {
										@Override
										public void run() {
											// TODO Auto-generated method stub
											DecimalFormat df = new DecimalFormat();
											df.setMaximumFractionDigits(wholesheet.getDigit());
											et.setText(df.format(newdata));
										}

									});
								}
							} catch (NumberFormatException nfe) {
								StringWriter sw = new StringWriter();
								PrintWriter pw = new PrintWriter(sw);
								pw.flush();
								et.post(new Runnable() {
									@Override
									public void run() {
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

		return et;
	}

	public int getRow(int position) {
		return (position / (wholesheet.getColumns() + EXTRACOLUMNS));
	}

	public int getColumn(int position) {
		return (position % (wholesheet.getColumns() + EXTRACOLUMNS));
	}

}
