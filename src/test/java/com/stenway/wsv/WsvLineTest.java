package com.stenway.wsv;

import org.junit.Test;

public class WsvLineTest {

	@Test
	public void test_setComment_exceptions() {
		setComment_throws_exception("\n");
	}
	
	private void setComment_throws_exception(String comment) {
		try {
			WsvLine wsvLine = new WsvLine();
			wsvLine.setComment(comment);
		} catch (Exception e) {
			return;
		}
		throw new RuntimeException("Comment is valid");
	}
	
	@Test
	public void test_setWhitespaces_exceptions() {
		setWhitespaces_throws_exception("\n");
	}
	
	private void setWhitespaces_throws_exception(String... whitespaces) {
		try {
			WsvLine wsvLine = new WsvLine();
			wsvLine.setWhitespaces(whitespaces);
		} catch (Exception e) {
			return;
		}
		throw new RuntimeException("Whitespaces are valid");
	}
	
	@Test
	public void test_parse_exceptions() {
		parse_throws_exception("a b c\n",				"Multiple WSV lines not allowed (1, 6)");
		
		parse_throws_exception("a b c \"hello world",	"String not closed (1, 19)");
		parse_throws_exception("a b c \"hello world\n",	"String not closed (1, 19)");
		
		parse_throws_exception("a b\"hello world\"",	"Invalid double quote in value (1, 4)");
		
		parse_throws_exception("\"hello world\"a b c",	"Invalid character after string (1, 14)");
		
		parse_throws_exception("\"Line1\"/ \"Line2\"",	"Invalid string line break (1, 9)");
	}
	
	private void parse_throws_exception(String line, String expectedExceptionMessage) {
		try {
			WsvLine.parse(line);
		} catch (WsvParserException e) {
			Assert.equals(e.getMessage(), expectedExceptionMessage);
			return;
		}
		throw new RuntimeException("Line is valid");
	}
	
	@Test
	public void test_parse() {
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
	public void test_parse_nonPreserving() {
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
	public void test_toString_singleValue() {
		test_toString_singleValue("",				"\"\"");
		test_toString_singleValue("a",				"a");
		test_toString_singleValue("abc",			"abc");
		test_toString_singleValue("-",				"\"-\"");
		test_toString_singleValue(null,				"-");
		test_toString_singleValue("#",				"\"#\"");
		test_toString_singleValue("abc def",		"\"abc def\"");
		test_toString_singleValue("\"",				"\"\"\"\"");
		test_toString_singleValue("\n",				"\"\"/\"\"");
		test_toString_singleValue("Line1\nLine2",	"\"Line1\"/\"Line2\"");
	}
	
	private void test_toString_singleValue(String value, String expected) {
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
}
