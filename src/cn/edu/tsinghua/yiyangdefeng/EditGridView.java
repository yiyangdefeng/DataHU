package cn.edu.tsinghua.yiyangdefeng;

import android.widget.FrameLayout;
import android.widget.GridView;

public class EditGridView {
	protected GridView gridview;
	protected WholeSheet wholesheet;
	
	public EditGridView (GridView gridview, WholeSheet wholesheet) {
		this.gridview = gridview;
		this.wholesheet = wholesheet;
	}
	
	public void createDataGridView() {
		gridview.setNumColumns(wholesheet.getColumns());
		gridview.setHorizontalSpacing(1);
		gridview.setVerticalSpacing(1);
		int width = wholesheet.calcWholeWidth();
		//int height = wholesheet.calcWholeHeight();
		gridview.setLayoutParams(new FrameLayout.LayoutParams(width, -1));
		
	}
}
