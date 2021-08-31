package com.stenway.wsv;

import com.stenway.reliabletxt.ReliableTxtCharIterator;

class WsvCharIterator extends ReliableTxtCharIterator {
	public WsvCharIterator(String text) {
		super(text);
	}
	
	public boolean isWhitespace() {
		if (isEndOfText()) return false;
		return WsvChar.isWhitespace(chars[index]);
	}
	
	public String readCommentText() {
		int startIndex = index;
		while (true) {
			if (isEndOfText()) break;
			if (chars[index] == '\n') break;
			index++;
		}
		return new String(chars,startIndex,index-startIndex);
	}
	
	public void skipCommentText() {
		while (true) {
			if (isEndOfText()) break;
			if (chars[index] == '\n') break;
			index++;
		}
	}

	public String readWhitespaceOrNull() {
		int startIndex = index;
		while (true) {
			if (isEndOfText()) break;
			int c = chars[index];
			if (c == '\n') break;
			if (!WsvChar.isWhitespace(c)) break;
			index++;
		}
		if (index == startIndex) return null;
		return new String(chars,startIndex,index-startIndex);
	}
	
	public boolean skipWhitespace() {
		int startIndex = index;
		while (true) {
			if (isEndOfText()) break;
			int c = chars[index];
			if (c == '\n') break;
			if (!WsvChar.isWhitespace(c)) break;
			index++;
		}
		return index > startIndex;
	}

	public String readString() {
		sb.setLength(0);
		while (true) {
			if (isEndOfText() || isChar('\n')) {
				throw getException("String not closed");
			}
			int c = chars[index];
			if (c == '"') {
				index++;
				if (tryReadChar('"')) {
					sb.append('"');
				} else if(tryReadChar('/')) {
					if (!tryReadChar('"')) {
						throw getException("Invalid string line break");
					}
					sb.append('\n');
				} else if (isWhitespace() || isChar('\n') || isChar('#') || isEndOfText() ) {
					break;
				} else {
					throw getException("Invalid character after string");
				}
			} else {
				sb.appendCodePoint(c);
				index++;
			}
		}
		return sb.toString();
	}

	public String readValue() {
		int startIndex = index;
		while (true) {
			if (isEndOfText()) {
				break;
			}
			int c = chars[index];
			if (WsvChar.isWhitespace(c) || c == '\n' || c == '#') {
				break;
			}
			if (c == '\"') {
				throw getException("Invalid double quote after value");
			}
			index++;
		}
		if (index == startIndex) {
			throw getException("Invalid value");
		}
		return new String(chars,startIndex,index-startIndex);
	}
	
	public WsvParserException getException(String message) {
		int[] lineInfo = getLineInfo();
		return new WsvParserException(index, lineInfo[0], lineInfo[1], message);
	}
}
