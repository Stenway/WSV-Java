package com.stenway.wsv;

import org.junit.Test;

public class WsvParserExceptionTest {

	@Test
	public void getMessage() {
		Assert.equals(new WsvParserException(1, 2, 3, "Test").getMessage(), "Test (3, 4)");
	}
	
}