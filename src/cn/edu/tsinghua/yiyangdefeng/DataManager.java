package cn.edu.tsinghua.yiyangdefeng;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;


//saving format:
//row 0 : columns,XXX,rows,XXX,width,XXX,height,XXX,dataname,XXX,graphtitle,XXX
//row 1 : instruction of each column 
//row 2 : unit of each column
//row 3 : type of each column
public class DataManager {
	public WholeSheet openFile(File filename) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = "";
		line = br.readLine();
		StringTokenizer st = new StringTokenizer(line,",");
		st.nextToken();
		int columns = Integer.parseInt(st.nextToken());
		st.nextToken();
		int rows = Integer.parseInt(st.nextToken());
		st.nextToken();
		float width = Float.parseFloat(st.nextToken());
		st.nextToken();
		float height = Float.parseFloat(st.nextToken());
		st.nextToken();
		String dataname = st.nextToken();
		st.nextToken();
		String graphtitle = st.nextToken();
		st.nextToken();
		int digit = Integer.parseInt(st.nextToken());
		WholeSheet wholesheet = new WholeSheet(columns,rows,width,height,dataname,graphtitle);
		wholesheet.setDigit(digit);
		
		line = br.readLine();
		st = new StringTokenizer(line,",");
		for (int i = 0; i < columns;i++) {
			String instruction = st.nextToken();
			wholesheet.getColumn(i).setNotes(instruction);
		}
		
		line = br.readLine();
		st = new StringTokenizer(line,",");
		for (int i = 0; i < columns;i++) {
			String unit = st.nextToken();
			wholesheet.getColumn(i).setUnit(unit);
		}
		
		line = br.readLine();
		st = new StringTokenizer(line,",");
		for (int i = 0; i < columns;i++) {
			String type = st.nextToken();
			if (type.equals("X")) {
				wholesheet.getColumn(i).setType(VarType.X);
			} else if (type.equals("Y")) {
				wholesheet.getColumn(i).setType(VarType.Y);
			}
		}
		
		int currentrow = 0;
		while ((line = br.readLine()) != null) {
			st = new StringTokenizer(line,",");
			for (int i = 0; i < columns;i++) {
				double data = Double.parseDouble(st.nextToken());
				wholesheet.getColumn(i).data.set(currentrow, data);
			}
			currentrow++;
		}
		br.close();
		return wholesheet;
	}
	
	//saving format:
	//row 0 : columns,XXX,rows,XXX,width,XXX,height,XXX,dataname,XXX,graphtitle,XXX,digit,XXX
	//row 1 : instruction of each column 
	//row 2 : unit of each column
	//row 3 : type of each column
	public void saveFile (WholeSheet wholesheet,File filename) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
		bw.write("columns,");
		bw.write(String.valueOf(wholesheet.getColumns()) + ",");
		bw.write("rows,");
		bw.write(String.valueOf(wholesheet.getRows()) + ",");
		bw.write("width,");
		bw.write(String.valueOf(wholesheet.getWidth()) + ",");
		bw.write("height,");
		bw.write(String.valueOf(wholesheet.getHeight()) + ",");
		bw.write("dataname,");
		bw.write(wholesheet.getName() + ",");
		bw.write("graphtitle,");
		bw.write(wholesheet.getGraphTitle() + ",");
		bw.write("digit,");
		bw.write(String.valueOf(wholesheet.getDigit()));
		bw.write("\n");
		int columns = wholesheet.getColumns();
		int rows = wholesheet.getRows();
		
		for (int i = 0; i < columns - 1;i++) {
			bw.write(wholesheet.getColumn(i).getNotes() + ",");
		}
		bw.write(wholesheet.getColumn(columns - 1).getNotes());
		bw.write("\n");
		
		for (int i = 0; i < columns - 1;i++) {
			bw.write(wholesheet.getColumn(i).getUnit() + ",");
		}
		bw.write(wholesheet.getColumn(columns - 1).getUnit());
		bw.write("\n");
		
		for (int i = 0; i < columns - 1;i++) {
			bw.write(wholesheet.getColumn(i).getType() + ",");
		}
		bw.write(wholesheet.getColumn(columns - 1).getType());
		bw.write("\n");
		
		for (int j = 0; j < rows; j++) {
			for (int i = 0; i < columns - 1;i++) {
				bw.write(wholesheet.getColumn(i).getData(j) + ",");
			}
			bw.write(String.valueOf(wholesheet.getColumn(columns - 1).getData(j)));
			bw.write("\n");
		}
		bw.close();
	}
	
}
