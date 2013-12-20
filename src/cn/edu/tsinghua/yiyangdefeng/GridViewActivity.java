package cn.edu.tsinghua.yiyangdefeng;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class GridViewActivity extends Activity {
	protected WholeSheet wholesheet;
	protected GridView mygridview;
	protected int titlewidth;
	protected Handler handler;
	protected DataManager dm;
	public static final int SAVE = Menu.FIRST;
	public static final int CHANGE_VARCHOICE = Menu.FIRST + 1;
	public static final int CREATENEWCOLUMN = Menu.FIRST + 2;
	public static final int SET_DIGIT = Menu.FIRST + 3;
	public static final int RETURN_EDITMODE = Menu.FIRST + 4;
	public static final int DRAW = Menu.FIRST + 5;
	protected TextView titletv;
	protected int digit = 3;
	protected GridCellAdapter gca;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		dm = new DataManager();		
		setContentView(R.layout.data_gridview);
		mygridview = new MyGridView(this);
		mygridview.setHorizontalScrollBarEnabled(true);
		mygridview.setSmoothScrollbarEnabled(true);
		mygridview.setVerticalScrollBarEnabled(true);
		mygridview.setBackgroundColor(Color.BLACK);
		mygridview.setGravity(Gravity.CENTER);
		mygridview.setHorizontalSpacing(1);
		mygridview.setVerticalSpacing(1);
		titletv = (TextView)findViewById(R.id.gridtextview);
		
		if (savedInstanceState == null) {
			wholesheet = new WholeSheet();
			mygridview.setNumColumns(wholesheet.getColumns()
					+ EditCellAdapter.EXTRACOLUMNS);

			setGridWidth();
			gca = new GridCellAdapter(getApplicationContext(),wholesheet,digit);
			mygridview.setAdapter(gca);
			FrameLayout fm = (FrameLayout) this
					.findViewById(R.id.grid_framelayout);
			fm.addView(mygridview);
			titletv.setText("数据查看处理界面-" + wholesheet.getName());
		}
		else if (savedInstanceState.getString("FILENAME") != null) {
			String filename = savedInstanceState.getString("FILENAME");
			try {
				wholesheet = dm.openFile(filename);
				mygridview.setNumColumns(wholesheet.getColumns()
						+ EditCellAdapter.EXTRACOLUMNS);

				setGridWidth();
				gca = new GridCellAdapter(getApplicationContext(),wholesheet,digit);
				mygridview.setAdapter(gca);
				FrameLayout fm = (FrameLayout) this
						.findViewById(R.id.edit_framelayout);
				fm.addView(mygridview);
				titletv.setText("数据查看处理界面-" + wholesheet.getName());
			} catch (IOException e) {
				Toast toast = Toast.makeText(GridViewActivity.this, "很抱歉，读取文件出错！", Toast.LENGTH_SHORT);
				toast.show();
			}
		}
	} 

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(GridViewActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, SAVE, Menu.NONE, "保存数据");
		menu.add(Menu.NONE, CHANGE_VARCHOICE, Menu.NONE, "设置数据类型");
		menu.add(Menu.NONE, CREATENEWCOLUMN, Menu.NONE, "通过计算生成列");
		menu.add(Menu.NONE, SET_DIGIT, Menu.NONE, "设置小数位数");
		menu.add(Menu.NONE, RETURN_EDITMODE, Menu.NONE, "回到编辑模式");
		menu.add(Menu.NONE, DRAW, Menu.NONE, "画图");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		AlertDialog.Builder builder;
		switch (item.getItemId()) {
		case SAVE:
			File file = new File(Environment.getExternalStorageDirectory().getName() + "/DataHU");
			file.mkdir();
			String filepath = file.getName() + wholesheet.getName() + ".csv";
			try {
				dm.saveFile(wholesheet, filepath);
				Toast toast = Toast.makeText(GridViewActivity.this,"保存文件" + filepath + "成功",Toast.LENGTH_LONG);
				toast.show();
			} catch (IOException e) {
				Toast toast = Toast.makeText(GridViewActivity.this, "很抱歉，存储文件出错！",Toast.LENGTH_LONG);
				toast.show();
			}
			return true;
		case RETURN_EDITMODE:
			gotoEditMode();
			return true;
		case SET_DIGIT:
			builder = new AlertDialog.Builder(GridViewActivity.this);
			builder.setTitle("请输入小数位数，范围0~6");
			final EditText et = new EditText(GridViewActivity.this);
			et.setText(String.valueOf(wholesheet.getWidth()));
			et.setFocusable(true);
			et.setClickable(true);
			builder.setView(et);
			builder.setCancelable(true);
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
							int newdigit = Integer.parseInt(et.getText().toString());
								if(newdigit < 0 || newdigit > 6) {
									Toast toast = Toast.makeText(GridViewActivity.this, "位数不符合要求", Toast.LENGTH_SHORT);
									toast.show();
								}
								else {
									gca.setDigit(newdigit);
									gca.notifyDataSetChanged();
								}
							}
							catch (NumberFormatException nfe) {
								Toast toast = Toast.makeText(GridViewActivity.this, "您的输入有误", Toast.LENGTH_SHORT);
								toast.show();
							}
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.show();
			return true;
		/*case CHANGE_VARCHOICE:
			builder = new AlertDialog.Builder(EditViewActivity.this);
			builder.setTitle("请选择需要删除的列");
			String[] currentcolumns = new String[wholesheet.getColumns()];
			for (int i = 0; i < wholesheet.getColumns(); i++) {
				currentcolumns[i] = String.valueOf(CommonTools
						.ChangeNumberintoLetter(i + 1));
			}
			builder.setItems(currentcolumns,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							wholesheet.eraseColumn(which);
							mygridview.setNumColumns(wholesheet.getColumns()
									+ EditCellAdapter.EXTRACOLUMNS);
							setGridWidth();
						}

					});
			builder.setCancelable(true);
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.show();
			return true;
		case CREATEROW:
			builder = new AlertDialog.Builder(EditViewActivity.this);
			builder.setTitle("请选择插入的位置的前一行，或者选择插在末尾");
			currentrows = new String[wholesheet.getRows()];
			for (int i = 0; i < wholesheet.getRows(); i++) {
				currentrows[i] = String.valueOf(i + 1);
			}
			builder.setItems(currentrows,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							wholesheet.insertRow(which);
							eca.notifyDataSetChanged();
						}

					});
			builder.setCancelable(true);
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.setPositiveButton("插在末尾",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							wholesheet.insertRow();
							eca.notifyDataSetChanged();
						}
					});
			builder.show();
			return true;
		case CREATECOLUMN:
			builder = new AlertDialog.Builder(EditViewActivity.this);
			builder.setTitle("请选择插入的位置的前一列，或者选择插在末尾");
			currentcolumns = new String[wholesheet.getColumns()];
			for (int i = 0; i < wholesheet.getColumns(); i++) {
				currentcolumns[i] = String.valueOf(CommonTools
						.ChangeNumberintoLetter(i + 1));
			}
			builder.setItems(currentcolumns,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							wholesheet.insertColumn(which);
							mygridview.setNumColumns(wholesheet.getColumns()
									+ EditCellAdapter.EXTRACOLUMNS);
							setGridWidth();
						}

					});
			builder.setCancelable(true);
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.setPositiveButton("插在末尾",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							wholesheet.insertColumn();
							mygridview.setNumColumns(wholesheet.getColumns()
									+ EditCellAdapter.EXTRACOLUMNS);
							setGridWidth();
						}
					});
			builder.show();
			return true;*/
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void setGridWidth() {
		int width = wholesheet.calcWholeWidth() + (int) wholesheet.getWidth()
				* EditCellAdapter.EXTRACOLUMNS;
		mygridview.setLayoutParams(new FrameLayout.LayoutParams(width, -1));
	}
	
	public void gotoEditMode() {
		Intent intent = new Intent();
		intent.setClass(GridViewActivity.this, EditViewActivity.class);
		startActivity(intent);
		finish();
	}
}
