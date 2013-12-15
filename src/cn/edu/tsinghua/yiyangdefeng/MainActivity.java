package cn.edu.tsinghua.yiyangdefeng;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class MainActivity extends Activity {
	Button createsheet;
	Button loadsheet;
	Button help;
	Button exit;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		createsheet = (Button) this.findViewById(R.id.createsheet);
		loadsheet = (Button) this.findViewById(R.id.loadsheet);
		help = (Button) this.findViewById(R.id.help);
		exit = (Button) this.findViewById(R.id.exit);
		
		createsheet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, EditViewActivity.class);
				startActivity(intent);
				finish();
			}
	    });
		loadsheet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.e("test","loadsheet");
			}
	    });
		help.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.e("test","help");
			}
	    });
		exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
	    });
		//GridView gridview = (GridView)findViewById(R.id.activity_main);
		//datagridview = new DataGridView(gridview,wholesheet);
		//datagridview.createDataGridView();
		//gridview.setAdapter(new CellAdapter(getApplicationContext(),wholesheet));
		//
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}
}
