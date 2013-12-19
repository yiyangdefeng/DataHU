package cn.edu.tsinghua.yiyangdefeng;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.GridView;

public class GridViewActivity extends Activity {
	protected WholeSheet wholesheet;
	protected GridView mygridview;
	protected int titlewidth;
	protected Handler handler;
	public static final int SHOW_VARCHOICE = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		wholesheet = new WholeSheet();
		setContentView(R.layout.data_gridview);
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
		int width = wholesheet.calcWholeWidth() + (int) wholesheet.getWidth()
				* EditCellAdapter.EXTRACOLUMNS;
		// int height = wholesheet.calcWholeHeight();
		mygridview.setLayoutParams(new FrameLayout.LayoutParams(width, -1));
		titlewidth = 200;
		mygridview.setAdapter(new GridCellAdapter(getApplicationContext(),
				wholesheet));
		FrameLayout fm = (FrameLayout)this.findViewById(R.id.grid_framelayout);
		fm.addView(mygridview);
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
}
