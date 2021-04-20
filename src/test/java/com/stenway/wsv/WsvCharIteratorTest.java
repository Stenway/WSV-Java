package com.stenway.wsv;

import org.junit.Test;

public class WsvCharIteratorTest {

	@Test
	public void readValue() {
		WsvCharIterator charIterator = new WsvCharIterator("Value1 Value2");
		Assert.equals(charIterator.readValue(), "Value1");
	}
	
	@Test
	public void readValue_EmptyTextGiven_ShouldThrowException() {
		try {
			WsvCharIterator charIterator = new WsvCharIterator("");
			charIterator.readValue();
		} catch (WsvParserException e) {
			Assert.equals(e.getMessage(), "Invalid value (1, 1)");
			return;
		}
		throw new RuntimeException("Text is valid");
	}
	
}
