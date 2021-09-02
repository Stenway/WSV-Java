package com.stenway.wsv;

import org.junit.Test;

public class WsvLineTest {

	@Test
	public void setComment_LineFeedGiven_ShouldThrowException() {
		setComment_LineFeedGiven_ShouldThrowException("\n");
	}
	
	private void setComment_LineFeedGiven_ShouldThrowException(String comment) {
		try {
			WsvLine wsvLine = new WsvLine();
			wsvLine.setComment(comment);
		} catch (Exception e) {
			return;
		}
		throw new RuntimeException("Comment is valid");
	}
	
	@Test
	public void setWhitespaces_LineFeedGiven_ShouldThrowException() {
		setWhitespaces_InvalidWhitespaceGiven_ShouldThrowException("\n");
	}
	
	private void setWhitespaces_InvalidWhitespaceGiven_ShouldThrowException(String... whitespaces) {
		try {
			WsvLine wsvLine = new WsvLine();
			wsvLine.setWhitespaces(whitespaces);
		} catch (Exception e) {
			return;
		}
		throw new RuntimeException("Whitespaces are valid");
	}
	
	@Test
	public void parse_InvalidTextGiven_ShouldThrowExeption() {
		parse_InvalidTextGiven_ShouldThrowExeption("a b c\n",				"Multiple WSV lines not allowed (1, 6)");
		
		parse_InvalidTextGiven_ShouldThrowExeption("a b c \"hello world",	"String not closed (1, 19)");
		parse_InvalidTextGiven_ShouldThrowExeption("a b c \"hello world\n",	"String not closed (1, 19)");
		
		parse_InvalidTextGiven_ShouldThrowExeption("a b\"hello world\"",	"Invalid double quote after value (1, 4)");
		
		parse_InvalidTextGiven_ShouldThrowExeption("\"hello world\"a b c",	"Invalid character after string (1, 14)");
		
		parse_InvalidTextGiven_ShouldThrowExeption("\"Line1\"/ \"Line2\"",	"Invalid string line break (1, 9)");
	}
	
	private void parse_InvalidTextGiven_ShouldThrowExeption(String line, String expectedExceptionMessage) {
		try {
			WsvLine.parse(line);
		} catch (WsvParserException e) {
			Assert.equals(e.getMessage(), expectedExceptionMessage);
			return;
		}
		throw new RuntimeException("Line is valid");
	}
	
	@Test
	public void parse() {
		parse_equals_toString("");
		parse_equals_toString("a");
		parse_equals_toString("a b");
		parse_equals_toString("a b c");
		
		parse_equals_toString(" ");
		parse_equals_toString(" a");
		parse_equals_toString(" a b");
		parse_equals_toString(" a b c");
		
		parse_equals_toString("  ");
		parse_equals_toString(" a ");
		parse_equals_toString(" a  b ");
		parse_equals_toString(" a  b  c ");
		
		parse_equals_toString("\t");
		parse_equals_toString("\ta\t");
		parse_equals_toString("\ta\tb\t");
		parse_equals_toString("\ta\tb\tc\t");
		
		parse_equals_toString("#c");
		parse_equals_toString("a#c");
		parse_equals_toString("a b#c");
		parse_equals_toString("a b c#c");
		
		parse_equals_toString(" #c");
		parse_equals_toString(" a#c");
		parse_equals_toString(" a b#c");
		parse_equals_toString(" a b c#c");
		
		parse_equals_toString("  #c");
		parse_equals_toString(" a #c");
		parse_equals_toString(" a  b #c");
		parse_equals_toString(" a  b  c #c");
		
		parse_equals_toString("\t#c");
		parse_equals_toString("\ta\t#c");
		parse_equals_toString("\ta\tb\t#c");
		parse_equals_toString("\ta\tb\tc\t#c");
		
		parse_equals_toString("\"Hello world\"");
		parse_equals_toString("\"Hello world\" \"Hello world\"");
		parse_equals_toString("\"Hello world\" \"Hello world\" ");
		parse_equals_toString("- -");
		parse_equals_toString("\"-\" \"-\"");
		parse_equals_toString("\"\" \"\"");
		parse_equals_toString("\"\"\"\" \"\"\"\"");
		parse_equals_toString("\"\"/\"\" \"\"/\"\"");
		parse_equals_toString("\"Line1\"/\"Line2\" \"Line1\"/\"Line2\"");
	}
	
	private void parse_equals_toString(String line) {
		Assert.equals(WsvLine.parse(line).toString(), line);
	}
	
	@Test
	public void parse_nonPreserving() {
		parse_nonPreserving_equals("a b c",				"a b c");
		parse_nonPreserving_equals("   a   b   c  ",	"a b c");
		parse_nonPreserving_equals("a b c#",			"a b c");
		parse_nonPreserving_equals("a b c#Comment",		"a b c");
	}
	
	private void parse_nonPreserving_equals(String line, String expected) {
		Assert.equals(WsvLine.parse(line, false).toString(), expected);
		Assert.equals(WsvLine.parse(line).toString(false), expected);
	}
	
	@Test
	public void toString_singleValue() {
		toString_singleValue("",				"\"\"");
		toString_singleValue("a",				"a");
		toString_singleValue("abc",				"abc");
		toString_singleValue("-",				"\"-\"");
		toString_singleValue(null,				"-");
		toString_singleValue("#",				"\"#\"");
		toString_singleValue("abc def",			"\"abc def\"");
		toString_singleValue("\"",				"\"\"\"\"");
		toString_singleValue("\n",				"\"\"/\"\"");
		toString_singleValue("Line1\nLine2",	"\"Line1\"/\"Line2\"");
	}
	
	private void toString_singleValue(String value, String expected) {
		Assert.equals(new WsvLine(new String[] {value}).toString(), expected);
	}
	
	@Test
	public void test_toString() {		
		String comment_null = null;
		String comment_empty = "";
		String comment = "c";
		
		String[] values_null = null;
		String[] values_empty = new String[] {};
		String[] values_a = new String[] {"a"};
		String[] values_a_b = new String[] {"a","b"};
		String[] values_a_b_c = new String[] {"a","b","c"};
		
		String[] ws_null = null;
		
		String[] ws_0 = new String[] {};
		String[] ws_1 = new String[] {"\t"};
		String[] ws_2 = new String[] {"\t","\t"};
		String[] ws_3 = new String[] {"\t","\t","\t"};
		String[] ws_4 = new String[] {"\t","\t","\t","\t"};
		
		String[] ws_e1 = new String[] {""};
		String[] ws_e2 = new String[] {"",""};
		String[] ws_e3 = new String[] {"","",""};
		String[] ws_e4 = new String[] {"","","",""};
		
		String[] ws_n1 = new String[] {null};
		String[] ws_n2 = new String[] {null,null};
		String[] ws_n3 = new String[] {null,null,null};
		String[] ws_n4 = new String[] {null,null,null,null};
		
		toString_equals(values_null, ws_null, comment_null,	"");
		toString_equals(values_null, ws_0, comment_null,	"");
		toString_equals(values_null, ws_1, comment_null,	"\t");
		toString_equals(values_null, ws_e1, comment_null,	"");
		toString_equals(values_null, ws_n1, comment_null,	"");
		
		toString_equals(values_null, ws_null, comment,	"#c");
		toString_equals(values_null, ws_0, comment,		"#c");
		toString_equals(values_null, ws_1, comment,		"\t#c");
		toString_equals(values_null, ws_e1, comment,	"#c");
		toString_equals(values_null, ws_n1, comment,	"#c");
		
		toString_equals(values_null, ws_null, comment_empty,	"#");
		
		toString_equals(values_a, ws_null, comment_null,	"a");
		toString_equals(values_a, ws_0, comment_null,		"a");
		toString_equals(values_a, ws_1, comment_null,		"\ta");
		toString_equals(values_a, ws_2, comment_null,		"\ta\t");
		toString_equals(values_a, ws_3, comment_null,		"\ta\t");
		toString_equals(values_a, ws_e1, comment_null,		"a");
		toString_equals(values_a, ws_e2, comment_null,		"a");
		toString_equals(values_a, ws_n1, comment_null,		"a");
		toString_equals(values_a, ws_n2, comment_null,		"a");
		
		toString_equals(values_a, ws_null, comment,	"a #c");
		toString_equals(values_a, ws_0, comment,	"a #c");
		toString_equals(values_a, ws_1, comment,	"\ta #c");
		toString_equals(values_a, ws_2, comment,	"\ta\t#c");
		toString_equals(values_a, ws_3, comment,	"\ta\t#c");
		toString_equals(values_a, ws_e1, comment,	"a #c");
		toString_equals(values_a, ws_e2, comment,	"a#c");
		toString_equals(values_a, ws_n1, comment,	"a #c");
		toString_equals(values_a, ws_n2, comment,	"a#c");

		toString_equals(values_a_b, ws_null, comment_null,	"a b");
		toString_equals(values_a_b, ws_0, comment_null,		"a b");
		toString_equals(values_a_b, ws_1, comment_null,		"\ta b");
		toString_equals(values_a_b, ws_2, comment_null,		"\ta\tb");
		toString_equals(values_a_b, ws_3, comment_null,		"\ta\tb\t");
		toString_equals(values_a_b, ws_e1, comment_null,	"a b");
		toString_equals(values_a_b, ws_e2, comment_null,	"a b");
		toString_equals(values_a_b, ws_e3, comment_null,	"a b");
		toString_equals(values_a_b, ws_n1, comment_null,	"a b");
		toString_equals(values_a_b, ws_n2, comment_null,	"a b");
		toString_equals(values_a_b, ws_n3, comment_null,	"a b");
		
		toString_equals(values_a_b, ws_null, comment,	"a b #c");
		toString_equals(values_a_b, ws_0, comment,		"a b #c");
		toString_equals(values_a_b, ws_1, comment,		"\ta b #c");
		toString_equals(values_a_b, ws_2, comment,		"\ta\tb #c");
		toString_equals(values_a_b, ws_3, comment,		"\ta\tb\t#c");
		toString_equals(values_a_b, ws_e1, comment,		"a b #c");
		toString_equals(values_a_b, ws_e2, comment,		"a b #c");
		toString_equals(values_a_b, ws_e3, comment,		"a b#c");
		toString_equals(values_a_b, ws_n1, comment,		"a b #c");
		toString_equals(values_a_b, ws_n2, comment,		"a b #c");
		toString_equals(values_a_b, ws_n3, comment,		"a b#c");
		
		toString_equals(values_a_b_c, ws_null, comment_null,	"a b c");
		toString_equals(values_a_b_c, ws_0, comment_null,		"a b c");
		toString_equals(values_a_b_c, ws_null, comment,			"a b c #c");
	}

	private void toString_equals(String[] values, String[] whitespaces, String comment, String expected) {
		Assert.equals(new WsvLine(values,whitespaces,comment).toString(), expected);
	}
	
	@Test
	public void hasValues() {
		hasValues(null, false);
		hasValues(stringArray(), false);
		hasValues(stringArray("Value"), true);
	}
	
	private void hasValues(String[] values, boolean expectedResult) {
		Assert.equals(new WsvLine(values).hasValues(), expectedResult);
	}
	
	@Test
	public void setValues() {
		WsvLine line = new WsvLine();
		line.setValues("Value1", "Value2");
		Assert.array_equals(line.Values, stringArray("Value1", "Value2"));
	}
	
	@Test
	public void getWhitespaces() {
		WsvLine line = WsvLine.parse("a  b  c #dfd");
		String[] clonedWhitespaces = line.getWhitespaces();
		Assert.array_equals(clonedWhitespaces, stringArray(null,"  ","  "," "));
		
		String[] internalWhitespaces = WsvBasedFormat.getWhitespaces(line);
		Assert.isNotEqual(clonedWhitespaces, internalWhitespaces);
	}
	
	@Test
	public void getComment() {
		Assert.equals(WsvLine.parse("a  b  c #comment").getComment(), "comment");
	}
	
	@Test
	public void parseAsArray() {
		Assert.array_equals(WsvLine.parseAsArray("a  b  c #comment"), stringArray("a", "b", "c"));
	}
	
	@Test
	public void getWhitespaces_Null() {
		WsvLine line = new WsvLine();
		Assert.array_equals(line.getWhitespaces(), null);
	}
	
	public static String[] stringArray(String... values) {
		return values;
	}
}
