package com.stenway.wsv;

import org.junit.Test;

public class WsvStringTest {
	
	@Test
	public void isWhitespace() {
		int[] wsCodePoints = WsvChar.getWhitespaceCodePoints();
		Assert.isTrue(WsvString.isWhitespace(new String(wsCodePoints, 0, wsCodePoints.length)));
	}
	
	@Test
	public void isWhitespace_NonWhitespaceGiven_ShouldBeFalse() {
		Assert.isFalse(WsvString.isWhitespace(null));
		Assert.isFalse(WsvString.isWhitespace(""));
		Assert.isFalse(WsvString.isWhitespace(" a "));
		Assert.isFalse(WsvString.isWhitespace("\n"));
	}
	
	@Test
	public void staticClassCodeCoverageFix() {
		WsvString wsvString = new WsvString();
	}	
}
