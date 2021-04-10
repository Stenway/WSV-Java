package com.stenway.wsv;

public class WsvString {
	public static boolean isWhitespace(String str) {
		if (str == null || str.length() == 0) {
			return false;
		}
		for (int i=0; i<str.length(); i++) {
			char c = str.charAt(i);
			if (!WsvChar.isWhitespace(c)) {
				return false;
			}
		}
		return true;
	}
}
