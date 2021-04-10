package com.stenway.wsv;

import org.junit.Test;

public class WsvStringTest {
	
	@Test
	public void test_isWhitespace() {
		int[] wsCodePoints = WsvChar.getWhitespaceCodePoints();
		Assert.isTrue(WsvString.isWhitespace(new String(wsCodePoints, 0, wsCodePoints.length)));
	}
	
	@Test
	public void test_isWhitespace_false() {
		Assert.isFalse(WsvString.isWhitespace(""));
		Assert.isFalse(WsvString.isWhitespace(" a "));
		Assert.isFalse(WsvString.isWhitespace("\n"));
	}
}
