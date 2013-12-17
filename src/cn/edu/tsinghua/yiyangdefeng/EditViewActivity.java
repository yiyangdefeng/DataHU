package cn.edu.tsinghua.yiyangdefeng;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class EditViewActivity extends Activity {
	protected WholeSheet wholesheet;
	protected MyGridView mygridview;
	protected int titlewidth;
	protected final int SAVE = Menu.FIRST;
	protected final int OBSERVATIONMODE = Menu.FIRST + 1;
	protected final int ERASEROW = Menu.FIRST + 2;
	protected final int ERASECOLUMN = Menu.FIRST + 3;
	protected final int DRAW = Menu.FIRST + 4;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		wholesheet = new WholeSheet();
		setContentView(R.layout.data_editview);
		mygridview = new MyGridView(this);
		mygridview.setHorizontalScrollBarEnabled(true);
		mygridview.setSmoothScrollbarEnabled(true);
		mygridview.setVerticalScrollBarEnabled(true);
		mygridview.setBackgroundColor(Color.BLACK);
		mygridview.setGravity(Gravity.CENTER);
		mygridview.setNumColumns(wholesheet.getColumns()
				+ EditCellAdapter.EXTRACOLUMNS);
		mygridview.setHorizontalSpacing(1);
		mygridview.setVerticalSpacing(1);
		setGridWidth();
		titlewidth = 200;
		mygridview.setAdapter(new EditCellAdapter(getApplicationContext(),
				wholesheet, titlewidth));
		FrameLayout fm = (FrameLayout)this.findViewById(R.id.edit_framelayout);
		fm.addView(mygridview);
	}
	
	public void setGridWidth () {
		int width = wholesheet.calcWholeWidth() + (int) wholesheet.getWidth()
				* EditCellAdapter.EXTRACOLUMNS;
		mygridview.setLayoutParams(new FrameLayout.LayoutParams(width, -1));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, SAVE, Menu.NONE, "��������");
		menu.add(Menu.NONE, OBSERVATIONMODE, Menu.NONE, "ת��鿴ģʽ");
		menu.add(Menu.NONE, ERASEROW, Menu.NONE, "ɾ����");
		menu.add(Menu.NONE, ERASECOLUMN, Menu.NONE, "ɾ����");
		menu.add(Menu.NONE, DRAW, Menu.NONE, "��ͼ");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		AlertDialog.Builder builder;
		switch (item.getItemId()) {
		case SAVE:
			saveWholeSheet();
			return true;
		case OBSERVATIONMODE:
			gotoObserveMode();
			return true;
		case ERASEROW:
			builder = new AlertDialog.Builder(EditViewActivity.this);
			builder.setTitle("��ѡ����Ҫɾ������");
			String[] currentrows = new String[wholesheet.getRows()];
			for (int i = 0; i < wholesheet.getRows(); i++) {
				currentrows[i] = String.valueOf(i + 1);
			}

			builder.setItems(currentrows,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							wholesheet.eraseRow(which);
						}

					});
			builder.setCancelable(true);
			builder.setNegativeButton("ȡ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.show();
			return true;
		case ERASECOLUMN:
			builder = new AlertDialog.Builder(EditViewActivity.this);
			builder.setTitle("��ѡ����Ҫɾ������");
			String[] currentcolumns = new String[wholesheet.getColumns()];
			for (int i = 0; i < wholesheet.getColumns(); i++) {
				currentcolumns[i] = String.valueOf(CommonTools.ChangeNumberintoLetter(i + 1));
			}
			builder.setItems(currentcolumns,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							wholesheet.eraseColumn(which);
							mygridview.setNumColumns(wholesheet.getColumns() + EditCellAdapter.EXTRACOLUMNS);
							setGridWidth();
						}

					});
			builder.setCancelable(true);
			builder.setNegativeButton("ȡ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.show();
			return true;
		case DRAW:
			builder = new AlertDialog.Builder(EditViewActivity.this);
			builder.setTitle("��ʾ");
			builder.setMessage("Ҫ��ʹ�û�ͼ���ܣ���Ҫ�����˳����ݱ༭ģʽ�������ݲ鿴ģʽ���Ƿ�ִ�У�");
			builder.setCancelable(true);
			builder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							gotoObserveMode();
						}
					});
			builder.setNegativeButton("ȡ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void saveWholeSheet() {

	}

	public void gotoObserveMode() {
		Intent intent = new Intent();
		intent.setClass(EditViewActivity.this, GridViewActivity.class);
		startActivity(intent);
		finish();
	}
}
