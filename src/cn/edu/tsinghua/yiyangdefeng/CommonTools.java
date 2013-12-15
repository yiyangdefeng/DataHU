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
}
