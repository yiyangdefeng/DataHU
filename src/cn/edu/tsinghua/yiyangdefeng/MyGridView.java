package cn.edu.tsinghua.yiyangdefeng;

import android.content.Context;
import android.widget.GridView;

public class MyGridView extends GridView {
	public MyGridView(Context c) {
		super(c);
	}
	@Override
	public boolean isFocused() {
		return true;
	}
}
