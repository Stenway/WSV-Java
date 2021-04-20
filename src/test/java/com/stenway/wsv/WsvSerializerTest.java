package com.stenway.wsv;

import org.junit.Test;

public class WsvSerializerTest {
	@Test
	public void serializeDocument() {
		serializeDocument_JaggedArrayGiven("  a  b  c  #c", "a b c");
		serializeDocument_JaggedArrayGiven("  a  b  c  #c\n", "a b c\n");
		serializeDocument_JaggedArrayGiven("  a  b  c  #c\nd", "a b c\nd");
	}
	
	private void serializeDocument_JaggedArrayGiven(String content, String expectedResult) {
		Assert.equals(WsvSerializer.serializeDocument(WsvParser.parseDocumentAsJaggedArray(content)), expectedResult);
	}
	
	@Test
	public void serializeLine_StringsGiven() {
		Assert.equals(WsvSerializer.serializeLine("V11", "V12"), "V11 V12");
	}
	
	@Test
	public void serializeLine() {
		Assert.equals(WsvSerializer.serializeLine(new WsvLine(stringArray("V11", "V12", "V13"), stringArray(" "), "Comment")), " V11 V12 V13 #Comment");
		Assert.equals(WsvSerializer.serializeLine(new WsvLine(stringArray("V11", "V12", "V13"), stringArray(" "), null)), " V11 V12 V13");
		Assert.equals(WsvSerializer.serializeLine(new WsvLine(stringArray("V11", "V12"), stringArray(" "), "Comment")), " V11 V12 #Comment");
		Assert.equals(WsvSerializer.serializeLine(new WsvLine(stringArray("V11", "V12"), stringArray(" "), null)), " V11 V12");
		Assert.equals(WsvSerializer.serializeLine(new WsvLine(stringArray("V11", "V12"), stringArray(" ", " "), "Comment")), " V11 V12 #Comment");
		Assert.equals(WsvSerializer.serializeLine(new WsvLine(stringArray("V11", "V12"), stringArray(" ", " "), null)), " V11 V12");
		Assert.equals(WsvSerializer.serializeLine(new WsvLine(stringArray("V11", "V12"), stringArray(" ", " ", " "), "Comment")), " V11 V12 #Comment");
		Assert.equals(WsvSerializer.serializeLine(new WsvLine(stringArray("V11", "V12"), stringArray(" ", " ", " "), null)), " V11 V12 ");
		Assert.equals(WsvSerializer.serializeLine(new WsvLine(stringArray("V11", "V12"), stringArray(" ", " ", " ", " "), "Comment")), " V11 V12 #Comment");
		Assert.equals(WsvSerializer.serializeLine(new WsvLine(stringArray("V11", "V12"), stringArray(" ", " ", " ", " "), null)), " V11 V12 ");
		Assert.equals(WsvSerializer.serializeLine(new WsvLine(stringArray("V11", "V12"), stringArray(" ", " ", " ", " ", " "), "Comment")), " V11 V12 #Comment");
		Assert.equals(WsvSerializer.serializeLine(new WsvLine(stringArray("V11", "V12"), stringArray(" ", " ", " ", " ", " "), null)), " V11 V12 ");
		Assert.equals(WsvSerializer.serializeLine(new WsvLine(stringArray(), stringArray(" ", " ", " ", " ", " "), "Comment")), " #Comment");
		Assert.equals(WsvSerializer.serializeLine(new WsvLine(stringArray(), stringArray(" ", " ", " ", " ", " "), null)), " ");
		Assert.equals(WsvSerializer.serializeLine(new WsvLine(stringArray(), stringArray(), "Comment")), "#Comment");
		Assert.equals(WsvSerializer.serializeLine(new WsvLine(stringArray(), stringArray(), null)), "");
		
		Assert.equals(WsvSerializer.serializeLine(new WsvLine(stringArray("V11", "V12", "V13"), null, "Comment")), "V11 V12 V13 #Comment");
		Assert.equals(WsvSerializer.serializeLine(new WsvLine(stringArray("V11", "V12", "V13"), null, null)), "V11 V12 V13");
		Assert.equals(WsvSerializer.serializeLine(new WsvLine(stringArray("V11", "V12"), null, "Comment")), "V11 V12 #Comment");
		Assert.equals(WsvSerializer.serializeLine(new WsvLine(stringArray("V11", "V12"), null, null)), "V11 V12");
		Assert.equals(WsvSerializer.serializeLine(new WsvLine(stringArray(), null, "Comment")), "#Comment");
		Assert.equals(WsvSerializer.serializeLine(new WsvLine(stringArray(), null, null)), "");
	}
	
	public static String[] stringArray(String... values) {
		return values;
	}
	
	@Test
	public void staticClassCodeCoverageFix() {
		WsvSerializer wsvSerializer = new WsvSerializer();
	}
}
