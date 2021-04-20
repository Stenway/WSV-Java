package com.stenway.wsv;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;

public class WsvCharTest {

	private static final int[] whitespaceChars = new int[] {
			0x0009,
			0x000B,
			0x000C,
			0x000D,
			0x0020,
			0x0085,
			0x00A0,
			0x1680,
			0x2000,
			0x2001,
			0x2002,
			0x2003,
			0x2004,
			0x2005,
			0x2006,
			0x2007,
			0x2008,
			0x2009,
			0x200A,
			0x2028,
			0x2029,
			0x202F,
			0x205F,
			0x3000
	};
	
	@Test
	public void isWhitespace() {
		for (int wsChar : whitespaceChars) {
			Assert.isTrue(WsvChar.isWhitespace(wsChar));
		}
	}
	
	@Test
	public void isWhitespace_NonWhitespaceGiven_ShouldBeFalse() {
		List<Integer> wsList = Arrays.stream(whitespaceChars).boxed().collect(Collectors.toList());
		for (int c = 0; c <= 0x10FFFF; c++) {
			if (wsList.contains(c)) {
				continue;
			}
			Assert.isFalse(WsvChar.isWhitespace(c));
		}
	}
	
	@Test
	public void isWhitespace_LineFeedGiven_ShouldBeFalse() {
		Assert.isFalse(WsvChar.isWhitespace('\n'));
	}
	
	@Test
	public void getWhitespaceCodePoints() {
		Assert.array_equals(WsvChar.getWhitespaceCodePoints(), whitespaceChars);
	}
	
	@Test
	public void staticClassCodeCoverageFix() {
		WsvChar wsvChar = new WsvChar();
	}
}
