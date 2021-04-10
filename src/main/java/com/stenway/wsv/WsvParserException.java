package com.stenway.wsv;

public class WsvParserException extends RuntimeException {
	public final int Index;
	public final int LineIndex;
	public final int LinePosition;
	
	WsvParserException(int index, int lineIndex, int linePosition, String message) {
		super(String.format("%s (%d, %d)", message, lineIndex + 1, linePosition + 1));
		Index = index;
		LineIndex = lineIndex;
		LinePosition = linePosition;
	}
}