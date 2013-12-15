package cn.edu.tsinghua.yiyangdefeng;

import java.util.ArrayList;
import java.util.List;


public class WholeSheet {
	protected int columns;
	protected int rows;
	protected float height;
	protected String name;
	protected String graphtitle;
	protected List<Column> columndata;
	
	
	public WholeSheet() {
		columns = 5;
		rows = 50;
		columndata = new ArrayList<Column>();
		for (int i = 0; i < columns;i++) {
			columndata.add(new Column(rows));
		}
		height = 60;
		name = "NewSheet";
		graphtitle = "NewGraph";
		
	}
	
	//set functions
	public void setColumns(int columns) {
		if (columns < 0) {
			return;
		}
		if (columns > this.columns) {
			for (int i = this.columns;i < columns;i++) {
				columndata.add(new Column(rows));
			}
		}
		this.columns = columns;
	}
	
	public void setRows(int rows) {
		if (rows < 0) {
			return;
		}
		if (rows > this.rows) {
			for (int i = 0; i < columns;i++) {
				columndata.get(i).setRows(rows);
			}
			
		}
		this.rows = rows;
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
		return columndata.get(rownumber);
	}
	
	//other functions
	//work of Zhang
	public void SaveData() {
		
	}
	
	//work of Zhang
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
		return rows;
	}
	
	public int calcWholeHeight() {
		return (int)((height + 1) * this.getMaxRowNumber());
	}
	
}
