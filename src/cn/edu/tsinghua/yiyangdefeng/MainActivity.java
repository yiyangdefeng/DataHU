package cn.edu.tsinghua.yiyangdefeng;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity {

	private GridView gridView;
	private List<Item> itemList;
	private MyListAdapter adapter;
	private ItemClickEvent listener;
	private MyHandler handler;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_main);
		gridView = (GridView) findViewById(R.id.gridView);
		handler = new MyHandler();
		new Thread() {
			public void run() {
				itemList = new ArrayList<Item>();
				for (int i = 0; i < 10; i++) {
					Item item = new Item("北极熊生存如履薄冰" + i, "如果我们从现在开始就采取措施降低气温"
							+ i);
					itemList.add(item);
				}
				Message msg = Message.obtain();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		}.start();
	}

	private class MyHandler extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if ((itemList == null) || (itemList.size() == 0)) {
					return;
				} else {
					LayoutParams params = new LayoutParams(itemList.size()
							* (420 + 6), LayoutParams.WRAP_CONTENT);

					gridView.setLayoutParams(params);
					gridView.setColumnWidth(420);
					gridView.setHorizontalSpacing(6);
					gridView.setStretchMode(GridView.NO_STRETCH);
					gridView.setNumColumns(itemList.size());

					adapter = new MyListAdapter(MainActivity.this);
					listener = new ItemClickEvent();
					gridView.setAdapter(adapter);
					gridView.setOnItemClickListener(listener);
				}
				break;
			}
		}
	}

	private class MyListAdapter extends BaseAdapter {
		private final String TAG = "MyListAdapter";
		private LayoutInflater mInflater;
		private final Context context;

		public MyListAdapter(Context context) {
			this.context = context;
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return itemList.size();
		}

		public Object getItem(int position) {
			return itemList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			Item item = itemList.get(position);
			CellHolder cellHolder;
			if (convertView == null) {
				cellHolder = new CellHolder();
				convertView = mInflater.inflate(R.layout.item, null);
				cellHolder.ivIcon = (ImageView) convertView
						.findViewById(R.id.ivIcon);
				cellHolder.tvTitle = (TextView) convertView
						.findViewById(R.id.tvTitle);
				cellHolder.tvContent = (TextView) convertView
						.findViewById(R.id.tvContent);
				convertView.setTag(cellHolder);
			} else {
				cellHolder = (CellHolder) convertView.getTag();
			}
			cellHolder.ivIcon.setImageResource(R.drawable.ic_launcher);
			cellHolder.tvTitle.setText(item.getTitle());
			cellHolder.tvContent.setText(item.getContent());
			return convertView;
		}
	}

	private class CellHolder {
		ImageView ivIcon;
		TextView tvTitle;
		TextView tvContent;
	}

	private class ItemClickEvent implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

		}

	}

}
