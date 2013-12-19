package cn.edu.tsinghua.graphdealer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.edu.tsinghua.graphdealer.GraphView.Mstyle;
import cn.edu.tsinghua.yiyangdefeng.MainActivity;
import cn.edu.tsinghua.yiyangdefeng.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

@SuppressLint({ "SimpleDateFormat", "NewApi" })
public class GraphDealActivity extends Activity implements
		OnCheckedChangeListener {
	protected static final int MENU_SAVE = Menu.FIRST;
	protected static final int MENU_SET_FIGURE = Menu.FIRST + 1;
	protected static final int MENU_SET_XY = Menu.FIRST + 2;
	protected static final int MENU_DELETE = Menu.FIRST + 3;
	protected static final int MENU_NO_FIT = Menu.FIRST + 4;
	protected static final int MENU_YX = Menu.FIRST + 5;
	protected static final int MENU_YFUNX = Menu.FIRST + 6;
	protected static final int MENU_YLNX = Menu.FIRST + 7;
	protected static final int MENU_LNYX = Menu.FIRST + 8;
	protected static final int MENU_LNYLNX = Menu.FIRST + 9;
	protected static final int MENU_YABX = Menu.FIRST + 10;
	protected static final int MENU_YAXB = Menu.FIRST + 11;

	GraphView figure;
	int inputn;
	double[] x, y, dx, dy;
	boolean[] isdelete;

	EditText numofnumet, xtitle, ytitle;
	RadioButton yesshow, noshow, showx, showy, showmain, showdetail,
			smoothline, brockenline, noline;
	int yesornoshow = 0, lineshow = 3, line = 2;
	RadioGroup yornshow, showline, linear;

	private final static String ALBUM_PATH = Environment
			.getExternalStorageDirectory() + "/DataHU_Figure/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.graphview);
		LinearLayout ll = (LinearLayout) findViewById(R.id.graphlinearlayout); 
		x = new double[16];
		y = new double[x.length];
		isdelete = new boolean[x.length];
		for (int i = 0; i < x.length; i++) {
			x[i] = i + 1;
			y[i] = Math.pow(2, x[i]);
			isdelete[i] = false;
		}

		figure = new GraphView(this, x, y, "x", "y");
		figure.setBackgroundColor(Color.WHITE);
		figure.setMstyle(Mstyle.Line);
		ll.addView(figure);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, MENU_SAVE, Menu.NONE, "存储图像");
		menu.add(Menu.NONE, MENU_SET_FIGURE, Menu.NONE, "图像设置");
		menu.add(Menu.NONE, MENU_SET_XY, Menu.NONE, "设置坐标");
		menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "删除图像");
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_SAVE:
			String saveinfo = "";
			if (false == figure.isDrawingCacheEnabled()) {
				figure.setDrawingCacheEnabled(true);
			}
			Bitmap bitmap = figure.getDrawingCache();
			try {
				Date date = new Date();
				SimpleDateFormat time = new SimpleDateFormat(
						"yyyy_MM_dd_HH_mm_ss");
				String filename = time.format(date) + ".jpg";
				Boolean b = saveFile(bitmap, filename);
				if (b) {
					saveinfo = "Success in Saving " + filename + " at "
							+ ALBUM_PATH;
				}
			} catch (IOException e) {
				saveinfo = "The External Storage can not be used now OR other problem.";
				e.printStackTrace();
			}

			AlertDialog.Builder saveconfirm = new AlertDialog.Builder(this);
			saveconfirm.setTitle("鍥惧儚淇濆瓨");
			saveconfirm.setMessage(saveinfo);
			saveconfirm.setPositiveButton("纭畾", null);
			saveconfirm.create();
			saveconfirm.show();

			break;

		case MENU_SET_FIGURE:
			break;

		case MENU_SET_XY:

			LayoutInflater inflatersetxy = getLayoutInflater();
			final View layoutsetxy = inflatersetxy.inflate(
					R.layout.graph_setxy,
					(ViewGroup) findViewById(R.id.graphsetxy));
			xtitle = (EditText) layoutsetxy.findViewById(R.id.xtitle);
			ytitle = (EditText) layoutsetxy.findViewById(R.id.ytitle);
			xtitle.setHint(R.string.xytitle);
			ytitle.setHint(R.string.xytitle);
			xtitle.setText(figure.getXstr());
			ytitle.setText(figure.getYstr());
			AlertDialog.Builder inputxy = new AlertDialog.Builder(this);
			inputxy.setTitle("鍧愭爣杞存爣绛�");
			inputxy.setView(layoutsetxy);
			inputxy.setPositiveButton("纭畾",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							try {
								String xtitlestr = xtitle.getText().toString();
								String ytitlestr = ytitle.getText().toString();
								if (xtitlestr.isEmpty()
										&& !figure.getXstr().isEmpty()) {
								} else {
									figure.setXstr(xtitlestr);
								}
								if (ytitlestr.isEmpty()
										&& !figure.getYstr().isEmpty()) {
								} else {
									figure.setYstr(ytitlestr);
								}
							} catch (Exception e) {
							}
							setContentView(figure);
						}

					});
			inputxy.create();
			inputxy.show();
			break;

		case MENU_DELETE:

			String[] xy = new String[x.length];
			final boolean[] chosed = new boolean[xy.length];
			for (int i = 0; i < x.length; i++) {
				xy[i] = "   (" + x[i] + " , " + y[i] + " )";
			}

			for (int i = 0; i < isdelete.length; i++) {
				if (isdelete[i]) {
					chosed[i] = true;
				} else {
					chosed[i] = false;
				}
			}

			new AlertDialog.Builder(this)
					.setTitle("閫変腑瑕佸墧闄ょ殑鏁版嵁")
					.setMultiChoiceItems(xy, chosed,
							new DialogInterface.OnMultiChoiceClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which, boolean isChecked) {
									if (isChecked) {
										chosed[which] = true;
									} else {
										chosed[which] = false;
									}
								}
							})
					.setPositiveButton("纭畾鍓旈櫎",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									int numfalse = 0;
									for (int i = 0; i < chosed.length; i++) {
										if (chosed[i]) {
											isdelete[i] = true;
										} else {
											isdelete[i] = false;
											numfalse += 1;
										}
									}
									int j = 0;
									dx = new double[numfalse];
									dy = new double[numfalse];
									for (int i = 0; i < isdelete.length; i++) {
										if (!isdelete[i]) {
											dx[j] = x[i];
											dy[j] = y[i];
											j = j + 1;
										}
									}
									figure.setXkey(dx);
									figure.setYkey(dy);
									setContentView(figure);
								}
							})
					.setNegativeButton("娓呯┖閫変腑",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									for (int i = 0; i < isdelete.length; i++) {
										isdelete[i] = false;
									}
									figure.setXkey(x);
									figure.setYkey(y);
									setContentView(figure);
								}
							}).show();

			break;

		case MENU_NO_FIT:

			figure.setIsfitting(false);
			setContentView(figure);
			break;

		case MENU_YX:
			figure.setIsfitting(true);
			figure.setFittingtype(1);
			setContentView(figure);
			break;

		case MENU_YFUNX:

			final EditText inputServer = new EditText(this);
			inputServer.setHint("寤鸿n澶т簬0");
			inputServer.setInputType(InputType.TYPE_CLASS_NUMBER);
			AlertDialog.Builder inputdialog = new AlertDialog.Builder(this);
			inputdialog.setTitle("璇疯緭鍏ユ鏁皀");
			inputdialog.setView(inputServer);
			inputdialog.setPositiveButton("纭畾",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							if (inputServer.getText().toString() != "") {
								try {
									inputn = Integer.valueOf(inputServer
											.getText().toString());
								} catch (Exception e) {
									inputn = 0;
								}
							} else {
								inputn = 0;
							}
							if (inputn > 0) {
								figure.setIsfitting(true);
								figure.setFittingtype(2);
								figure.setNumofpara(inputn + 1);
								setContentView(figure);
							} else {
							}
						}

					});
			inputdialog.create();
			inputdialog.show();
			break;

		case MENU_YLNX:
			figure.setIsfitting(true);
			figure.setFittingtype(3);
			setContentView(figure);
			break;
		case MENU_LNYX:
			figure.setIsfitting(true);
			figure.setFittingtype(4);
			setContentView(figure);
			break;
		case MENU_LNYLNX:
			figure.setIsfitting(true);
			figure.setFittingtype(5);
			setContentView(figure);
			break;

		case MENU_YABX:
			figure.setIsfitting(true);
			figure.setFittingtype(6);
			setContentView(figure);
			break;
		case MENU_YAXB:
			figure.setIsfitting(true);
			figure.setFittingtype(7);
			setContentView(figure);
			break;
		}
		return true;
	}

	public Boolean saveFile(Bitmap bm, String fileName) throws IOException {
		File dirFile = new File(ALBUM_PATH);
		fileName = fileName.replace("/", "_");
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		Matrix matrix = new Matrix();
		matrix.postScale(2.5f, 2.5f);
		Bitmap bigbm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
				bm.getHeight(), matrix, true);
		File myCaptureFile = new File(ALBUM_PATH + fileName);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		Boolean b = bigbm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		bos.flush();
		bos.close();
		return b;
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		if (arg0 == yornshow) {
			if (arg1 == yesshow.getId()) {
				yesornoshow = 1;
				showx.setEnabled(true);
				showy.setEnabled(true);
				showmain.setEnabled(true);
				showdetail.setEnabled(true);
				showmain.setChecked(true);
			} else if (arg1 == noshow.getId()) {
				yesornoshow = 0;
				showx.setChecked(false);
				showy.setChecked(false);
				showmain.setChecked(false);
				showdetail.setChecked(false);
				showx.setEnabled(false);
				showy.setEnabled(false);
				showmain.setEnabled(false);
				showdetail.setEnabled(false);
			}

		}
		if (arg0 == showline) {
			if (arg1 == showx.getId()) {
				lineshow = 1;
				yesshow.setChecked(true);
			} else if (arg1 == showy.getId()) {
				lineshow = 2;
				yesshow.setChecked(true);
			} else if (arg1 == showdetail.getId()) {
				lineshow = 4;
				yesshow.setChecked(true);
			} else if (arg1 == showmain.getId()) {
				lineshow = 3;
				yesshow.setChecked(true);
			}

		}
		if (arg0 == linear) {
			if (arg1 == smoothline.getId()) {
				line = 1;
			} else if (arg1 == noline.getId()) {
				line = 0;
			} else {
				line = 2;
			}

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(GraphDealActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onRestart() {
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE); 
		super.onRestart();
	}

	@Override
	protected void onResume() {
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE); 
		super.onResume();
	}

	@Override
	protected void onStart() {
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE); 
		super.onStart();
	}
}