package cn.edu.tsinghua.yiyangdefeng;

import java.io.File;
import java.io.IOException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EditViewActivity extends Activity {
	protected WholeSheet wholesheet;
	protected MyGridView mygridview;
	protected int titlewidth;
	EditCellAdapter eca;
	protected final int SAVE = Menu.FIRST;
	protected final int OBSERVATIONMODE = Menu.FIRST + 1;
	protected final int ERASEROW = Menu.FIRST + 2;
	protected final int ERASECOLUMN = Menu.FIRST + 3;
	protected final int CREATEROW = Menu.FIRST + 4;
	protected final int CREATECOLUMN = Menu.FIRST + 5;
	protected final int CHANGEWIDTH = Menu.FIRST + 6;
	protected final int CHANGEHEIGHT = Menu.FIRST + 7;
	protected final int DRAW = Menu.FIRST + 8;
	protected final int CHANGENAME = Menu.FIRST + 9;
	protected TextView titletv;

	protected DataManager dm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		dm = new DataManager();
		setContentView(R.layout.data_editview);
		mygridview = new MyGridView(this);
		mygridview.setHorizontalScrollBarEnabled(true);
		mygridview.setSmoothScrollbarEnabled(true);
		mygridview.setVerticalScrollBarEnabled(true);
		mygridview.setBackgroundColor(Color.BLACK);
		mygridview.setGravity(Gravity.CENTER);
		mygridview.setHorizontalSpacing(1);
		mygridview.setVerticalSpacing(1);
		titletv = (TextView) findViewById(R.id.edittextview);
		FrameLayout fm = (FrameLayout) this.findViewById(R.id.edit_framelayout);
		if (Session.getSession() == null) {
			wholesheet = new WholeSheet();
			mygridview.setNumColumns(wholesheet.getColumns()
					+ EditCellAdapter.EXTRACOLUMNS);

			setGridWidth();
			eca = new EditCellAdapter(getApplicationContext(), wholesheet);
			mygridview.setAdapter(eca);
			fm.addView(mygridview);
			titletv.setText("�༭����-" + wholesheet.getName());
		} else if (Session.getSession().get("wholesheet") != null) {
			wholesheet = (WholeSheet) Session.getSession().get("wholesheet");
			mygridview.setNumColumns(wholesheet.getColumns()
					+ EditCellAdapter.EXTRACOLUMNS);

			setGridWidth();
			mygridview.setAdapter(new EditCellAdapter(getApplicationContext(),
					wholesheet));
			fm.addView(mygridview);
			titletv.setText("�༭����-" + wholesheet.getName());
		} else {
			wholesheet = new WholeSheet();
			mygridview.setNumColumns(wholesheet.getColumns()
					+ EditCellAdapter.EXTRACOLUMNS);

			setGridWidth();
			eca = new EditCellAdapter(getApplicationContext(), wholesheet);
			mygridview.setAdapter(eca);
			fm.addView(mygridview);
			titletv.setText("�༭����-" + wholesheet.getName());
		}
	}

	public void setGridWidth() {
		int width = wholesheet.calcWholeWidth() + (int) wholesheet.getWidth()
				* EditCellAdapter.EXTRACOLUMNS;
		mygridview.setLayoutParams(new FrameLayout.LayoutParams(width, -1));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, SAVE, Menu.NONE, "��������");
		menu.add(Menu.NONE, OBSERVATIONMODE, Menu.NONE, "ת��鿴ģʽ");
		menu.add(Menu.NONE, ERASEROW, Menu.NONE, "ɾ����");
		menu.add(Menu.NONE, ERASECOLUMN, Menu.NONE, "ɾ����");
		menu.add(Menu.NONE, CREATEROW, Menu.NONE, "������");
		menu.add(Menu.NONE, CREATECOLUMN, Menu.NONE, "������");
		menu.add(Menu.NONE, CHANGEWIDTH, Menu.NONE, "�����п�");
		menu.add(Menu.NONE, CHANGEHEIGHT, Menu.NONE, "�����и�");
		menu.add(Menu.NONE, DRAW, Menu.NONE, "��ͼ");
		menu.add(Menu.NONE, CHANGENAME, Menu.NONE, "���ı������");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		AlertDialog.Builder builder;
		switch (item.getItemId()) {
		case SAVE:
			File file = new File(Environment.getExternalStorageDirectory()
					.getName() + "/DataHU/");
			if (!file.exists()) {
				file.mkdir();
			}
			String filepath = file.getName() + wholesheet.getName() + ".csv";
			try {
				dm.saveFile(wholesheet, filepath);
				Toast toast = Toast.makeText(EditViewActivity.this, "�����ļ�"
						+ filepath + "�ɹ�", Toast.LENGTH_LONG);
				toast.show();
			} catch (IOException e) {
				Toast toast = Toast.makeText(EditViewActivity.this,
						"�ܱ�Ǹ���洢�ļ�����", Toast.LENGTH_LONG);
				toast.show();
			}
			return true;
		case OBSERVATIONMODE:
			gotoObserveMode();
			return true;
		case ERASEROW:
			builder = new AlertDialog.Builder(EditViewActivity.this);
			builder.setTitle("��ѡ����Ҫɾ������");
			String[] currentrows = new String[wholesheet.getRows()];
			for (int i = 0; i < wholesheet.getRows(); i++) {
				currentrows[i] = String.valueOf(i + 1);
			}

			builder.setItems(currentrows,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							wholesheet.eraseRow(which);
							eca.notifyDataSetChanged();
						}

					});
			builder.setCancelable(true);
			builder.setNegativeButton("ȡ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.show();
			return true;
		case ERASECOLUMN:
			builder = new AlertDialog.Builder(EditViewActivity.this);
			builder.setTitle("��ѡ����Ҫɾ������");
			String[] currentcolumns = new String[wholesheet.getColumns()];
			for (int i = 0; i < wholesheet.getColumns(); i++) {
				currentcolumns[i] = String.valueOf(CommonTools
						.ChangeNumberintoLetter(i + 1));
			}
			builder.setItems(currentcolumns,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							wholesheet.eraseColumn(which);
							mygridview.setNumColumns(wholesheet.getColumns()
									+ EditCellAdapter.EXTRACOLUMNS);
							setGridWidth();
						}

					});
			builder.setCancelable(true);
			builder.setNegativeButton("ȡ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.show();
			return true;
		case CREATEROW:
			builder = new AlertDialog.Builder(EditViewActivity.this);
			builder.setTitle("��ѡ������λ�õ�ǰһ�У�����ѡ�����ĩβ");
			currentrows = new String[wholesheet.getRows()];
			for (int i = 0; i < wholesheet.getRows(); i++) {
				currentrows[i] = String.valueOf(i + 1);
			}
			builder.setItems(currentrows,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							rowNumberInput(which);
						}
					});
			builder.setCancelable(true);
			builder.setNegativeButton("ȡ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.setPositiveButton("����ĩβ",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							rowNumberInput(-1);
						}
					});
			builder.show();
			return true;
		case CREATECOLUMN:
			builder = new AlertDialog.Builder(EditViewActivity.this);
			builder.setTitle("��ѡ������λ�õ�ǰһ�У�����ѡ�����ĩβ");
			currentcolumns = new String[wholesheet.getColumns()];
			for (int i = 0; i < wholesheet.getColumns(); i++) {
				currentcolumns[i] = String.valueOf(CommonTools
						.ChangeNumberintoLetter(i + 1));
			}
			builder.setItems(currentcolumns,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							wholesheet.insertColumn(which);
							mygridview.setNumColumns(wholesheet.getColumns()
									+ EditCellAdapter.EXTRACOLUMNS);
							setGridWidth();
						}

					});
			builder.setCancelable(true);
			builder.setNegativeButton("ȡ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.setPositiveButton("����ĩβ",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							wholesheet.insertColumn();
							mygridview.setNumColumns(wholesheet.getColumns()
									+ EditCellAdapter.EXTRACOLUMNS);
							setGridWidth();
						}
					});
			builder.show();
			return true;
		case DRAW:
			builder = new AlertDialog.Builder(EditViewActivity.this);
			builder.setTitle("��ʾ");
			builder.setMessage("Ҫ��ʹ�û�ͼ���ܣ���Ҫ�����˳����ݱ༭ģʽ�������ݲ鿴ģʽ���Ƿ�ִ�У�");
			builder.setCancelable(true);
			builder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							gotoObserveMode();
						}
					});
			builder.setNegativeButton("ȡ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.show();
			return true;
		case CHANGEWIDTH:
			builder = new AlertDialog.Builder(EditViewActivity.this);
			builder.setTitle("�������п�������Ϊ��λ");
			final EditText et = new EditText(EditViewActivity.this);
			et.setText(String.valueOf(wholesheet.getWidth()));
			et.setFocusable(true);
			et.setClickable(true);
			builder.setView(et);
			builder.setCancelable(true);
			builder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								float newwidth = Float.parseFloat(et.getText()
										.toString());
								if (newwidth > 0) {
									wholesheet.setWidth(newwidth);
									setGridWidth();
									eca.notifyDataSetChanged();
								}
							} catch (NumberFormatException nfe) {
								Toast toast = Toast.makeText(
										EditViewActivity.this, "������������",
										Toast.LENGTH_SHORT);
								toast.show();
							}

						}
					});
			builder.setNegativeButton("ȡ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.show();
			return true;
		case CHANGEHEIGHT:
			builder = new AlertDialog.Builder(EditViewActivity.this);
			builder.setTitle("�����иߣ�������Ϊ��λ");
			final EditText et2 = new EditText(EditViewActivity.this);
			et2.setText(String.valueOf(wholesheet.getHeight()));
			et2.setFocusable(true);
			et2.setClickable(true);
			builder.setView(et2);
			builder.setCancelable(true);
			builder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								float newheight = Float.parseFloat(et2
										.getText().toString());
								if (newheight > 0) {
									wholesheet.setHeight(newheight);
									eca.notifyDataSetChanged();
								}
							} catch (NumberFormatException nfe) {
								Toast toast = Toast.makeText(
										EditViewActivity.this, "������������",
										Toast.LENGTH_SHORT);
								toast.show();
							}

						}
					});
			builder.setNegativeButton("ȡ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.show();
			return true;
		case CHANGENAME:
			builder = new AlertDialog.Builder(EditViewActivity.this);
			builder.setTitle("���������ݱ�����");
			final EditText et3 = new EditText(EditViewActivity.this);
			et3.setText(wholesheet.getName());
			et3.setFocusable(true);
			et3.setClickable(true);
			builder.setView(et3);
			builder.setCancelable(true);
			builder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String newname = et3.getText().toString();
							if (!newname.equals("")) {
								wholesheet.setName(newname);
								eca.notifyDataSetChanged();
								titletv.setText("�༭����-" + newname);
							} else {

								Toast toast = Toast.makeText(
										EditViewActivity.this, "���Ʋ���Ϊ��",
										Toast.LENGTH_SHORT);
								toast.show();
							}
						}
					});
			builder.setNegativeButton("ȡ��",
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

	public void gotoObserveMode() {
		Session.getSession().cleanUpSession();
		Session session = Session.getSession();
		session.put("wholesheet", wholesheet);
		Intent intent = new Intent();
		intent.setClass(EditViewActivity.this, GridViewActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					EditViewActivity.this);
			builder.setTitle("��ע��");
			builder.setMessage("����˻����˵������е����ݻᶪʧ���Ƿ�ȷ���˳���");
			builder.setCancelable(true);
			builder.setNegativeButton("ȡ��", new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {

				}

			});
			builder.setPositiveButton("ȷ��", new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					Intent intent = new Intent();
					intent.setClass(EditViewActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}
			});
			builder.setNeutralButton("��������", new OnClickListener() {
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
						Toast toast = Toast.makeText(EditViewActivity.this,
								"�����ļ�" + filepath + "�ɹ�", Toast.LENGTH_LONG);
						toast.show();
					} catch (IOException e) {
						Toast toast = Toast.makeText(EditViewActivity.this,
								"�ܱ�Ǹ���洢�ļ�����", Toast.LENGTH_LONG);
						toast.show();
					}
				}
			});
			builder.show();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	protected void rowNumberInput(final int therowbefore) {
		AlertDialog.Builder builder = new AlertDialog.Builder(EditViewActivity.this);
		builder.setTitle("������Ҫ���������");
		final EditText et = new EditText(EditViewActivity.this);
		et.setText(String.valueOf(1));
		et.setFocusable(true);
		et.setClickable(true);
		builder.setView(et);
		builder.setCancelable(true);
		builder.setPositiveButton("ȷ��",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							int newrows = Math.abs(Integer.parseInt(et.getText()
									.toString()));
							if (therowbefore != -1) {
								for(int i = 0; i < newrows;i++) {
									wholesheet.insertRow(therowbefore);
									eca.notifyDataSetChanged();
								}
							}
							else {
								for (int i = 0;i < newrows;i++) {
									wholesheet.insertRow();
									eca.notifyDataSetChanged();
							}
							}
						} catch (NumberFormatException nfe) {
							Toast toast = Toast.makeText(
									EditViewActivity.this, "������������",
									Toast.LENGTH_SHORT);
							toast.show();
						}
					}
				});
		builder.setNegativeButton("ȡ��",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		builder.show();
	}
}
