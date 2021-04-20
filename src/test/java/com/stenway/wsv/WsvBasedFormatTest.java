package com.stenway.wsv;

import org.junit.Test;

public class WsvBasedFormatTest {
	
	@Test
	public void getWhitespaces() {
		WsvLine line = WsvLine.parse("a  b  c #dfd");
		String[] whitespaces = WsvBasedFormat.getWhitespaces(line);
		Assert.array_equals(whitespaces, new String[] {null,"  ","  "," "});
	}
	
	@Test
	public void staticClassCodeCoverageFix() {
		WsvBasedFormat wsvBasedFormat = new WsvBasedFormat();
	}
}
