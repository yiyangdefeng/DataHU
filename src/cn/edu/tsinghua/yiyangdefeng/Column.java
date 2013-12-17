package cn.edu.tsinghua.yiyangdefeng;

import java.util.ArrayList;
import java.util.List;

public class Column {
	protected float width;
	protected List<Long> data;
	protected String unit;
	protected String notes;
	protected VarType vt;
	protected int rows;
	
	public Column(int rows, float width) {
		data = new ArrayList<Long>();
		this.width = width;
		unit = "";
		notes = "";
		vt = VarType.X;
		for (int i = 0; i < rows; i++) {
			data.add(0l);
		}
	}
	
	public void setRows(int rows) {
		if(rows < 0) {
			return;
		}
		if(rows > this.rows) {
			for (int i = this.rows; i < rows; i++) {
				data.add(0l);
			}
		}
		
	}
	
	//set functions
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public void setData(long newdata,int row) {
		this.data.set(row,newdata);
	}
	
	public void setWidth(float width) {
		if(width < 1) {
			return;
		}
		this.width = width;
	}
	
	public void setType (VarType type) {
		this.vt = type;
	}
	
	//get functions
	public float getWidth() {
		return width;
	}
	
	public long getData(int row) {
		return data.get(row);
	}
	
	public List<Long> getAllData() {
		return data;
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
