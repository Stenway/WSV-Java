package com.stenway.wsv;

public class WsvLine {
	public String[] Values;
	
	String[] whitespaces;
	String comment;

	public WsvLine() {
		
	}
	
	public WsvLine(String... values) {
		Values = values;
		
		whitespaces = null;
		comment = null;
	}

	public WsvLine(String[] values, String[] whitespaces, String comment) {
		Values = values;
		
		setWhitespaces(whitespaces);
		setComment(comment);
	}
	
	public boolean hasValues() {
		return Values != null && Values.length > 0;
	}
	
	public void setValues(String... values) {
		Values = values;
	}
	
	public final void setWhitespaces(String... whitespaces) {
		validateWhitespaces(whitespaces);
		this.whitespaces = whitespaces;
	}
	
	public static void validateWhitespaces(String... whitespaces) {
		if (whitespaces != null) {
			for (String whitespace : whitespaces) {
				if (whitespace != null && whitespace.length() > 0 && !WsvString.isWhitespace(whitespace)) {
					throw new IllegalArgumentException(
							"Whitespace value contains non whitespace character or line feed");
				}
			}
		}
	}
	
	public String[] getWhitespaces() {
		if (whitespaces == null) {
			return null;
		}
		return whitespaces.clone();
	}
	
	public final void setComment(String comment) {
		validateComment(comment);
		this.comment = comment;
	}
	
	public static void validateComment(String comment) {
		if (comment != null && comment.indexOf('\n') >= 0) {
			throw new IllegalArgumentException(
					"Line feed in comment is not allowed");
		}
	}
	
	public String getComment() {
		return comment;
	}
	
	void set(String[] values, String[] whitespaces, String comment) {
		Values = values;
		this.whitespaces = whitespaces;
		this.comment = comment;
	}

	@Override
	public String toString() {
		return toString(true);
	}
	
	public String toString(boolean preserveWhitespaceAndComment) {
		if (preserveWhitespaceAndComment) {
			return WsvSerializer.serializeLine(this);
		} else {
			return WsvSerializer.serializeLineNonPreserving(this);
		}
	}
	
	public static WsvLine parse(String content) {
		return parse(content, true);
	}
	
	public static WsvLine parse(String content, boolean preserveWhitespaceAndComment) {
		if (preserveWhitespaceAndComment) {
			return WsvParser.parseLine(content);
		} else {
			return WsvParser.parseLineNonPreserving(content);
		}
	}
	
	public static String[] parseAsArray(String content) {
		return WsvParser.parseLineAsArray(content);
	}
}