package cn.edu.tsinghua.yiyangdefeng;

public class CommonTools {
	public static String ChangeNumberintoLetter(int number) {
		if (number > 0 && number < 27) {
			return String.valueOf((char) (number + 64));
		} else if (number > 26 && number < 26 * 27 + 1) {
			char[] tempchar = new char[2];
			tempchar[1] = (char) ((number % 26) + 65);
			if (tempchar[1] == '@') {
				tempchar[1] = 'Z';
			}
			tempchar[0] = (char) ((number - number % 26) / 26 + 64);
			return String.valueOf(tempchar);
		} else
			return "Unknown";
	}
	
	//operators  contains "sin","cos","tan","1/x","x^y","ln","exp","arcsin","arccos","arctan","abs"
	public static Double Calculate(double first,double second,String operator) {
		if (operator.equals("+")) {
			return first + second;
		} else if (operator.equals("-")) {
			return first - second;
		} else if (operator.equals("*")) {
			return first * second;
		} else if (operator.equals("/")) {
			return first / second;
		} else if (operator.equals("sin")) {
			return Math.sin(first);
		} else if (operator.equals("cos")) {
			return Math.cos(first);
		} else if (operator.equals("tan")) {
			return Math.tan(first);
		} else if (operator.equals("1/x")) {
			return 1 / first;
		} else if (operator.equals("ln")) {
			return Math.log(first);
		} else if (operator.equals("exp")) {
			return Math.exp(first);
		} else if (operator.equals("arcsin")) {
			return Math.asin(first);
		} else if (operator.equals("arccos")) {
			return Math.acos(first);
		} else if (operator.equals("arctan")) {
			return Math.atan(first);
		} else if (operator.equals("x^y")) {
			return Math.pow(first, second);
		} else if (operator.equals("abs")) {
			return Math.abs(first);
		}
		
		return null;
	}
}
