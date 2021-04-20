package com.stenway.wsv;

import com.stenway.reliabletxt.ReliableTxtDocument;
import com.stenway.reliabletxt.ReliableTxtEncoding;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;

public class WsvStreamWriterTest {

	@Test
	public void constructor() throws Exception {
		String filePath = "Test.wsv";
		try (WsvStreamWriter writer = new WsvStreamWriter(filePath)) {

		}
		try (WsvStreamWriter writer = new WsvStreamWriter(filePath, true)) {

		}
	}
	
	@Test
	public void constructor_append() throws IOException, Exception {
		constructor_append(ReliableTxtEncoding.UTF_8, ReliableTxtEncoding.UTF_8);
		constructor_append(ReliableTxtEncoding.UTF_8, ReliableTxtEncoding.UTF_16);
		constructor_append(ReliableTxtEncoding.UTF_8, ReliableTxtEncoding.UTF_16_REVERSE);
		constructor_append(ReliableTxtEncoding.UTF_8, ReliableTxtEncoding.UTF_32);
		
		constructor_append(ReliableTxtEncoding.UTF_16, ReliableTxtEncoding.UTF_8);
		constructor_append(ReliableTxtEncoding.UTF_16, ReliableTxtEncoding.UTF_16);
		constructor_append(ReliableTxtEncoding.UTF_16, ReliableTxtEncoding.UTF_16_REVERSE);
		constructor_append(ReliableTxtEncoding.UTF_16, ReliableTxtEncoding.UTF_32);
		
		constructor_append(ReliableTxtEncoding.UTF_16_REVERSE, ReliableTxtEncoding.UTF_8);
		constructor_append(ReliableTxtEncoding.UTF_16_REVERSE, ReliableTxtEncoding.UTF_16);
		constructor_append(ReliableTxtEncoding.UTF_16_REVERSE, ReliableTxtEncoding.UTF_16_REVERSE);
		constructor_append(ReliableTxtEncoding.UTF_16_REVERSE, ReliableTxtEncoding.UTF_32);
		
		constructor_append(ReliableTxtEncoding.UTF_32, ReliableTxtEncoding.UTF_8);
		constructor_append(ReliableTxtEncoding.UTF_32, ReliableTxtEncoding.UTF_16);
		constructor_append(ReliableTxtEncoding.UTF_32, ReliableTxtEncoding.UTF_16_REVERSE);
		constructor_append(ReliableTxtEncoding.UTF_32, ReliableTxtEncoding.UTF_32);
		
	}
	
	private void constructor_append(ReliableTxtEncoding firstEncoding, ReliableTxtEncoding secondEncoding) throws IOException, Exception {
		String filePath = "Append.wsv";
		deleteAppendFile(filePath);
		try (WsvStreamWriter writer = new WsvStreamWriter(filePath, firstEncoding, true)) {
			writer.writeLine("V11", "V12");
			Assert.equals(writer.Encoding, firstEncoding);
		}
		load(filePath, "V11 V12", firstEncoding);
		try (WsvStreamWriter writer = new WsvStreamWriter(filePath, secondEncoding, true)) {
			writer.writeLine("V21", "V22");
			Assert.equals(writer.Encoding, firstEncoding);
		}
		load(filePath, "V11 V12\nV21 V22", firstEncoding);
	}
	
	private void deleteAppendFile(String filePath) {
		try {
			Files.deleteIfExists(Paths.get(filePath));
		} catch (IOException ex) {
			throw new RuntimeException();
		}
	}
	
	@Test
	public void constructor_ZeroByteFileGiven() throws IOException, Exception {
		constructor_ZeroByteFileGiven(ReliableTxtEncoding.UTF_8);
		constructor_ZeroByteFileGiven(ReliableTxtEncoding.UTF_16);
		constructor_ZeroByteFileGiven(ReliableTxtEncoding.UTF_16_REVERSE);
		constructor_ZeroByteFileGiven(ReliableTxtEncoding.UTF_32);
	}
	
	private void constructor_ZeroByteFileGiven(ReliableTxtEncoding encoding) throws IOException, Exception {
		String filePath = "Append.wsv";
		deleteAppendFile(filePath);
		Files.write(Paths.get(filePath), new byte[] {});
		try (WsvStreamWriter writer = new WsvStreamWriter(filePath, encoding, true)) {
			writer.writeLine("V11", "V12");
		}
		load(filePath, "V11 V12", encoding);
	}
	
	@Test
	public void writeLine() throws Exception {
		writeLine(ReliableTxtEncoding.UTF_8);
		writeLine(ReliableTxtEncoding.UTF_16);
		writeLine(ReliableTxtEncoding.UTF_16_REVERSE);
		writeLine(ReliableTxtEncoding.UTF_32);
	}
	
	private void writeLine(ReliableTxtEncoding encoding) throws Exception {
		String filePath = "Test.wsv";
		try (WsvStreamWriter writer = 
			new WsvStreamWriter(filePath, encoding)) {
			for (int i=1; i<=3; i++) {
				writer.writeLine("V"+i+"1");
			}
		}
		load(filePath, "V11\nV21\nV31", encoding);
	}
	
	@Test
	public void writeLine_WhitespaceAndCommentsGiven() throws Exception {
		writeLine_WhitespaceAndCommentsGiven(ReliableTxtEncoding.UTF_8);
		writeLine_WhitespaceAndCommentsGiven(ReliableTxtEncoding.UTF_16);
		writeLine_WhitespaceAndCommentsGiven(ReliableTxtEncoding.UTF_16_REVERSE);
		writeLine_WhitespaceAndCommentsGiven(ReliableTxtEncoding.UTF_32);
	}
	
	private void writeLine_WhitespaceAndCommentsGiven(ReliableTxtEncoding encoding) throws Exception {
		String filePath = "Test.wsv";
		try (WsvStreamWriter writer = 
			new WsvStreamWriter(filePath, encoding)) {
			for (int i=1; i<=3; i++) {
				writer.writeLine(stringArray("V"+i+"1"), stringArray("  ", "   "), "comment");
			}
		}
		load(filePath, "  V11   #comment\n  V21   #comment\n  V31   #comment", encoding);
	}
	
	@Test
	public void writeLines() throws Exception {
		String filePath = "Test.wsv";
		try (WsvStreamWriter writer = 
			new WsvStreamWriter(filePath)) {
			writer.writeLines(WsvLine.parse("V11 V12"), WsvLine.parse("V21 V22"), WsvLine.parse("V31 V32"));
		}
		load(filePath, "V11 V12\nV21 V22\nV31 V32", ReliableTxtEncoding.UTF_8);
	}
	
	@Test
	public void writeLines_DocumentGiven() throws Exception {
		String filePath = "Test.wsv";
		try (WsvStreamWriter writer = 
			new WsvStreamWriter(filePath)) {
			WsvDocument doc = WsvDocument.parse("V11 V12  #Comment\nV21 V22\nV31 V32");
			writer.writeLines(doc);
		}
		load(filePath, "V11 V12  #Comment\nV21 V22\nV31 V32", ReliableTxtEncoding.UTF_8);
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
	
	public static String[] stringArray(String... values) {
		return values;
	}
}

