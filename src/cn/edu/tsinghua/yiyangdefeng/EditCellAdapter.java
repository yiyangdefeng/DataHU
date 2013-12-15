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
	protected static final int EXTRAROWS = 1; 
	protected int titlewidth;
		
	public EditCellAdapter (Context context,WholeSheet wholesheet,int titlewidth ) {
		li = LayoutInflater.from(context);
		this.wholesheet = wholesheet;
		this.titlewidth = titlewidth;
	}
	
	public EditCellAdapter(Context context) {
		li = LayoutInflater.from(context);
		wholesheet = new WholeSheet();
		titlewidth = 160;
	}
	
	@Override
	public int getCount() {
		return ((wholesheet.getColumns() + EXTRACOLUMNS) * (wholesheet.getMaxRowNumber() + EXTRAROWS));
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
	public View getView(int position,  View convertView, ViewGroup parent) {
		final int row = getRow(position);
		final int column = getColumn(position);
		
		final EditText et;
		if (convertView == null) {
			convertView = li.inflate(R.layout.editcell, null);
		}
		final View finalConvertView = convertView;
		et = (EditText) convertView.findViewById(R.id.edittext);
		et.setTextSize(TypedValue.COMPLEX_UNIT_PX,25);
		et.setHeight((int) wholesheet.getHeight());
		et.setTextColor(Color.BLACK);
		if (column == 0) {
			et.setWidth(titlewidth);
			et.setBackgroundColor(Color.GRAY);
			et.setClickable(false);
			et.setFocusable(false);
			et.setLongClickable(false);
			et.setGravity(Gravity.CENTER);
			if (row == 0) {
				et.setText("");
			} else {
				et.setText(String.valueOf(row));
			}
		} else if (row == 0) {
			et.setClickable(false);
			et.setFocusable(false);
			et.setLongClickable(false);
			et.setBackgroundColor(Color.GRAY);
			et.setGravity(Gravity.CENTER);
			et.setWidth((int) (wholesheet.getColumn(column - EXTRACOLUMNS).getWidth()));
			et.setText(CommonTools.ChangeNumberintoLetter(column));
		} else {
			et.setFocusable(true);
			et.setClickable(true);
			et.setWidth((int) (wholesheet.getColumn(column - EXTRACOLUMNS).getWidth()));
			et.setBackgroundColor(Color.WHITE);
			et.setGravity(Gravity.FILL);
			et.setText(row + "," + column);
			//et.setText(String.valueOf(wholesheet.columndata.get(column - EXTRACOLUMNS).getData(row - EXTRAROWS)));
			et.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if (!hasFocus) {
						try {
							final long newdata = Long.parseLong(et.getText().toString().replaceAll("\\s", ""));
							setItem(column - EXTRACOLUMNS,row - EXTRAROWS,newdata);
							if (!Long.toString(newdata).equals(et.getText().toString())) {
								finalConvertView.post(new Runnable() {
									@Override
									public void run() {
										// TODO Auto-generated method stub
										et.setText(Long.toString(newdata));
									}
									
								});
							}
						}
						catch (NumberFormatException nfe) {
							StringWriter sw = new StringWriter();
							PrintWriter pw = new PrintWriter(sw);
							nfe.printStackTrace(pw);
							pw.flush();
							Log.e("q",sw.toString());
							finalConvertView.post(new Runnable() {
								@Override
								public void run() {
									//throw a new warning dialog to remind the error input
									// TODO Auto-generated method stub
									et.setText("0");
									setItem(column - EXTRACOLUMNS,row - EXTRAROWS,0l);
								}
								
							});
						}
					}
					
				}
				
			});
			/*et.addTextChangedListener(new TextWatcher() {
				@Override
				public void afterTextChanged(Editable editable) {
					Log.e("test",editable.toString());
					);
						
						
					}
				}
				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
				}
				@Override
				public void onTextChanged(CharSequence s, int start,
						int end, int after) {
					
				}
			});*/
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
