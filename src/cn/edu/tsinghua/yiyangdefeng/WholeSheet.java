package cn.edu.tsinghua.yiyangdefeng;

import android.util.Log;

public class WholeSheet {
	protected int columns;
	protected float height;
	protected String name;
	protected String graphtitle;
	protected Column[] columndata;
	
	public WholeSheet() {
		columns = 5;
		columndata = new Column[columns];
		for (int i = 0; i < columns;i++) {
			columndata[i] = new Column();
		}
		if (columndata[0] == null) {
			Log.e("test","null pointer!");
		}
		height = 40;
		name = "NewSheet";
		graphtitle = "NewGraph";
		
	}
	
	//set functions
	public void setColumns(int columns) {
		if (columns < 0) {
			return;
		}
		this.columns = columns;
	}
	
	public void setHeight (float height) {
		if (height < 1) {
			return;
		}
		this.height = height;
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	public void setGraphTitle (String graphtitle) {
		this.graphtitle = graphtitle;
	}
	
    //get functions
	public int getColumns() {
		return this.columns;
	}
	
	public float getHeight () {
		return this.height;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getGraphTitle() {
		return this.graphtitle;
	}
	
	public Column getColumn(int rownumber) {
		return columndata[rownumber];
	}
	
	//other functions
	public void SaveData() {
		
	}
	
	public void LoadData() {
		
	}
	
	public Column createColumnbyFunction(Column[] originals,String function) {
		return null;
		//do nothing now
	}
	
	public int calcWholeWidth() {
		float wholewidth = 0;
		for (int i = 0; i < this.getColumns();i++) {
			wholewidth += this.getColumn(i).getWidth() + 1;
		}
		return (int)wholewidth;
	}
	
	public int getMaxRowNumber() {
		int maxrownumber = 0;
		for (int i = 0;i < this.getColumns();i++) {
			maxrownumber = Math.max(maxrownumber, this.getColumn(i).getRows());
		}
		return maxrownumber;
	}
	
	public int calcWholeHeight() {
		return (int)((height + 1) * this.getMaxRowNumber());
	}
	
}
