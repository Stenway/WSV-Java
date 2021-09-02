package com.stenway.wsv;

import com.stenway.reliabletxt.ReliableTxtDocument;
import com.stenway.reliabletxt.ReliableTxtEncoding;
import java.io.IOException;
import org.junit.Test;

public class WsvDocumentTest {

	@Test
	public void parse() {
		parse("a  b  c  #comment\nd  e  f");
		parse("\" a \" \" b \" \" c \"#comment\n\" d \"\n\" e \"");
	}
	
	private void parse(String text) {
		Assert.equals(WsvDocument.parse(text).toString(), text);		
	}
	
	@Test
	public void parse_NonPreserving() {
		parse_NonPreserving("a  b  c  #comment\nd  e  f", "a b c\nd e f");
	}
	
	private void parse_NonPreserving(String text, String expected) {
		Assert.equals(WsvDocument.parse(text, false).toString(), expected);		
	}
	
	@Test
	public void parse_InvalidTextGiven_ShouldThrowException() {
		parse_InvalidTextGiven_ShouldThrowException("a b c \"hello world",	"String not closed (1, 19)");
		parse_InvalidTextGiven_ShouldThrowException("a b c \"hello world\n",	"String not closed (1, 19)");
		
		parse_InvalidTextGiven_ShouldThrowException("a b\"hello world\"",	"Invalid double quote after value (1, 4)");
		
		parse_InvalidTextGiven_ShouldThrowException("\"hello world\"a b c",	"Invalid character after string (1, 14)");
		
		parse_InvalidTextGiven_ShouldThrowException("\"Line1\"/ \"Line2\"",	"Invalid string line break (1, 9)");
		
		parse_InvalidTextGiven_ShouldThrowException("Line1\na b c \"hello world",	"String not closed (2, 19)");
		parse_InvalidTextGiven_ShouldThrowException("Line1\na b c \"hello world\n",	"String not closed (2, 19)");
		
		parse_InvalidTextGiven_ShouldThrowException("Line1\na b\"hello world\"",		"Invalid double quote after value (2, 4)");
		
		parse_InvalidTextGiven_ShouldThrowException("Line1\n\"hello world\"a b c",	"Invalid character after string (2, 14)");
		
		parse_InvalidTextGiven_ShouldThrowException("Line1\n\"Line1\"/ \"Line2\"",	"Invalid string line break (2, 9)");
	}
	
	private void parse_InvalidTextGiven_ShouldThrowException(String text, String expectedExceptionMessage) {
		try {
			WsvDocument.parse(text);
		} catch (WsvParserException e) {
			Assert.equals(e.getMessage(), expectedExceptionMessage);
			return;
		}
		throw new RuntimeException("Text is valid");
	}
	
	@Test
	public void getEncoding() {
		getEncoding(new WsvDocument(), ReliableTxtEncoding.UTF_8);
		getEncoding(new WsvDocument(ReliableTxtEncoding.UTF_16), ReliableTxtEncoding.UTF_16);
		getEncoding(new WsvDocument(ReliableTxtEncoding.UTF_16_REVERSE), ReliableTxtEncoding.UTF_16_REVERSE);
		getEncoding(new WsvDocument(ReliableTxtEncoding.UTF_32), ReliableTxtEncoding.UTF_32);
	}
	
	private void getEncoding(WsvDocument document, ReliableTxtEncoding expectedEncoding) {
		Assert.equals(document.getEncoding(), expectedEncoding);
	}
	
	@Test
	public void addLine() {
		WsvDocument document = new WsvDocument();
		document.addLine("V11", "V12");
		document.addLine(stringArray("V21", "V22"), stringArray("  ", "   ", ""), "comment");
		Assert.equals(document.toString(), "V11 V12\n  V21   V22#comment");
	}
	
	@Test
	public void getLine() {
		WsvDocument document = WsvDocument.parse("V11 V12\n  V21   V22#comment");
		WsvLine line = document.getLine(1);
		Assert.equals(line.toString(), "  V21   V22#comment");
	}
	
	@Test
	public void toArray() {
		WsvDocument document = WsvDocument.parse("V11 V12\n  V21   V22#comment");
		String[][] jaggedArray = document.toArray();
		Assert.array_equals(jaggedArray[0], stringArray("V11", "V12"));
		Assert.array_equals(jaggedArray[1], stringArray("V21", "V22"));
		Assert.equals(jaggedArray.length, 2);
	}
	
	@Test
	public void toString_NonPreserving() {
		WsvDocument document = WsvDocument.parse("V11 V12\n  V21   V22#comment");
		Assert.equals(document.toString(false), "V11 V12\nV21 V22");
	}
	
	@Test
	public void save() throws IOException {
		String text = "V11 V12\n  V21   V22#comment";
		save(text, ReliableTxtEncoding.UTF_8);
		save(text, ReliableTxtEncoding.UTF_16);
		save(text, ReliableTxtEncoding.UTF_16_REVERSE);
		save(text, ReliableTxtEncoding.UTF_32);
	}
	
	private void save(String text, ReliableTxtEncoding encoding) throws IOException {
		WsvDocument document = WsvDocument.parse(text);
		document.setEncoding(encoding);
		
		String filePath = "Test.wsv";
		document.save(filePath);
		load(filePath, text, encoding);
	}
	
	private void load(String filePath, String text, ReliableTxtEncoding encoding) {
		try {
			ReliableTxtDocument loaded = ReliableTxtDocument.load(filePath);
			Assert.equals(loaded.getEncoding(), encoding);
			Assert.equals(loaded.getText(), text);
		} catch (IOException ex) {
			throw new RuntimeException();
		}
	}
	
	@Test
	public void load() throws IOException {
		String text = "V11 V12\n  V21   V22#comment";
		load(text, ReliableTxtEncoding.UTF_8);
		load(text, ReliableTxtEncoding.UTF_16);
		load(text, ReliableTxtEncoding.UTF_16_REVERSE);
		load(text, ReliableTxtEncoding.UTF_32);
	}
	
	private void load(String text, ReliableTxtEncoding encoding) throws IOException {
		String filePath = "Test.wsv";
		ReliableTxtDocument.save(text, encoding, filePath);
		
		WsvDocument document = WsvDocument.load(filePath);
		Assert.equals(document.toString(), text);
		Assert.equals(document.getEncoding(), encoding);
	}
	
	@Test
	public void parseAsJaggedArray() {
		String[][] jaggedArray = WsvDocument.parseAsJaggedArray("V11 V12\n  V21   V22#comment");
		Assert.array_equals(jaggedArray[0], stringArray("V11", "V12"));
		Assert.array_equals(jaggedArray[1], stringArray("V21", "V22"));
		Assert.equals(jaggedArray.length, 2);
	}
	
	public static String[] stringArray(String... values) {
		return values;
	}
}
