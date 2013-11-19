package cn.edu.tsinghua.yiyangdefeng;

import android.os.Bundle;
import android.app.Activity;
import android.widget.GridView;

public class MainActivity extends Activity {
	private WholeSheet wholesheet;
	protected DataGridView datagridview;
	

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
