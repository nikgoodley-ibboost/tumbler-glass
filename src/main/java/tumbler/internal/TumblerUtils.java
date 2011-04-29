package tumbler.internal;

import java.util.*;

public class TumblerUtils {

	public static String decamelise(String testName) {
		String nocamel = charAtToUpper(0, testName);
		for (int i = 1; i < testName.length(); i++) {
			char nextChar = testName.charAt(i);
			if (isUpper(nextChar)) {
				nocamel += spaceAndToLower(nextChar);
			} else {
				nocamel += nextChar;
			}
		}
		return nocamel;
	}

	private static String spaceAndToLower(char character) {
		return " " + (character + "").toLowerCase();
	}

	private static boolean isUpper(char nextChar) {
		return nextChar >= 'A' && nextChar <= 'Z';
	}

	private static String charAtToUpper(int at, String text) {
		return (text.charAt(at) + "").toUpperCase();
	}

	public static String camelise(String name) {
		String camelisedName = "";
		StringTokenizer tokenizer = new StringTokenizer(name, " ");
		while (tokenizer.hasMoreTokens()) {
			String nextToken = tokenizer.nextToken();
			camelisedName += charAtToUpper(0, nextToken) + nextToken.substring(1);
		}
		return camelisedName;
	}

	public static String removeSpecialCharacters(String name) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (c == ' ' || isBetween(c, '0', '9') || isBetween(c, 'A', 'Z') || c == '_' || isBetween(c, 'a', 'z'))
				sb.append(c);
		}
		return sb.toString();
	}

	private static boolean isBetween(char c, int min, int max) {
		return c >= min && c <= max;
	}

}
