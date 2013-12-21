package cn.edu.tsinghua.yiyangdefeng;

import java.util.ArrayList;
import java.util.List;

public class WholeSheet {
	protected int columns;
	protected int rows;
	protected float height;
	protected float width;
	protected String name;
	protected String graphtitle;
	protected List<Column> columndata;

	public WholeSheet() {
		columns = 2;
		rows = 30;
		width = 300;
		columndata = new ArrayList<Column>();
		for (int i = 0; i < columns; i++) {
			columndata.add(new Column(rows, width));
		}
		height = 80;
		columndata.get(0).setType(VarType.X);
		columndata.get(1).setType(VarType.Y);
		name = "NewSheet";
		graphtitle = "NewGraph";

	}
	
	public WholeSheet(int columns,int rows) {
		this.columns = columns;
		this.rows = rows;
		width = 200;
		columndata = new ArrayList<Column>();
		for (int i = 0; i < columns; i++) {
			columndata.add(new Column(rows, width));
		}
		height = 60;
		name = "NewSheet";
		graphtitle = "NewGraph";
	}
	
	public WholeSheet(int columns,int rows,float width,float height,String dataname,String graphtitle) {
		this.columns = columns;
		this.rows = rows;
		this.width = width;
		columndata = new ArrayList<Column>();
		for (int i = 0; i < columns; i++) {
			columndata.add(new Column(rows, width));
		}
		this.height = height;
		this.name = dataname;
		this.graphtitle = graphtitle;
	}

	public float getWidth() {
		return this.width;
	}

	public void setWidth(float width) {
		for (int i = 0; i < this.columns; i++) {
			columndata.get(i).setWidth(width);
		}
		this.width = width;
	}

	// set functions
	public void setColumns(int columns) {
		if (columns < 0) {
			return;
		}
		if (columns > this.columns) {
			for (int i = this.columns; i < columns; i++) {
				columndata.add(new Column(rows, width));
			}
		}
		this.columns = columns;
	}

	public void setRows(int rows) {
		if (rows < 0) {
			return;
		}
		if (rows > this.rows) {
			for (int i = 0; i < columns; i++) {
				columndata.get(i).setRows(rows);
			}

		}
		this.rows = rows;
	}

	public void setHeight(float height) {
		if (height < 1) {
			return;
		}
		this.height = height;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGraphTitle(String graphtitle) {
		this.graphtitle = graphtitle;
	}

	// get functions
	public int getColumns() {
		return this.columns;
	}

	public float getHeight() {
		return this.height;
	}

	public String getName() {
		return this.name;
	}

	public String getGraphTitle() {
		return this.graphtitle;
	}

	public Column getColumn(int columnnumber) {
		return columndata.get(columnnumber);
	}

	// other functions
	// work of Zhang
	public void SaveData() {

	}

	// work of Zhang
	public void LoadData() {

	}

	public Column createColumnbyFunction(Column[] originals, String function) {
		return null;
		// do nothing now
	}

	public int calcWholeWidth() {
		return (int) (this.columns * this.width);
	}

	public int calcWholeHeight() {
		return (int) (this.height * this.rows);
	}

	public int getRows() {
		return this.rows;
	}

	public void eraseRow(int selectedrow) {
		if (selectedrow < 0 || selectedrow > rows - 1) {
			return;
		} else {
			for (int i = 0; i < columns; i++) {
				columndata.get(i).data.remove(selectedrow);
			}
			rows = rows - 1;
		}
	}

	public void eraseColumn(int selectedcolumn) {
		if (selectedcolumn < 0 || selectedcolumn > columns - 1) {
			return;
		} else {
			columndata.remove(selectedcolumn);
			columns = columns - 1;
		}
	}
	
	public void insertRow() {
		//add to the last of the sheet
		for(int i = 0;i < columns;i++) {
			columndata.get(i).data.add(null);
		}
		rows = rows + 1;
	}
	
	public void insertRow(int therowbefore) {
		for (int i = 0;i < columns;i++) {
			columndata.get(i).data.add(therowbefore, null);
		}
		rows = rows + 1;
	}
	
	public void insertColumn() {
		//add to the last of the sheet, default Y vartype generated
		Column column = new Column(rows,width);
		column.setType(VarType.Y);
		columndata.add(column);
		columns = columns + 1;
	}
	
	public void insertColumn(int thecolumnbefore) {
		columndata.add(thecolumnbefore,new Column(rows,width));
		columns = columns + 1;
	}
	
	public void cleardata(int column,int row) {
		columndata.get(column).data.set(row, null);
	}
}
