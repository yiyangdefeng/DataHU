package cn.edu.tsinghua.yiyangdefeng;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.GridView;

public class EditViewActivity extends Activity {
	protected WholeSheet wholesheet;
	protected DataGridView datagridview;
	protected int titlewidth;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		wholesheet = new WholeSheet();
		setContentView(R.layout.data_gridview);
		GridView gridview = (GridView)findViewById(R.id.data_gridview);
		gridview.setNumColumns(wholesheet.getColumns() + EditCellAdapter.EXTRACOLUMNS);
		gridview.setHorizontalSpacing(1);
		gridview.setVerticalSpacing(1);
		titlewidth = 160;
		int width = wholesheet.calcWholeWidth() + titlewidth;
		//int height = wholesheet.calcWholeHeight();
		gridview.setLayoutParams(new FrameLayout.LayoutParams(width, -1));
		gridview.setAdapter(new EditCellAdapter(getApplicationContext(),wholesheet,titlewidth));
	}
}
