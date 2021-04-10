package com.stenway.wsv;

import java.util.ArrayList;

class WsvParser {
	private static final String MULTIPLE_WSV_LINES_NOT_ALLOWED = "Multiple WSV lines not allowed";
	private static final String UNEXPECTED_PARSER_ERROR = "Unexpected parser error";
	
	private static WsvLine parseLine(WsvCharIterator iterator, 
			ArrayList<String> values, ArrayList<String> whitespaces) {
		values.clear();
		whitespaces.clear();
		
		String whitespace = iterator.readWhitespaceOrNull();
		whitespaces.add(whitespace);

		while (!iterator.isChar('\n') && !iterator.isEndOfText()) {
			String value;
			if(iterator.isChar('#')) {
				break;
			} else if(iterator.tryReadChar('"')) {
				value = iterator.readString();
			} else {
				value = iterator.readValue();
				if (value.equals("-")) {
					value = null;
				}
			}
			values.add(value);

			whitespace = iterator.readWhitespaceOrNull();
			if (whitespace == null) {
				break;
			}
			whitespaces.add(whitespace);
		}
		
		String comment = null;
		if(iterator.tryReadChar('#')) {
			comment = iterator.readCommentText();
			if (whitespace == null) {
				whitespaces.add(null);
			}
		}

		String[] valueArray = new String[values.size()];
		String[] whitespaceArray = new String[whitespaces.size()];
		values.toArray(valueArray);
		whitespaces.toArray(whitespaceArray);

		WsvLine newLine = new WsvLine();
		newLine.set(valueArray, whitespaceArray, comment);
		return newLine;
	}
	
	public static WsvLine parseLine(String content) {
		WsvCharIterator iterator = new WsvCharIterator(content);
		ArrayList<String> values = new ArrayList<>();
		ArrayList<String> whitespaces = new ArrayList<>();
		
		WsvLine newLine = parseLine(iterator, values, whitespaces);
		if (iterator.isChar('\n')) {
			throw iterator.getException(MULTIPLE_WSV_LINES_NOT_ALLOWED);
		} else if (!iterator.isEndOfText()) {
			throw iterator.getException(UNEXPECTED_PARSER_ERROR);
		}
		
		return newLine;
	}
	
	public static WsvDocument parseDocument(String content) {
		WsvDocument document = new WsvDocument();
		
		WsvCharIterator iterator = new WsvCharIterator(content);
		ArrayList<String> values = new ArrayList<>();
		ArrayList<String> whitespaces = new ArrayList<>();
		
		while (true) {
			WsvLine newLine = parseLine(iterator, values, whitespaces);
			document.addLine(newLine);
			
			if (iterator.isEndOfText()) {
				break;
			} else if(!iterator.tryReadChar('\n')) {
				throw iterator.getException(UNEXPECTED_PARSER_ERROR);
			}
		}
		
		if (!iterator.isEndOfText()) {
			throw iterator.getException(UNEXPECTED_PARSER_ERROR);
		}

		return document;
	}
	
	public static WsvLine parseLineNonPreserving(String content) {
		String[] values = parseLineAsArray(content);
		return new WsvLine(values);
	}
	
	public static WsvDocument parseDocumentNonPreserving(String content) {
		WsvDocument document = new WsvDocument();
		
		WsvCharIterator iterator = new WsvCharIterator(content);
		ArrayList<String> values = new ArrayList<>();
		
		while (true) {
			String[] lineValues = parseLineAsArray(iterator, values);
			WsvLine newLine = new WsvLine(lineValues);
			document.addLine(newLine);
			
			if (iterator.isEndOfText()) {
				break;
			} else if(!iterator.tryReadChar('\n')) {
				throw iterator.getException(UNEXPECTED_PARSER_ERROR);
			}
		}
		
		if (!iterator.isEndOfText()) {
			throw iterator.getException(UNEXPECTED_PARSER_ERROR);
		}

		return document;
	}
	
	public static String[][] parseDocumentAsJaggedArray(String content) {
		WsvCharIterator iterator = new WsvCharIterator(content);
		ArrayList<String> values = new ArrayList<>();
		ArrayList<String[]> lines = new ArrayList<>();
		
		while (true) {
			String[] newLine = parseLineAsArray(iterator, values);
			lines.add(newLine);
			
			if (iterator.isEndOfText()) {
				break;
			} else if(!iterator.tryReadChar('\n')) {
				throw iterator.getException(UNEXPECTED_PARSER_ERROR);
			}
		}
		
		if (!iterator.isEndOfText()) {
			throw iterator.getException(UNEXPECTED_PARSER_ERROR);
		}
		
		String[][] linesArray = new String[lines.size()][];
		lines.toArray(linesArray);
		return linesArray;
	}
	
	public static String[] parseLineAsArray(String content) {
		WsvCharIterator iterator = new WsvCharIterator(content);
		ArrayList<String> values = new ArrayList<>();
		String[] result = parseLineAsArray(iterator, values);
		if (iterator.isChar('\n')) {
			throw iterator.getException(MULTIPLE_WSV_LINES_NOT_ALLOWED);
		} else if (!iterator.isEndOfText()) {
			throw iterator.getException(UNEXPECTED_PARSER_ERROR);
		}
		return result;
	}
	
	private static String[] parseLineAsArray(WsvCharIterator iterator, ArrayList<String> values) {
		values.clear();
		iterator.skipWhitespace();

		while (!iterator.isChar('\n') && !iterator.isEndOfText()) {
			String value;
			if(iterator.isChar('#')) {
				break;
			} else if(iterator.tryReadChar('"')) {
				value = iterator.readString();
			} else {
				value = iterator.readValue();
				if (value.equals("-")) {
					value = null;
				}
			}
			values.add(value);

			if (!iterator.skipWhitespace()) {
				break;
			}
		}
		
		String comment = null;
		if(iterator.tryReadChar('#')) {
			iterator.skipCommentText();
		}

		String[] valueArray = new String[values.size()];
		values.toArray(valueArray);
		return valueArray;
	}
}
