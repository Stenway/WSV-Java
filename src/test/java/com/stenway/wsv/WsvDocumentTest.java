package com.stenway.wsv;

import org.junit.Test;

public class WsvDocumentTest {

	@Test
	public void test_parse_exceptions() {
		parse_throws_exception("a b c \"hello world",	"String not closed (1, 19)");
		parse_throws_exception("a b c \"hello world\n",	"String not closed (1, 19)");
		
		parse_throws_exception("a b\"hello world\"",	"Invalid double quote in value (1, 4)");
		
		parse_throws_exception("\"hello world\"a b c",	"Invalid character after string (1, 14)");
		
		parse_throws_exception("\"Line1\"/ \"Line2\"",	"Invalid string line break (1, 9)");
		
		parse_throws_exception("Line1\na b c \"hello world",	"String not closed (2, 19)");
		parse_throws_exception("Line1\na b c \"hello world\n",	"String not closed (2, 19)");
		
		parse_throws_exception("Line1\na b\"hello world\"",		"Invalid double quote in value (2, 4)");
		
		parse_throws_exception("Line1\n\"hello world\"a b c",	"Invalid character after string (2, 14)");
		
		parse_throws_exception("Line1\n\"Line1\"/ \"Line2\"",	"Invalid string line break (2, 9)");
	}
	
	private void parse_throws_exception(String text, String expectedExceptionMessage) {
		try {
			WsvDocument.parse(text);
		} catch (WsvParserException e) {
			Assert.equals(e.getMessage(), expectedExceptionMessage);
			return;
		}
		throw new RuntimeException("Text is valid");
	}
}
