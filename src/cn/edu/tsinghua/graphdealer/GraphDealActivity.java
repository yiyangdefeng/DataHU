package cn.edu.tsinghua.graphdealer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint({ "SimpleDateFormat", "NewApi" })
public class GraphDealActivity extends Activity implements
		OnCheckedChangeListener {
	/**/

	GraphView figure;
	int inputn;
	double[] x, y, dx, dy;
	boolean[] isdelete;

	EditText numofnumet, xtitle, ytitle;
	RadioButton smoothline, brockenline, noline;
	int lineshow = 1, line = 2;
	RadioGroup linear;
	Spinner showline;
	String graphtitle;
	LinearLayout ll;
	protected static final int FIGURE_VIEW = 0;

	private final static String ALBUM_PATH = Environment
			.getExternalStorageDirectory() + "/DataHU_Figure/";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.graphview);
		ll = (LinearLayout) findViewById(R.id.graphlinearlayout);
		String xlabel;
		String ylabel;
		if (Session.getSession().get("x") != null) {
			Session session = Session.getSession();
			x = (double[]) session.get("xvalues");
			y = (double[]) session.get("yvalues");
			String xunit = (String) session.get("xunit");
			String xname = (String) session.get("xname");
			String yunit = (String) session.get("yunit");
			String yname = (String) session.get("yname");
			String graphtitle = (String) session.get("graphtitle");
			xlabel = xname + "(" + xunit + ")";
			ylabel = yname + "(" + yunit + ")";
			this.graphtitle = graphtitle;
			isdelete = new boolean[x.length];
			for (int i = 0; i < isdelete.length; i++) {
				isdelete[i] = false;
			}
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
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
			saveconfirm.setTitle(R.string.menu_save);
			saveconfirm.setMessage(saveinfo);
			saveconfirm.setPositiveButton(R.string.confirm, null);
			saveconfirm.create();
			saveconfirm.show();

			break;

		case R.id.menusetfigure:

			LayoutInflater inflater = getLayoutInflater();
			final View layout = inflater.inflate(R.layout.graph_setfigure,
					(ViewGroup) findViewById(R.id.graphsetfigure));
			numofnumet = (EditText) layout.findViewById(R.id.numofnum);
			numofnumet.setHint(getResources().getText(R.string.numofnumhint)
					+ " " + figure.getNumofnum());
			numofnumet.setGravity(Gravity.CENTER_HORIZONTAL);
			showline = (Spinner) layout.findViewById(R.id.showlineselect);

			final int[] selecttext = { R.string.noshow, R.string.showmain,
					R.string.showdetail, R.string.showx, R.string.showy };
			BaseAdapter ba = new BaseAdapter() {

				public int getCount() {
					return 5;
				}

				public Object getItem(int position) {
					return null;
				}

				public long getItemId(int position) {
					return 0;
				}

				public View getView(int position, View convertView,
						ViewGroup parent) {
					LinearLayout ll = new LinearLayout(GraphDealActivity.this);
					ll.setOrientation(LinearLayout.HORIZONTAL);
					TextView tv = new TextView(GraphDealActivity.this);
					tv.setGravity(Gravity.CENTER_HORIZONTAL);
					tv.setText(getResources().getText(selecttext[position]));
					tv.setTextColor(Color.BLACK);
					tv.setTextSize(20);
					ll.addView(tv);
					return ll;
				}
			};

			showline.setAdapter(ba);
			showline.setOnItemSelectedListener(new OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					LinearLayout ll = (LinearLayout) view;
					TextView tvn = (TextView) ll.getChildAt(0);
					if (tvn.getText().equals(
							getResources().getText(selecttext[0]))) {
						lineshow = 1;
					} else if (tvn.getText().equals(
							getResources().getText(selecttext[1]))) {
						lineshow = 2;
					} else if (tvn.getText().equals(
							getResources().getText(selecttext[2]))) {
						lineshow = 3;
					} else if (tvn.getText().equals(
							getResources().getText(selecttext[3]))) {
						lineshow = 4;
					} else if (tvn.getText().equals(
							getResources().getText(selecttext[4]))) {
						lineshow = 5;
					} else {
						lineshow = 1;
					}
				}

				public void onNothingSelected(AdapterView<?> parent) {
				}
			});

			linear = (RadioGroup) layout.findViewById(R.id.linearrad);
			smoothline = (RadioButton) layout.findViewById(R.id.smoothline);
			brockenline = (RadioButton) layout.findViewById(R.id.brokenline);
			noline = (RadioButton) layout.findViewById(R.id.noline);
			linear.setOnCheckedChangeListener(this);
			if (figure.getIsshowx()) {
				if (figure.getIsshowy()) {
					if (figure.getIsshowdetail()) {
						showline.setSelection(2);
					} else {
						showline.setSelection(1);
					}
				} else {
					showline.setSelection(3);
				}
			} else if (figure.getIsshowy()) {
				showline.setSelection(4);
			} else {
				showline.setSelection(0);
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
			setdialog.setTitle(R.string.menusetfigure);
			setdialog.setView(layout);
			setdialog.setPositiveButton(R.string.confirm,
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
									}
								} else {
								}
							} catch (Exception e) {

							}
							if (lineshow == 1) {
								figure.setIsshowx(false);
								figure.setIsshowy(false);
								figure.setIsshowdetail(false);
							} else if (lineshow == 3) {
								figure.setIsshowx(true);
								figure.setIsshowy(true);
								figure.setIsshowdetail(true);
							} else if (lineshow == 4) {
								figure.setIsshowx(true);
								figure.setIsshowy(false);
								figure.setIsshowdetail(false);
							} else if (lineshow == 5) {
								figure.setIsshowx(false);
								figure.setIsshowy(true);
								figure.setIsshowdetail(false);
							} else {
								figure.setIsshowx(true);
								figure.setIsshowy(true);
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
							ll.removeViewAt(FIGURE_VIEW);
							ll.addView(figure, FIGURE_VIEW);

						}

					});
			setdialog.create();
			setdialog.show();

			break;

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
			inputxy.setTitle(R.string.menusetxy);
			inputxy.setView(layoutsetxy);
			inputxy.setPositiveButton(R.string.confirm,
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

		case R.id.menu_delete:

			String[] xy = new String[x.length];
			final boolean[] chosed = new boolean[xy.length];
			for (int i = 0; i < x.length; i++) {
				xy[i] = "     (" + x[i] + " , " + y[i] + " )";
			}

			for (int i = 0; i < isdelete.length; i++) {
				if (isdelete[i]) {
					chosed[i] = true;
				} else {
					chosed[i] = false;
				}
			}

			new AlertDialog.Builder(this)
					.setTitle(R.string.deletetext)
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
					.setPositiveButton(R.string.deleteconfirm,
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
									if (numfalse == 0) {
										dx = new double[1];
										dy = new double[1];
										dx[0] = 0;
										dy[0] = 0;
									} else {
										dx = new double[numfalse];
										dy = new double[numfalse];
										for (int i = 0; i < isdelete.length; i++) {
											if (!isdelete[i]) {
												dx[j] = x[i];
												dy[j] = y[i];
												j = j + 1;
											}
										}
									}
									figure.setXkey(dx);
									figure.setYkey(dy);
									setContentView(figure);
								}
							})
					.setNegativeButton(R.string.deletecancel,
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

		case R.id.menunofit:

			figure.setIsfitting(false);
			setContentView(figure);
			break;

		case R.id.menuyx:
			figure.setIsfitting(true);
			figure.setFittingtype(1);
			setContentView(figure);
			break;

		case R.id.menuyfunx:

			final EditText inputServer = new EditText(this);
			inputServer.setGravity(Gravity.CENTER_HORIZONTAL);
			inputServer.setHint(R.string.yfunxhint);
			inputServer.setInputType(InputType.TYPE_CLASS_NUMBER);
			AlertDialog.Builder inputdialog = new AlertDialog.Builder(this);
			inputdialog.setTitle(R.string.yfunxtitle);
			inputdialog.setView(inputServer);
			inputdialog.setPositiveButton(R.string.confirm,
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

		case R.id.menuylnx:
			figure.setIsfitting(true);
			figure.setFittingtype(3);
			setContentView(figure);
			break;
		case R.id.menulnyx:
			figure.setIsfitting(true);
			figure.setFittingtype(4);
			setContentView(figure);
			break;
		case R.id.menulnylnx:
			figure.setIsfitting(true);
			figure.setFittingtype(5);
			setContentView(figure);
			break;

		case R.id.menuyabx:
			figure.setIsfitting(true);
			figure.setFittingtype(6);
			setContentView(figure);
			break;
		case R.id.menuyaxb:
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