package cn.edu.tsinghua.graphdealer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.edu.tsinghua.graphdealer.GraphView.Mstyle;
import cn.edu.tsinghua.yiyangdefeng.GridViewActivity;
import cn.edu.tsinghua.yiyangdefeng.R;
import cn.edu.tsinghua.yiyangdefeng.Session;

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
	/*
	 * protected static final int MENU_SAVE = Menu.FIRST; protected static final
	 * int MENU_SET_FIGURE = Menu.FIRST + 1; protected static final int
	 * MENU_SET_XY = Menu.FIRST + 2; protected static final int MENU_DELETE =
	 * Menu.FIRST + 3; protected static final int MENU_NO_FIT = Menu.FIRST + 4;
	 * protected static final int MENU_YX = Menu.FIRST + 5; protected static
	 * final int MENU_YFUNX = Menu.FIRST + 6; protected static final int
	 * MENU_YLNX = Menu.FIRST + 7; protected static final int MENU_LNYX =
	 * Menu.FIRST + 8; protected static final int MENU_LNYLNX = Menu.FIRST + 9;
	 * protected static final int MENU_YABX = Menu.FIRST + 10; protected static
	 * final int MENU_YAXB = Menu.FIRST + 11; protected static final int
	 * MENU_SETTING = Menu.FIRST + 12;
	 */

	GraphView figure;
	int inputn;
	double[] x, y, dx, dy;
	boolean[] isdelete;

	EditText numofnumet, xtitle, ytitle;
	RadioButton yesshow, noshow, showx, showy, showmain, showdetail,
			smoothline, brockenline, noline;
	int yesornoshow = 0, lineshow = 3, line = 2;
	RadioGroup yornshow, showline, linear;
	protected String graphtitle;
	private final static String ALBUM_PATH = Environment
			.getExternalStorageDirectory() + "/DataHU_Figure/";
	protected LinearLayout ll;
	protected static final int FIGURE_VIEW = 100;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.graphview);
		ll = (LinearLayout) findViewById(R.id.graphlinearlayout);
		String xlabel;
		String ylabel;
		if (Session.getSession().get("xvalues") != null) {
			Session session = Session.getSession();
			List<Double> xvalues = (List<Double>) session.get("xvalues");
			List<Double> yvalues = (List<Double>) session.get("yvalues");
			String xunit = (String) session.get("xunit");
			String xname = (String) session.get("xname");
			String yunit = (String) session.get("yunit");
			String yname = (String) session.get("yname");
			String graphtitle = (String) session.get("graphtitle");
			xlabel = xname + "(" + xunit + ")";
			ylabel = yname + "(" + yunit + ")";
			int xcount = 0;
			int ycount = 0;
			for (int i = 0; i < xvalues.size(); i++) {
				if (xvalues.get(i) != null) {
					xcount++;
				} else {
					break;
				}
			}
			for (int i = 0; i < yvalues.size(); i++) {
				if (yvalues.get(i) != null) {
					ycount++;
				} else {
					break;
				}
			}
			int count = Math.min(xcount, ycount);
			x = new double[count];

			for (int i = 0; i < count; i++) {
				x[i] = xvalues.get(i);
			}
			y = new double[count];
			for (int i = 0; i < count; i++) {
				y[i] = yvalues.get(i);
			}
			this.graphtitle = graphtitle;
			isdelete = new boolean[x.length];
		}

		else {
			// just an example
			x = new double[16];
			y = new double[x.length];
			isdelete = new boolean[x.length];
			for (int i = 0; i < x.length; i++) {
				x[i] = i + 1;
				y[i] = Math.pow(2, x[i]);
				isdelete[i] = false;
			}
			xlabel = "x";
			ylabel = "y";
		}
		figure = new GraphView(this, x, y, xlabel, ylabel);
		figure.setBackgroundColor(Color.WHITE);
		figure.setMstyle(Mstyle.Line);
		ll.addView(figure, FIGURE_VIEW);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;

		/*
		 * menu.add(Menu.NONE, MENU_SAVE, Menu.NONE, "存储图像");
		 * menu.add(Menu.NONE, MENU_SETTING, Menu.NONE, "格式设置");
		 * menu.add(Menu.NONE, MENU_SET_FIGURE, Menu.NONE, "图像样式");
		 * menu.add(Menu.NONE, MENU_SET_XY, Menu.NONE, "设置坐标轴标签");
		 * menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "删除异常数据");
		 * menu.add(Menu.NONE, MENU_NO_FIT, Menu.NONE, "清除拟合结果");
		 * menu.add(Menu.NONE, MENU_YX, Menu.NONE, "y=a*x+b");
		 * menu.add(Menu.NONE, MENU_YFUNX, Menu.NONE, "n次多项式");
		 * menu.add(Menu.NONE, MENU_YLNX, Menu.NONE, "y=a*ln(x)+b");
		 * menu.add(Menu.NONE, MENU_LNYX, Menu.NONE, "ln(y)=a*x+b");
		 * menu.add(Menu.NONE, MENU_LNYLNX, Menu.NONE, "ln(y)=a*ln(x)+b");
		 * menu.add(Menu.NONE, MENU_YABX, Menu.NONE, "y=a*b^x+c");
		 * menu.add(Menu.NONE, MENU_YAXB, Menu.NONE, "y=a*x^b+c");
		 */
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_save:
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
			saveconfirm.setTitle("图像保存");
			saveconfirm.setMessage(saveinfo);
			saveconfirm.setPositiveButton("确定", null);
			saveconfirm.create();
			saveconfirm.show();

			break;

		case R.id.menusetfigure:
			/*
			LayoutInflater inflater = getLayoutInflater();
			final View layout = inflater.inflate(R.layout.graph_setfigure,
					(ViewGroup) findViewById(R.id.graphsetfigure));
			numofnumet = (EditText) layout.findViewById(R.id.numofnum);
			numofnumet.setHint("已设置为" + figure.getNumofnum());
			numofnumet.setGravity(Gravity.CENTER_HORIZONTAL);
			yornshow = (RadioGroup) layout.findViewById(R.id.yornshowrad);
			yesshow = (RadioButton) layout.findViewById(R.id.yesshow);
			noshow = (RadioButton) layout.findViewById(R.id.noshow);
			yornshow.setOnCheckedChangeListener(this);
			showline = (RadioGroup) layout.findViewById(R.id.showlinerad);
			showx = (RadioButton) layout.findViewById(R.id.showx);
			showy = (RadioButton) layout.findViewById(R.id.showy);
			showmain = (RadioButton) layout.findViewById(R.id.showmain);
			showdetail = (RadioButton) layout.findViewById(R.id.showdetail);
			showline.setOnCheckedChangeListener(this);
			linear = (RadioGroup) layout.findViewById(R.id.linearrad);
			smoothline = (RadioButton) layout.findViewById(R.id.smoothline);
			brockenline = (RadioButton) layout.findViewById(R.id.brokenline);
			noline = (RadioButton) layout.findViewById(R.id.noline);
			linear.setOnCheckedChangeListener(this);
			if (figure.getIsshowx()) {
				showx.setEnabled(true);
				showy.setEnabled(true);
				showmain.setEnabled(true);
				showdetail.setEnabled(true);
				if (figure.getIsshowy()) {
					if (figure.getIsshowdetail()) {
						yesshow.setChecked(true);
						noshow.setChecked(false);
						showx.setChecked(false);
						showy.setChecked(false);
						showmain.setChecked(false);
						showdetail.setChecked(true);
					} else {
						yesshow.setChecked(true);
						noshow.setChecked(false);
						showx.setChecked(false);
						showy.setChecked(false);
						showmain.setChecked(true);
						showdetail.setChecked(false);
					}
				} else {
					yesshow.setChecked(true);
					noshow.setChecked(false);
					showx.setChecked(true);
					showy.setChecked(false);
					showmain.setChecked(false);
					showdetail.setChecked(false);
				}
			} else {
				if (figure.getIsshowy()) {
					showx.setEnabled(true);
					showy.setEnabled(true);
					showmain.setEnabled(true);
					showdetail.setEnabled(true);
					yesshow.setChecked(true);
					noshow.setChecked(false);
					showx.setChecked(false);
					showy.setChecked(true);
					showmain.setChecked(false);
					showdetail.setChecked(false);
				} else {
					yesshow.setChecked(false);
					noshow.setChecked(true);
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
			if (figure.getIslinear()) {
				if (figure.getMstyle() == Mstyle.Line) {
					brockenline.setChecked(true);
				} else {
					smoothline.setChecked(true);
				}

			} else {
				noline.setChecked(true);
			}
			AlertDialog.Builder setdialog = new AlertDialog.Builder(this);
			setdialog.setTitle("图像样式");
			setdialog.setView(layout);
			setdialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@SuppressLint("NewApi")
						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								String numofnumstr = numofnumet.getText()
										.toString();
								if (numofnumstr != "") {
									try {
										int intnumofnum = Integer
												.valueOf(numofnumstr);
										figure.setNumofnum(intnumofnum);
									} catch (Exception e) {
										figure.setNumofnum(6);
									}
								} else {
									figure.setNumofnum(6);
								}
							} catch (Exception e) {

							}
							if (yesornoshow == 1) {
								if (lineshow == 1) {
									figure.setIsshowx(true);
									figure.setIsshowy(false);
									figure.setIsshowdetail(false);
								} else if (lineshow == 2) {
									figure.setIsshowx(false);
									figure.setIsshowy(true);
									figure.setIsshowdetail(false);
								} else if (lineshow == 4) {
									figure.setIsshowx(true);
									figure.setIsshowy(true);
									figure.setIsshowdetail(true);
								} else {
									figure.setIsshowx(true);
									figure.setIsshowy(true);
									figure.setIsshowdetail(false);
								}
							} else {
								figure.setIsshowx(false);
								figure.setIsshowy(false);
								figure.setIsshowdetail(false);
							}

							if (line == 1) {
								figure.setIslinear(true);
								figure.setMstyle(Mstyle.scroll);
							} else if (line == 0) {
								figure.setIslinear(false);
							} else {
								figure.setIslinear(true);
								figure.setMstyle(Mstyle.Line);
							}
							ll.removeViewAt(FIGURE_VIEW);\ll.addView(figure, FIGURE_VIEW);

						}

					});
			setdialog.create();
			setdialog.show();

			break;*/

		case R.id.menusetxy:

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
			inputxy.setTitle("坐标轴标签");
			inputxy.setView(layoutsetxy);
			inputxy.setPositiveButton("确定",
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
							ll.removeViewAt(FIGURE_VIEW);
							ll.addView(figure, FIGURE_VIEW);
						}

					});
			inputxy.create();
			inputxy.show();
			break;

		case R.id.menu_delete:

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
					.setTitle("选中要剔除的数据")
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
					.setPositiveButton("确定剔除",
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
									ll.removeViewAt(FIGURE_VIEW);
									ll.addView(figure, FIGURE_VIEW);
								}
							})
					.setNegativeButton("清空选中",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									for (int i = 0; i < isdelete.length; i++) {
										isdelete[i] = false;
									}
									figure.setXkey(x);
									figure.setYkey(y);
									ll.removeViewAt(FIGURE_VIEW);
									ll.addView(figure, FIGURE_VIEW);
								}
							}).show();

			break;

		case R.id.menunofit:

			figure.setIsfitting(false);
			;
			break;

		case R.id.menuyx:
			figure.setIsfitting(true);
			figure.setFittingtype(1);
			ll.removeViewAt(FIGURE_VIEW);
			ll.addView(figure, FIGURE_VIEW);
			break;

		case R.id.menuyfunx:

			final EditText inputServer = new EditText(this);
			inputServer.setHint("建议n大于0");
			inputServer.setInputType(InputType.TYPE_CLASS_NUMBER);
			AlertDialog.Builder inputdialog = new AlertDialog.Builder(GraphDealActivity.this);
			inputdialog.setTitle("请输入次数n");
			inputdialog.setView(inputServer);
			inputdialog.setPositiveButton("确定",
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
								ll.removeViewAt(FIGURE_VIEW);
								ll.addView(figure, FIGURE_VIEW);
							} else {
							}
						}

					});
			inputdialog.create();
			inputdialog.show();
			break;

		case R.id.menuylnx:
			figure.setIsfitting(true);
			figure.setFittingtype(3);
			ll.removeViewAt(FIGURE_VIEW);
			ll.addView(figure, FIGURE_VIEW);
			break;
		case R.id.menulnyx:
			figure.setIsfitting(true);
			figure.setFittingtype(4);
			ll.removeViewAt(FIGURE_VIEW);
			ll.addView(figure, FIGURE_VIEW);
			break;
		case R.id.menulnylnx:
			figure.setIsfitting(true);
			figure.setFittingtype(5);
			ll.removeViewAt(FIGURE_VIEW);
			ll.addView(figure, FIGURE_VIEW);
			break;

		case R.id.menuyabx:
			figure.setIsfitting(true);
			figure.setFittingtype(6);
			ll.removeViewAt(FIGURE_VIEW);
			ll.addView(figure, FIGURE_VIEW);
			break;
		case R.id.menuyaxb:
			figure.setIsfitting(true);
			figure.setFittingtype(7);
			ll.removeViewAt(FIGURE_VIEW);
			ll.addView(figure, FIGURE_VIEW);
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
			intent.setClass(GraphDealActivity.this, GridViewActivity.class);
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