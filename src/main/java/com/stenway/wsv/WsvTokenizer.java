package com.stenway.wsv;

import com.stenway.reliabletxt.ReliableTxtCharIterator;
import java.util.ArrayList;
import java.util.Arrays;

public class WsvTokenizer extends ReliableTxtCharIterator {
	private ArrayList<Integer> tokens = new ArrayList<>();
	
	public static final int TokenTypeLineBreak = 0;
	public static final int TokenTypeWhitespace = 1;
	public static final int TokenTypeComment = 2;
	public static final int TokenTypeNull = 3;
	public static final int TokenTypeValue = 4;
	public static final int TokenTypeStringStart = 5;
	public static final int TokenTypeStringEnd = 6;
	public static final int TokenTypeStringText = 7;
	public static final int TokenTypeStringEscapedDoubleQuote = 8;
	public static final int TokenTypeStringLineBreak = 9;
	public static final int TokenTypeEndOfText = 10;
	public static final int TokenTypeError = 11;
	
	private int lastTokenEndIndex = 0;
	
	public WsvTokenizer(String text) {
		super(text);
	}
	
	private void addToken(int tokenType, int length) {
		tokens.add(tokenType);
		tokens.add(length);
		lastTokenEndIndex += length;
	}
	
	private void readComment() {
		int startIndex = index;
		while (!isChar('\n') && !isEndOfText()) {
			index++;
		}
		addToken(TokenTypeComment, index - startIndex);
	}
	
	private boolean readWhitespace() {
		int startIndex = index;
		while (true) {
			if (isEndOfText()) break;
			int c = chars[index];
			if (c == '\n') break;
			if (!WsvChar.isWhitespace(c)) break;
			index++;
		}
		if (index == startIndex) return false;
		addToken(TokenTypeWhitespace, index-startIndex);
		return true;
	}
	
	private boolean isWhitespace() {
		if (isEndOfText()) return false;
		return WsvChar.isWhitespace(chars[index]);
	}
	
	private boolean readString() {
		int stringStartIndex = index-1;
		int tokenStartCount = tokens.size();
		addToken(TokenTypeStringStart, 1);
		int partLength = 0;
		while (true) {
			if (isEndOfText() || isChar('\n')) {
				while (tokens.size() > tokenStartCount) {
					tokens.remove(tokens.size()-1);
				}
				lastTokenEndIndex = stringStartIndex;
				return false;
			}
			int c = chars[index];
			if (c == '"') {
				if (partLength > 0) {
					addToken(TokenTypeStringText, partLength);
					partLength = 0;
				}
				index++;
				if (tryReadChar('"')) {
					addToken(TokenTypeStringEscapedDoubleQuote, 2);
				} else if(tryReadChar('/')) {
					if (!tryReadChar('"')) {
						addToken(TokenTypeStringEnd, 1);
						return false;
					}
					addToken(TokenTypeStringStart, 1);
					addToken(TokenTypeStringLineBreak, 1);
					addToken(TokenTypeStringEnd, 1);
				} else if (isWhitespace() || isChar('\n') || isChar('#') || isEndOfText() ) {
					break;
				} else {
					addToken(TokenTypeStringEnd, 1);
					index++;
					return false;
				}
			} else {
				partLength++;
				index++;
			}
		}
		addToken(TokenTypeStringEnd, 1);
		return true;
	}
	
	private boolean readValue() {
		int startIndex = index;
		int lastChar = 0;
		boolean result = true;
		while (true) {
			if (isEndOfText()) {
				break;
			}
			int c = chars[index];
			if (WsvChar.isWhitespace(c) || c == '\n' || c == '#') {
				break;
			}
			if (c == '\"') {
				result = false;
				break;
			}
			lastChar = c;
			index++;
		}
		if (index == startIndex) {
			return false;
		}
		int length = index-startIndex;
		if (length == 1 && lastChar == '-') {
			addToken(TokenTypeNull, 1);
		} else {
			addToken(TokenTypeValue, length);
		}
		if (result == false) {
			index++;
		}
		return result;
	}
	
	private boolean readLine() {
		readWhitespace();
		
		while (!isChar('\n') && !isEndOfText()) {
			String value;
			if(isChar('#')) {
				readComment();
				break;
			} else if(tryReadChar('"')) {
				if (!readString()) {
					return false;
				}
			} else {
				if (!readValue()) {
					return false;
				}
			}

			if (!readWhitespace()) {
				break;
			}
		}
		return true;
	}
	
	public int[] tokenize() {
		while (true) {
			if (!readLine()) {
				break;
			}
			if (tryReadChar('\n')) {
				addToken(TokenTypeLineBreak, 1);
			} else {
				break;
			}
		}
		if (lastTokenEndIndex == chars.length) {
			addToken(TokenTypeEndOfText, 0);
		} else {
			addToken(TokenTypeError, index-lastTokenEndIndex);
		}
		
		return tokens.stream().mapToInt(i -> i).toArray();
	}
	
	public static int[] tokenize(String text) {
		return new WsvTokenizer(text).tokenize();	
	}
}
