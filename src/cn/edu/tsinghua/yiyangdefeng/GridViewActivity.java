package cn.edu.tsinghua.yiyangdefeng;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.tsinghua.graphdealer.GraphDealActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class GridViewActivity extends Activity {
	protected WholeSheet wholesheet;
	protected GridView mygridview;
	protected int titlewidth;
	protected Handler handler;
	protected DataManager dm;
	public static final int SAVE = Menu.FIRST;
	public static final int CHANGE_VARCHOICE = Menu.FIRST + 1;
	public static final int CREATENEWCOLUMN = Menu.FIRST + 2;
	public static final int SET_DIGIT = Menu.FIRST + 3;
	public static final int RETURN_EDITMODE = Menu.FIRST + 4;
	public static final int DRAW = Menu.FIRST + 5;
	protected TextView titletv;
	protected int digit = 3;
	protected GridCellAdapter gca;
	protected int tempcolumn;
	protected int tempvartype = 0;
	protected View varchoice;
	protected Spinner columnchoicespinner;
	protected Spinner vartypechoicespinner;
	public static final String[] operators = { "+", "-", "*", "/", "sin",
			"cos", "tan", "1/x", "x^y", "ln", "exp", "arcsin", "arccos",
			"arctan", "abs" };
	protected String first;
	protected String second;
	protected boolean firstiscolumn;
	protected int drawX;
	protected int drawY;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		dm = new DataManager();
		setContentView(R.layout.data_gridview);
		mygridview = new MyGridView(this);
		mygridview.setHorizontalScrollBarEnabled(true);
		mygridview.setSmoothScrollbarEnabled(true);
		mygridview.setVerticalScrollBarEnabled(true);
		mygridview.setBackgroundColor(Color.BLACK);
		mygridview.setGravity(Gravity.CENTER);
		mygridview.setHorizontalSpacing(1);
		mygridview.setVerticalSpacing(1);
		titletv = (TextView) findViewById(R.id.gridtextview);
		varchoice = this.findViewById(R.layout.vartypechoice);
		columnchoicespinner = (Spinner) this
				.findViewById(R.id.columnchoicespinner);
		vartypechoicespinner = (Spinner) this
				.findViewById(R.id.vartypechoicespinner);
		FrameLayout fm = (FrameLayout) this
				.findViewById(R.id.grid_framelayout);
		
		if (Session.getSession() == null) {
			wholesheet = new WholeSheet();
			mygridview.setNumColumns(wholesheet.getColumns()
					+ EditCellAdapter.EXTRACOLUMNS);

			setGridWidth();
			gca = new GridCellAdapter(getApplicationContext(), wholesheet,
					digit);
			mygridview.setAdapter(gca);
			fm.addView(mygridview);
			titletv.setText("数据查看处理界面-" + wholesheet.getName());
		} else if (Session.getSession().get("wholesheet") != null) {
			wholesheet = (WholeSheet) Session.getSession().get("wholesheet");
			mygridview.setNumColumns(wholesheet.getColumns()
					+ EditCellAdapter.EXTRACOLUMNS);
			setGridWidth();
			gca = new GridCellAdapter(getApplicationContext(), wholesheet,
					digit);
			mygridview.setAdapter(gca);
			
			fm.addView(mygridview);
			titletv.setText("数据查看处理界面-" + wholesheet.getName());
		} else {
			Log.e("test","wrong branch");
			wholesheet = new WholeSheet();
			mygridview.setNumColumns(wholesheet.getColumns()
					+ EditCellAdapter.EXTRACOLUMNS);

			setGridWidth();
			gca = new GridCellAdapter(getApplicationContext(), wholesheet,
					digit);
			mygridview.setAdapter(gca);
			fm.addView(mygridview);
			titletv.setText("数据查看处理界面-" + wholesheet.getName());
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					GridViewActivity.this);
			builder.setTitle("请注意");
			builder.setMessage("如果退回主菜单，现有的数据会丢失，是否确认退出？");
			builder.setCancelable(true);
			builder.setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {

				}

			});
			builder.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					Intent intent = new Intent();
					intent.setClass(GridViewActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}
			});
			builder.setNeutralButton("保存数据", new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					File file = new File(Environment
							.getExternalStorageDirectory().getName()
							+ "/DataHU/");
					if (!file.exists()) {
						file.mkdir();
					}
					String filepath = file.getName() + wholesheet.getName()
							+ ".csv";
					try {
						dm.saveFile(wholesheet, filepath);
						Toast toast = Toast.makeText(GridViewActivity.this,
								"保存文件" + filepath + "成功", Toast.LENGTH_LONG);
						toast.show();
					} catch (IOException e) {
						Toast toast = Toast.makeText(GridViewActivity.this,
								"很抱歉，存储文件出错！", Toast.LENGTH_LONG);
						toast.show();
					}
				}
			});
			builder.show();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, SAVE, Menu.NONE, "保存数据");
		menu.add(Menu.NONE, CHANGE_VARCHOICE, Menu.NONE, "设置数据类型");
		menu.add(Menu.NONE, CREATENEWCOLUMN, Menu.NONE, "通过计算生成列");
		menu.add(Menu.NONE, SET_DIGIT, Menu.NONE, "设置小数位数");
		menu.add(Menu.NONE, RETURN_EDITMODE, Menu.NONE, "回到编辑模式");
		menu.add(Menu.NONE, DRAW, Menu.NONE, "画图");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		AlertDialog.Builder builder;
		switch (item.getItemId()) {
		case SAVE:
			File file = new File(Environment.getExternalStorageDirectory()
					.getName() + "/DataHU");
			file.mkdir();
			String filepath = file.getName() + wholesheet.getName() + ".csv";
			try {
				dm.saveFile(wholesheet, filepath);
				Toast toast = Toast.makeText(GridViewActivity.this, "保存文件"
						+ filepath + "成功", Toast.LENGTH_LONG);
				toast.show();
			} catch (IOException e) {
				Toast toast = Toast.makeText(GridViewActivity.this,
						"很抱歉，存储文件出错！", Toast.LENGTH_LONG);
				toast.show();
			}
			return true;
		case RETURN_EDITMODE:
			gotoEditMode();
			return true;
		case SET_DIGIT:
			builder = new AlertDialog.Builder(GridViewActivity.this);
			builder.setTitle("请输入小数位数，范围0~6");
			final EditText et = new EditText(GridViewActivity.this);
			et.setText(String.valueOf(digit));
			et.setFocusable(true);
			et.setClickable(true);
			builder.setView(et);
			builder.setCancelable(true);
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								int newdigit = Integer.parseInt(et.getText()
										.toString());
								if (newdigit < 0 || newdigit > 6) {
									Toast toast = Toast.makeText(
											GridViewActivity.this, "位数不符合要求",
											Toast.LENGTH_SHORT);
									toast.show();
								} else {
									gca.setDigit(newdigit);
									gca.notifyDataSetChanged();
								}
							} catch (NumberFormatException nfe) {
								Toast toast = Toast.makeText(
										GridViewActivity.this, "您的输入有误",
										Toast.LENGTH_SHORT);
								toast.show();
							}
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.show();
			return true;
		case CHANGE_VARCHOICE:
			tempcolumn = 0;
			tempvartype = 0;
			LayoutInflater li = this.getLayoutInflater();
			final View vartypechoiceview = li.inflate(R.layout.vartypechoice,
					null);
			builder = new AlertDialog.Builder(GridViewActivity.this);
			builder.setTitle("改变变量类型");
			builder.setView(vartypechoiceview);
			Spinner columnchoicespinner = (Spinner) vartypechoiceview
					.findViewById(R.id.columnchoicespinner);
			Spinner vartypechoicespinner = (Spinner) vartypechoiceview
					.findViewById(R.id.vartypechoicespinner);
			columnchoicespinner.setAdapter(new BaseAdapter() {
				@Override
				public int getCount() {
					return wholesheet.getColumns();
				}

				@Override
				public Object getItem(int arg0) {
					return null;
				}

				@Override
				public long getItemId(int position) {
					return position;
				}

				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					TextView tv = new TextView(GridViewActivity.this);
					tv.setTextSize(20);
					tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
					tv.setMinimumWidth(60);
					tv.setText(String.valueOf(CommonTools
							.ChangeNumberintoLetter(position + 1)));
					convertView = tv;
					return convertView;
				}
			});
			columnchoicespinner
					.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int position, long arg3) {
							tempcolumn = position;
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});

			vartypechoicespinner.setAdapter(new BaseAdapter() {
				@Override
				public int getCount() {
					return 2;
				}

				@Override
				public Object getItem(int position) {
					return null;
				}

				@Override
				public long getItemId(int position) {
					return 0;
				}

				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					TextView tv = new TextView(GridViewActivity.this);
					tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
					tv.setTextSize(20);
					tv.setMinimumWidth(60);
					if (position == 0) {
						tv.setText("X");
					} else if (position == 1) {
						tv.setText("Y");
					}
					convertView = tv;
					return convertView;
				}
			});

			vartypechoicespinner
					.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int position, long arg3) {
							if (position == 0) {
								tempvartype = 0;
							} else if (position == 1) {
								tempvartype = 1;
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
						}
					});
			builder.setCancelable(true);
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (tempvartype == 0) {
								wholesheet.getColumn(tempcolumn).setType(
										VarType.X);
							} else if (tempvartype == 1) {
								wholesheet.getColumn(tempcolumn).setType(
										VarType.Y);
							}
							gca.notifyDataSetChanged();
						}
					});
			builder.show();
			return true;
		case CREATENEWCOLUMN:
			first = "0";
			second = "0";
			firstiscolumn = false;

			builder = new AlertDialog.Builder(GridViewActivity.this);
			builder.setTitle("请输入进行运算的列，或者输入一个数：");
			String[] currentcolumns = new String[wholesheet.getColumns()];
			for (int i = 0; i < wholesheet.getColumns(); i++) {
				currentcolumns[i] = CommonTools.ChangeNumberintoLetter(i + 1);
			}
			builder.setItems(currentcolumns,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							firstiscolumn = true;
							first = String.valueOf(which);
							openoperator();
						}

					});
			builder.setCancelable(true);
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.setPositiveButton("输入数字",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							firstiscolumn = false;
							openNumberInput(true, 0);
						}
					});
			builder.show();
			return true;
		case DRAW:
			builder = new AlertDialog.Builder(GridViewActivity.this);
			builder.setTitle("请选择X中的一列作为自变量：");
			List<String> currentxcolumnlist = new ArrayList<String>();
			final List<Integer> currentxcolumnnumber = new ArrayList<Integer>();
			for (int i = 0; i < wholesheet.getColumns(); i++) {
				if (wholesheet.getColumn(i).getType().equals("X")) {
					currentxcolumnlist.add(CommonTools
							.ChangeNumberintoLetter(i + 1));
					currentxcolumnnumber.add(i);
				}
			}
			String[] currentxcolumnname = new String [currentxcolumnlist.size()];
			for(int i = 0;i < currentxcolumnlist.size(); i++) {
				currentxcolumnname[i] = currentxcolumnlist.get(i);
			}
			builder.setItems(currentxcolumnname,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							drawX = currentxcolumnnumber.get(which);
							openychoice();
						}

					});
			builder.setCancelable(true);
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void setGridWidth() {
		int width = wholesheet.calcWholeWidth() + (int) wholesheet.getWidth()
				* EditCellAdapter.EXTRACOLUMNS;
		mygridview.setLayoutParams(new FrameLayout.LayoutParams(width, -1));
	}

	public void gotoEditMode() {
		Session.getSession().cleanUpSession();
		Session session = Session.getSession();
		session.put("wholesheet", wholesheet);
		Intent intent = new Intent();
		intent.setClass(GridViewActivity.this, EditViewActivity.class);
		startActivity(intent);
		finish();
	}

	public void gotoDrawMode() {
		Intent intent = new Intent();
		intent.setClass(GridViewActivity.this, GraphDealActivity.class);
		List<Double> xvalues = wholesheet.getColumn(drawX).getAllData();
		List<Double> yvalues = wholesheet.getColumn(drawY).getAllData();
		String xunit = wholesheet.getColumn(drawX).getUnit();
		String xname = wholesheet.getColumn(drawX).getNotes();
		String yunit = wholesheet.getColumn(drawY).getUnit();
		String yname = wholesheet.getColumn(drawY).getNotes();
		Session.getSession().cleanUpSession();
		Session session = Session.getSession();
		session.put("xvalues", xvalues);
		session.put("yvalues", yvalues);
		session.put("xunit", xunit);
		session.put("xname", xname);
		session.put("yunit", yunit);
		session.put("yname", yname);
		session.put("graphtitle",wholesheet.getGraphTitle());
		session.put("wholesheet", wholesheet);
		startActivity(intent);
		finish();
	}

	public void openychoice() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				GridViewActivity.this);
		builder.setTitle("请选择Y中的一列作为因变量：");
		List<String> currentycolumnlist = new ArrayList<String>();
		final List<Integer> currentycolumnnumber = new ArrayList<Integer>();
		for (int i = 0; i < wholesheet.getColumns(); i++) {
			if (wholesheet.getColumn(i).getType().equals("Y")) {
				currentycolumnlist.add(CommonTools
						.ChangeNumberintoLetter(i + 1));
				currentycolumnnumber.add(i);
			}
		}
		String[] currentycolumnname = new String [currentycolumnlist.size()];
		for(int i = 0;i < currentycolumnlist.size(); i++) {
			currentycolumnname[i] = currentycolumnlist.get(i);
		}
		builder.setItems(currentycolumnname,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						drawY = currentycolumnnumber.get(which);
						gotoDrawMode();
					}

				});
		builder.setCancelable(true);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.show();
	}

	// operators contains "+" "-" "*" "/" "sin" single,"cos" single,"tan"
	// single,"1/x" single,"x^y","ln" single,"exp" single,"arcsin"
	// single,"arccos" single,"arctan" single,"abs" single
	public void openoperator() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				GridViewActivity.this);
		builder.setTitle("请选择运算符");
		builder.setItems(operators, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (!firstiscolumn) {
					double firstnumber = Double.parseDouble(first);
					if (which == 4) {
						wholesheet.insertColumn();
						setGridWidth();
						for (int i = 0; i < wholesheet.getRows(); i++) {
							wholesheet.getColumn(wholesheet.getColumns() - 1)
									.setData(Math.sin(firstnumber), i);
						}
					} else if (which == 5) {
						wholesheet.insertColumn();
						setGridWidth();
						for (int i = 0; i < wholesheet.getRows(); i++) {
							wholesheet.getColumn(wholesheet.getColumns() - 1)
									.setData(Math.cos(firstnumber), i);
						}
					} else if (which == 6) {
						wholesheet.insertColumn();
						setGridWidth();
						for (int i = 0; i < wholesheet.getRows(); i++) {
							wholesheet.getColumn(wholesheet.getColumns() - 1)
									.setData(Math.tan(firstnumber), i);
						}
					} else if (which == 7) {
						wholesheet.insertColumn();
						setGridWidth();
						for (int i = 0; i < wholesheet.getRows(); i++) {
							wholesheet.getColumn(wholesheet.getColumns() - 1)
									.setData((1 / firstnumber), i);
						}
					} else if (which == 9) {
						wholesheet.insertColumn();
						setGridWidth();
						for (int i = 0; i < wholesheet.getRows(); i++) {
							wholesheet.getColumn(wholesheet.getColumns() - 1)
									.setData(Math.log(firstnumber), i);
						}
					} else if (which == 10) {
						wholesheet.insertColumn();
						setGridWidth();
						for (int i = 0; i < wholesheet.getRows(); i++) {
							wholesheet.getColumn(wholesheet.getColumns() - 1)
									.setData(Math.exp(firstnumber), i);
						}
					} else if (which == 11) {
						wholesheet.insertColumn();
						setGridWidth();
						for (int i = 0; i < wholesheet.getRows(); i++) {
							wholesheet.getColumn(wholesheet.getColumns() - 1)
									.setData(Math.asin(firstnumber), i);
						}
					} else if (which == 12) {
						wholesheet.insertColumn();
						setGridWidth();
						for (int i = 0; i < wholesheet.getRows(); i++) {
							wholesheet.getColumn(wholesheet.getColumns() - 1)
									.setData(Math.acos(firstnumber), i);
						}
					} else if (which == 13) {
						wholesheet.insertColumn();
						setGridWidth();
						for (int i = 0; i < wholesheet.getRows(); i++) {
							wholesheet.getColumn(wholesheet.getColumns() - 1)
									.setData(Math.atan(firstnumber), i);
						}
					} else if (which == 14) {
						wholesheet.insertColumn();
						setGridWidth();
						for (int i = 0; i < wholesheet.getRows(); i++) {
							wholesheet.getColumn(wholesheet.getColumns() - 1)
									.setData(Math.abs(firstnumber), i);
						}
					} else {
						openSecondInput(which);
					}
				} else {
					int selectedcolumn = Integer.parseInt(first);
					if (which == 4) {
						wholesheet.insertColumn();
						setGridWidth();
						for (int i = 0; i < wholesheet.getRows(); i++) {
							if (wholesheet.getColumn(selectedcolumn).getData(i) != null) {
								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										Math.sin(wholesheet.getColumn(
												selectedcolumn).getData(i)), i);
							}
						}
					} else if (which == 5) {
						wholesheet.insertColumn();
						setGridWidth();
						for (int i = 0; i < wholesheet.getRows(); i++) {
							if (wholesheet.getColumn(selectedcolumn).getData(i) != null) {
								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										Math.cos(wholesheet.getColumn(
												selectedcolumn).getData(i)), i);
							}
						}
					} else if (which == 6) {
						wholesheet.insertColumn();
						setGridWidth();
						for (int i = 0; i < wholesheet.getRows(); i++) {
							if (wholesheet.getColumn(selectedcolumn).getData(i) != null) {
								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										Math.tan(wholesheet.getColumn(
												selectedcolumn).getData(i)), i);
							}
						}
					} else if (which == 7) {
						wholesheet.insertColumn();
						setGridWidth();
						for (int i = 0; i < wholesheet.getRows(); i++) {
							if (wholesheet.getColumn(selectedcolumn).getData(i) != null) {
								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										(1 / wholesheet.getColumn(
												selectedcolumn).getData(i)), i);
							}
						}
					} else if (which == 9) {
						wholesheet.insertColumn();
						setGridWidth();
						for (int i = 0; i < wholesheet.getRows(); i++) {
							if (wholesheet.getColumn(selectedcolumn).getData(i) != null) {
								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										Math.log(wholesheet.getColumn(
												selectedcolumn).getData(i)), i);
							}
						}
					} else if (which == 10) {
						wholesheet.insertColumn();
						setGridWidth();
						for (int i = 0; i < wholesheet.getRows(); i++) {
							if (wholesheet.getColumn(selectedcolumn).getData(i) != null) {
								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										Math.exp(wholesheet.getColumn(
												selectedcolumn).getData(i)), i);
							}
						}
					} else if (which == 11) {
						wholesheet.insertColumn();
						setGridWidth();
						for (int i = 0; i < wholesheet.getRows(); i++) {
							if (wholesheet.getColumn(selectedcolumn).getData(i) != null) {
								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										Math.asin(wholesheet.getColumn(
												selectedcolumn).getData(i)), i);
							}
						}
					} else if (which == 12) {
						wholesheet.insertColumn();
						setGridWidth();
						for (int i = 0; i < wholesheet.getRows(); i++) {
							if (wholesheet.getColumn(selectedcolumn).getData(i) != null) {
								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										Math.acos(wholesheet.getColumn(
												selectedcolumn).getData(i)), i);
							}
						}
					} else if (which == 13) {
						wholesheet.insertColumn();
						setGridWidth();
						for (int i = 0; i < wholesheet.getRows(); i++) {
							if (wholesheet.getColumn(selectedcolumn).getData(i) != null) {
								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										Math.atan(wholesheet.getColumn(
												selectedcolumn).getData(i)), i);
							}
						}
					} else if (which == 14) {
						wholesheet.insertColumn();
						setGridWidth();
						for (int i = 0; i < wholesheet.getRows(); i++) {
							if (wholesheet.getColumn(selectedcolumn).getData(i) != null) {
								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										Math.abs(wholesheet.getColumn(
												selectedcolumn).getData(i)), i);
							}
						}
					} else {
						openSecondInput(which);
					}
					gca.notifyDataSetChanged();
				}
			}

		});
		builder.show();
	}

	public void openSecondInput(final int operator) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				GridViewActivity.this);
		builder.setTitle("请输入运算的第二列，或者输入数字");
		String[] currentcolumns = new String[wholesheet.getColumns()];
		for (int i = 0; i < wholesheet.getColumns(); i++) {
			currentcolumns[i] = CommonTools.ChangeNumberintoLetter(i + 1);
		}
		builder.setItems(currentcolumns, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (firstiscolumn) {
					int selectedcolumn = Integer.parseInt(first);
					wholesheet.insertColumn();
					setGridWidth();
					for (int i = 0; i < wholesheet.getRows(); i++) {
						if (wholesheet.getColumn(selectedcolumn).getData(i) != null
								&& wholesheet.getColumn(which).getData(i) != null) {
							switch (operator) {
							case 0:
								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										wholesheet.getColumn(selectedcolumn)
												.getData(i)
												+ wholesheet.getColumn(which)
														.getData(i), i);

								break;
							case 1:

								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										wholesheet.getColumn(selectedcolumn)
												.getData(i)
												- wholesheet.getColumn(which)
														.getData(i), i);

								break;
							case 2:

								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										wholesheet.getColumn(selectedcolumn)
												.getData(i)
												* wholesheet.getColumn(which)
														.getData(i), i);

								break;
							case 3:

								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										wholesheet.getColumn(selectedcolumn)
												.getData(i)
												/ wholesheet.getColumn(which)
														.getData(i), i);

								break;
							case 8:

								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										Math.pow(
												wholesheet.getColumn(
														selectedcolumn)
														.getData(i), wholesheet
														.getColumn(which)
														.getData(i)), i);

								break;
							}

						}
					}
				} else {
					double firstnumber = Double.parseDouble(first);
					wholesheet.insertColumn();
					setGridWidth();
					for (int i = 0; i < wholesheet.getRows(); i++) {
						if (wholesheet.getColumn(which).getData(i) != null) {
							switch (operator) {
							case 0:
								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										firstnumber
												+ wholesheet.getColumn(which)
														.getData(i), i);

								break;
							case 1:

								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										firstnumber
												- wholesheet.getColumn(which)
														.getData(i), i);

								break;
							case 2:

								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										firstnumber
												* wholesheet.getColumn(which)
														.getData(i), i);

								break;
							case 3:

								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										firstnumber
												/ wholesheet.getColumn(which)
														.getData(i), i);

								break;
							case 8:

								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										Math.pow(firstnumber, wholesheet
												.getColumn(which).getData(i)),
										i);

								break;

							}
						}
					}
				}
			}
		});
		builder.setCancelable(true);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.setPositiveButton("输入数字",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						openNumberInput(false, operator);
					}
				});
		builder.show();

	}

	public void openNumberInput(final boolean isfirst, final int operator) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				GridViewActivity.this);
		builder.setTitle("请输入数字");
		final EditText et = new EditText(GridViewActivity.this);
		et.setText("");
		et.setFocusable(true);
		et.setClickable(true);
		builder.setView(et);
		builder.setCancelable(true);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (isfirst) {
					first = et.getText().toString();
					openoperator();
				} else {
					double secondnumber = Double.parseDouble(et.getText()
							.toString());
					wholesheet.insertColumn();
					setGridWidth();
					if (firstiscolumn) {
						int selectedcolumn = Integer.parseInt(first);
						for (int i = 0; i < wholesheet.getRows(); i++) {
							if (wholesheet.getColumn(selectedcolumn).getData(i) != null) {
								switch (operator) {
								case 0:

									wholesheet.getColumn(
											wholesheet.getColumns() - 1)
											.setData(
													wholesheet.getColumn(
															selectedcolumn)
															.getData(i)
															+ secondnumber, i);

									break;
								case 1:

									wholesheet.getColumn(
											wholesheet.getColumns() - 1)
											.setData(
													wholesheet.getColumn(
															selectedcolumn)
															.getData(i)
															- secondnumber, i);

									break;
								case 2:

									wholesheet.getColumn(
											wholesheet.getColumns() - 1)
											.setData(
													wholesheet.getColumn(
															selectedcolumn)
															.getData(i)
															* secondnumber, i);

									break;
								case 3:

									wholesheet.getColumn(
											wholesheet.getColumns() - 1)
											.setData(
													wholesheet.getColumn(
															selectedcolumn)
															.getData(i)
															/ secondnumber, i);

									break;
								case 8:

									wholesheet
											.getColumn(
													wholesheet.getColumns() - 1)
											.setData(
													Math.pow(
															wholesheet
																	.getColumn(
																			selectedcolumn)
																	.getData(i),
															secondnumber), i);

									break;
								}
							}
						}
					} else {
						double firstnumber = Double.parseDouble(first);
						for (int i = 0; i < wholesheet.getRows(); i++) {
							switch (operator) {
							case 0:

								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										firstnumber + secondnumber, i);

								break;
							case 1:

								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										firstnumber - secondnumber, i);

								break;
							case 2:

								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										firstnumber * secondnumber, i);

								break;
							case 3:

								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										firstnumber / secondnumber, i);

								break;
							case 8:
								wholesheet.getColumn(
										wholesheet.getColumns() - 1).setData(
										Math.pow(firstnumber, secondnumber), i);

								break;
							}
						}
					}
					gca.notifyDataSetChanged();
				}

			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.show();
	}
}
