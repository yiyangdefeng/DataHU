package cn.edu.tsinghua.yiyangdefeng;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.GridView;

public class GridViewActivity extends Activity {
	protected WholeSheet wholesheet;
	protected GridView mygridview;
	protected int titlewidth;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		mygridview.setAdapter(new EditCellAdapter(getApplicationContext(),
				wholesheet, titlewidth));
		FrameLayout fm = (FrameLayout)this.findViewById(R.id.grid_framelayout);
		fm.addView(mygridview);
	} 

}
