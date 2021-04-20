package com.stenway.wsv;

import com.stenway.reliabletxt.ReliableTxtDocument;
import com.stenway.reliabletxt.ReliableTxtEncoding;
import org.junit.Test;

public class WsvStreamReaderTest {

	@Test
	public void readLine() throws Exception {
		readLine(ReliableTxtEncoding.UTF_8);
		readLine(ReliableTxtEncoding.UTF_16);
		readLine(ReliableTxtEncoding.UTF_16_REVERSE);
		readLine(ReliableTxtEncoding.UTF_32);
	}
	
	private void readLine(ReliableTxtEncoding encoding) throws Exception {
		String filePath = "Test.wsv";
		ReliableTxtDocument.save("V11 V12\nV21 V22\nV31 V32", encoding, filePath);
		try (WsvStreamReader reader = 
				new WsvStreamReader(filePath)) {
			Assert.equals(reader.Encoding, encoding);
			WsvLine line = null;
			int lineCount = 0;
			while ((line = reader.readLine()) != null) {
				lineCount++;
				Assert.equals(line.Values[0], "V"+lineCount+"1");
			}
			Assert.equals(lineCount, 3);
		}
	}
}
