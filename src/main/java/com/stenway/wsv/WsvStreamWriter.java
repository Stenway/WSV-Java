package com.stenway.wsv;

import com.stenway.reliabletxt.ReliableTxtEncoding;
import com.stenway.reliabletxt.ReliableTxtStreamWriter;
import java.io.IOException;

public class WsvStreamWriter implements AutoCloseable {
	ReliableTxtStreamWriter writer;
	StringBuilder sb;
	
	public final ReliableTxtEncoding Encoding;
	public final boolean AppendMode;
	
	public WsvStreamWriter(String filePath) throws IOException {
		this(filePath, null, false);
	}
	
	public WsvStreamWriter(String filePath, boolean append) throws IOException {
		this(filePath, null, append);
	}
	
	public WsvStreamWriter(String filePath, ReliableTxtEncoding encoding) throws IOException {
		this(filePath, encoding, false);
	}
	
	public WsvStreamWriter(String filePath, ReliableTxtEncoding encoding,
			boolean append) throws IOException {
		writer = new ReliableTxtStreamWriter(filePath, encoding, append);
		sb = new StringBuilder();
		
		Encoding = writer.Encoding;
		AppendMode = writer.AppendMode;
	}
	
	public void writeLine(String... values) throws IOException {
		writeLine(new WsvLine(values));
	}
	
	public void writeLine(String[] values, String[] whitespaces, String comment) throws IOException {
		writeLine(new WsvLine(values, whitespaces, comment));
	}
	
	public void writeLine(WsvLine line) throws IOException {
		sb.setLength(0);
		WsvSerializer.serializeLine(sb, line);
		String str = sb.toString();
		writer.writeLine(str);
	}
	
	public void writeLines(WsvLine... lines) throws IOException {
		for (WsvLine line:lines) {
			writeLine(line);
		}
	}
	
	public void writeLines(WsvDocument document) throws IOException {
		writeLines(document.Lines.toArray(new WsvLine[document.Lines.size()]));
	}

	@Override
	public void close() throws Exception {
		writer.close();
	}
}
