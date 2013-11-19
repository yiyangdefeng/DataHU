package cn.edu.tsinghua.yiyangdefeng;

public class Column {
	protected int rows;
	protected float width;
	protected long[] data;
	protected String unit;
	protected String notes;
	protected VarType vt;
	
	public Column() {
		rows = 100;
		data = new long[rows];
		width = 120;
		unit = "";
		notes = "";
	}
	
	//set functions
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public void setData(long newdata,int row) {
		this.data[row] = newdata;
	}
	
	public void setWidth(float width) {
		if(width < 1) {
			return;
		}
		this.width = width;
	}
	
	public void setRows (int rownum) {
		if(rownum < 0) {
			return;
		}
		this.rows = rownum;
	}
	
	public void setType (VarType type) {
		this.vt = type;
	}
	
	//get functions
	public float getWidth() {
		return width;
	}
	
	public long getData(int row) {
		return data[row];
	}
	
	public long[] getAllData() {
		return data;
	}
	
	public int getRows() {
		return this.rows;
	}
	
	public String getNotes() {
		return this.notes;
	}
	
	public String getUnit() {
		return this.unit;
	}
	
}

enum VarType {
	X,Y
}
