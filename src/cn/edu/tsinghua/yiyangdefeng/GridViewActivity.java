package cn.edu.tsinghua.yiyangdefeng;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

public class GridViewActivity extends Activity {
	private WholeSheet wholesheet;
	protected DataGridView datagridview;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		wholesheet = new WholeSheet();
		setContentView(R.layout.data_gridview);
		GridView gridview = (GridView)findViewById(R.id.data_gridview);
		datagridview = new DataGridView(gridview,wholesheet);
		datagridview.createDataGridView();
		
		gridview.setAdapter(new CellAdapter(getApplicationContext(),wholesheet));
	} 
}
