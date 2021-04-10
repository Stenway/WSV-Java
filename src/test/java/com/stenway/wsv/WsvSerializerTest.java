package com.stenway.wsv;

import org.junit.Test;

public class WsvSerializerTest {
	@Test
	public void test_serialize() {
		serialize_equals("  a  b  c  #c", "a b c");
	}
	
	private void serialize_equals(String content, String expectedResult) {
		Assert.equals(WsvSerializer.serializeDocument(WsvParser.parseDocumentAsJaggedArray(content)), expectedResult);
	}
}
