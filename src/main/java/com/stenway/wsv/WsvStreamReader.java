package com.stenway.wsv;

import com.stenway.reliabletxt.ReliableTxtEncoding;
import com.stenway.reliabletxt.ReliableTxtStreamReader;
import java.io.IOException;

public class WsvStreamReader implements AutoCloseable {
	public final ReliableTxtEncoding Encoding;
	ReliableTxtStreamReader reader;
	
	public WsvStreamReader(String filePath) throws IOException {
		reader = new ReliableTxtStreamReader(filePath);
		Encoding = reader.Encoding;
	}
	
	public WsvLine readLine() throws IOException {
		String str = reader.readLine();
		if (str == null) {
			return null;
		}
		return WsvLine.parse(str);
	}

	@Override
	public void close() throws Exception {
		reader.close();
	}
}