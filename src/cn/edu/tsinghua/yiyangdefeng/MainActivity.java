package cn.edu.tsinghua.yiyangdefeng;

import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.GridView;

public class MainActivity extends Activity {
	private WholeSheet wholesheet;
	protected DataGridView datagridview;
	public static int status;
	private float x;
	private float y;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		status = Constant.STATUS_START;
		wholesheet = new WholeSheet();
		//
		setContentView(R.layout.data_gridview);
		//GridView gridview = (GridView)findViewById(R.id.activity_main);
		//datagridview = new DataGridView(gridview,wholesheet);
		//datagridview.createDataGridView();
		//gridview.setAdapter(new CellAdapter(getApplicationContext(),wholesheet));
		//
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyLongPress(keyCode, event);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	} 
	

	
}
