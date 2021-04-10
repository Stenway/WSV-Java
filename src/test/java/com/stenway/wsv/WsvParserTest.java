package com.stenway.wsv;

import org.junit.Test;

public class WsvParserTest {
	@Test
	public void test_parseDocument_exceptions() {
		parseDocumentAsJaggedArray_throws_exception("a b c \"hello world",	"String not closed (1, 19)");
		parseDocumentAsJaggedArray_throws_exception("a b c \"hello world\n",	"String not closed (1, 19)");
		
		parseDocumentAsJaggedArray_throws_exception("a b\"hello world\"",	"Invalid double quote in value (1, 4)");
		
		parseDocumentAsJaggedArray_throws_exception("\"hello world\"a b c",	"Invalid character after string (1, 14)");
		
		parseDocumentAsJaggedArray_throws_exception("\"Line1\"/ \"Line2\"",	"Invalid string line break (1, 9)");
		
		parseDocumentAsJaggedArray_throws_exception("Line1\na b c \"hello world",	"String not closed (2, 19)");
		parseDocumentAsJaggedArray_throws_exception("Line1\na b c \"hello world\n",	"String not closed (2, 19)");
		
		parseDocumentAsJaggedArray_throws_exception("Line1\na b\"hello world\"",		"Invalid double quote in value (2, 4)");
		
		parseDocumentAsJaggedArray_throws_exception("Line1\n\"hello world\"a b c",	"Invalid character after string (2, 14)");
		
		parseDocumentAsJaggedArray_throws_exception("Line1\n\"Line1\"/ \"Line2\"",	"Invalid string line break (2, 9)");
	}
	
	private void parseDocumentAsJaggedArray_throws_exception(String text, String expectedExceptionMessage) {
		try {
			WsvParser.parseDocumentAsJaggedArray(text);
		} catch (WsvParserException e) {
			Assert.equals(e.getMessage(), expectedExceptionMessage);
			return;
		}
		throw new RuntimeException("Text is valid");
	}
	
	@Test
	public void test_parseDocumentAsJaggedArray() {
		String[] lineValues_empty	= Utils.stringArray();
		String[] lineValues_a		= Utils.stringArray("a");
		String[] lineValues_a_b		= Utils.stringArray("a", "b");
		String[] lineValues_a_b_c	= Utils.stringArray("a", "b", "c");
		
		parseDocumentAsJaggedArray_equals("",			lineValues_empty);
		parseDocumentAsJaggedArray_equals("  ",			lineValues_empty);
		parseDocumentAsJaggedArray_equals("\t \t",		lineValues_empty);
		parseDocumentAsJaggedArray_equals("#",			lineValues_empty);
		parseDocumentAsJaggedArray_equals("#c",			lineValues_empty);
		parseDocumentAsJaggedArray_equals(" #",			lineValues_empty);
		parseDocumentAsJaggedArray_equals(" #c",			lineValues_empty);
		parseDocumentAsJaggedArray_equals("\t#c",		lineValues_empty);
		parseDocumentAsJaggedArray_equals("\u0009\u000B\u000C\u000C\u0020\u0085\u00A0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200A\u2028\u2029\u202F\u205F\u3000",		lineValues_empty);
		
		parseDocumentAsJaggedArray_equals("a",			lineValues_a);
		parseDocumentAsJaggedArray_equals(" a",			lineValues_a);
		parseDocumentAsJaggedArray_equals("a ",			lineValues_a);
		parseDocumentAsJaggedArray_equals(" a ",			lineValues_a);
		parseDocumentAsJaggedArray_equals("a#",			lineValues_a);
		parseDocumentAsJaggedArray_equals("a#c",			lineValues_a);
		parseDocumentAsJaggedArray_equals("a #c",		lineValues_a);
		
		parseDocumentAsJaggedArray_equals("a b",			lineValues_a_b);
		parseDocumentAsJaggedArray_equals(" a b",		lineValues_a_b);
		parseDocumentAsJaggedArray_equals("a b ",		lineValues_a_b);
		parseDocumentAsJaggedArray_equals(" a b ",		lineValues_a_b);
		parseDocumentAsJaggedArray_equals("  a   b  ",	lineValues_a_b);
		parseDocumentAsJaggedArray_equals("a b#",		lineValues_a_b);
		parseDocumentAsJaggedArray_equals(" a b#",		lineValues_a_b);
		parseDocumentAsJaggedArray_equals("a b #",		lineValues_a_b);
		parseDocumentAsJaggedArray_equals(" a b #",		lineValues_a_b);
		parseDocumentAsJaggedArray_equals("  a   b  #",	lineValues_a_b);
		
		parseDocumentAsJaggedArray_equals("a b c",		lineValues_a_b_c);
		
		parseDocumentAsJaggedArray_equals_serialized("\"Hello world\"");
		parseDocumentAsJaggedArray_equals_serialized("\"Hello world\" \"Hello world\"");
		parseDocumentAsJaggedArray_equals_serialized("- -");
		parseDocumentAsJaggedArray_equals_serialized("\"-\" \"-\"");
		parseDocumentAsJaggedArray_equals_serialized("\"\" \"\"");
		parseDocumentAsJaggedArray_equals_serialized("\"\"\"\" \"\"\"\"");
		parseDocumentAsJaggedArray_equals_serialized("\"\"/\"\" \"\"/\"\"");
		parseDocumentAsJaggedArray_equals_serialized("\"Line1\"/\"Line2\" \"Line1\"/\"Line2\"");
	}
	
	private void parseDocumentAsJaggedArray_equals(String text, String[] expectedLineValues) {
		String[][] actualLines = WsvParser.parseDocumentAsJaggedArray(text);
		Assert.equals(actualLines.length, 1);
		Assert.array_equals(actualLines[0], expectedLineValues);
	}
	
	private void parseDocumentAsJaggedArray_equals_serialized(String textAndExpected) {
		Assert.equals(WsvSerializer.serializeDocument(WsvParser.parseDocumentAsJaggedArray(textAndExpected)),textAndExpected);
	}
}