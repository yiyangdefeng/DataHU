package cn.edu.tsinghua.yiyangdefeng;

public class InvalidInputException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidInputException() {
		super("Invalid Input! Please check again.");
	}
}
