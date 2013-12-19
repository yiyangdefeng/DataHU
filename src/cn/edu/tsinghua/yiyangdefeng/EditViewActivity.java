package cn.edu.tsinghua.yiyangdefeng;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import cn.edu.tsinghua.graphdealer.GraphDealActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

public class EditViewActivity extends Activity {
	protected WholeSheet wholesheet;
	protected MyGridView mygridview;
	protected int titlewidth;
	protected final int SAVE = Menu.FIRST;
	protected final int OBSERVATIONMODE = Menu.FIRST + 1;
	protected final int ERASEROW = Menu.FIRST + 2;
	protected final int ERASECOLUMN = Menu.FIRST + 3;
	protected final int CREATEROW = Menu.FIRST + 4;
	protected final int CREATECOLUMN = Menu.FIRST + 5;
	protected final int DRAW = Menu.FIRST + 6;
	protected DataManager dm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		dm = new DataManager();		
		setContentView(R.layout.data_editview);
		mygridview = new MyGridView(this);
		mygridview.setHorizontalScrollBarEnabled(true);
		mygridview.setSmoothScrollbarEnabled(true);
		mygridview.setVerticalScrollBarEnabled(true);
		mygridview.setBackgroundColor(Color.BLACK);
		mygridview.setGravity(Gravity.CENTER);
		mygridview.setHorizontalSpacing(1);
		mygridview.setVerticalSpacing(1);
		if (savedInstanceState == null) {
			wholesheet = new WholeSheet();
			mygridview.setNumColumns(wholesheet.getColumns()
					+ EditCellAdapter.EXTRACOLUMNS);

			setGridWidth();
			mygridview.setAdapter(new EditCellAdapter(getApplicationContext(),
					wholesheet));
			FrameLayout fm = (FrameLayout) this
					.findViewById(R.id.edit_framelayout);
			fm.addView(mygridview);
		}
		else if (savedInstanceState.getString("FILENAME") != null) {
			String filename = savedInstanceState.getString("FILENAME");
			try {
				wholesheet = dm.openFile(filename);
				mygridview.setNumColumns(wholesheet.getColumns()
						+ EditCellAdapter.EXTRACOLUMNS);

				setGridWidth();
				mygridview.setAdapter(new EditCellAdapter(getApplicationContext(),
						wholesheet));
				FrameLayout fm = (FrameLayout) this
						.findViewById(R.id.edit_framelayout);
				fm.addView(mygridview);
			} catch (IOException e) {
				Toast toast = new Toast(this);
				toast.setText("很抱歉，读取文件出错！");
				toast.show();
			}
	}
		else {
			throw new RuntimeException("Failure in creating editview!");
		}
	}

	public void setGridWidth() {
		int width = wholesheet.calcWholeWidth() + (int) wholesheet.getWidth()
				* EditCellAdapter.EXTRACOLUMNS;
		mygridview.setLayoutParams(new FrameLayout.LayoutParams(width, -1));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, SAVE, Menu.NONE, "保存数据");
		menu.add(Menu.NONE, OBSERVATIONMODE, Menu.NONE, "转入查看模式");
		menu.add(Menu.NONE, ERASEROW, Menu.NONE, "删除行");
		menu.add(Menu.NONE, ERASECOLUMN, Menu.NONE, "删除列");
		menu.add(Menu.NONE, CREATEROW, Menu.NONE, "插入行");
		menu.add(Menu.NONE, CREATECOLUMN, Menu.NONE, "插入列");
		menu.add(Menu.NONE, DRAW, Menu.NONE, "画图");
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		AlertDialog.Builder builder;
		switch (item.getItemId()) {
		case SAVE:
			File file = new File(Environment.getExternalStorageDirectory().getName() + "/DataHU");
			file.mkdir();
			Log.e("test",file.getName());
			Date date = new Date();
			String filepath = file.getName() + "/sheetdata" + String.valueOf(date.getDate()) + ".csv";
			Log.e("test",filepath);
			try {
				dm.saveFile(wholesheet, filepath);
				Toast toast = Toast.makeText(EditViewActivity.this,"保存文件" + filepath + "成功",Toast.LENGTH_LONG);
				toast.show();
			} catch (IOException e) {
				throw new RuntimeException(e);
				//Toast toast = Toast.makeText(EditViewActivity.this, "很抱歉，存储文件出错！",Toast.LENGTH_LONG);
				//toast.show();
			}
			return true;
		case OBSERVATIONMODE:
			gotoObserveMode();
			return true;
		case ERASEROW:
			builder = new AlertDialog.Builder(EditViewActivity.this);
			builder.setTitle("请选择需要删除的行");
			String[] currentrows = new String[wholesheet.getRows()];
			for (int i = 0; i < wholesheet.getRows(); i++) {
				currentrows[i] = String.valueOf(i + 1);
			}

			builder.setItems(currentrows,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							wholesheet.eraseRow(which);
							mygridview.setNumColumns(wholesheet.getColumns()
									+ EditCellAdapter.EXTRACOLUMNS);
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
		case ERASECOLUMN:
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
							mygridview.setNumColumns(wholesheet.getColumns()
									+ EditCellAdapter.EXTRACOLUMNS);
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
							mygridview.setNumColumns(wholesheet.getColumns()
									+ EditCellAdapter.EXTRACOLUMNS);
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
			return true;
		case DRAW:
			Intent intent = new Intent();
			intent.setClass(EditViewActivity.this, GraphDealActivity.class);
			startActivity(intent);
			finish();
			/*builder = new AlertDialog.Builder(EditViewActivity.this);
			builder.setTitle("提示");
			builder.setMessage("要想使用画图功能，需要首先退出数据编辑模式进入数据查看模式，是否执行？");
			builder.setCancelable(true);
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							gotoObserveMode();
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.show();
			return true;*/
		}
		return super.onOptionsItemSelected(item);
	}

	public void gotoObserveMode() {
		Intent intent = new Intent();
		intent.setClass(EditViewActivity.this, GridViewActivity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(EditViewActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
