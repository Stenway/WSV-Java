package com.stenway.wsv;

public class WsvSerializer {
	private static boolean containsSpecialChar(String value) {
		for (int i=0; i<value.length(); i++) {
			char c = value.charAt(i);
			if (c == '\n' || WsvChar.isWhitespace(c) || c == '"'
					 || c == '#') {
				return true;
			}
		}
		return false;
	}

	public static void serializeValue(StringBuilder sb, String value) {
		if (value==null) {
			sb.append('-');
		} else if (value.length() == 0) {
			sb.append("\"\"");
		} else if (value.equals("-")) {
			sb.append("\"-\"");
		} else if (containsSpecialChar(value)) {
			sb.append('"');
			for (int i=0; i<value.length(); i++) {
				char c = value.charAt(i);
				if (c == '\n') {
					sb.append("\"/\"");
				} else if(c == '"') {
					sb.append("\"\"");
				} else {
					sb.append(c);
				}
			}
			sb.append('"');
		} else {
			sb.append(value);
		}
	}
	
	private static void serializeWhitespace(StringBuilder sb, String whitespace,
			boolean isRequired) {
		if (whitespace != null && whitespace.length() > 0) {
			sb.append(whitespace);
		} else if (isRequired) {
			sb.append(" ");
		} 
	}

	private static void serializeValuesWithWhitespace(StringBuilder sb,
			WsvLine line) {
		if (line.Values == null) {
			String whitespace = line.whitespaces[0];
			serializeWhitespace(sb, whitespace, false);
			return;
		}
		
		for (int i=0; i<line.Values.length; i++) {
			String whitespace = null;
			if (i < line.whitespaces.length) {
				whitespace = line.whitespaces[i];
			}
			if (i == 0) {
				serializeWhitespace(sb, whitespace, false);
			} else {
				serializeWhitespace(sb, whitespace, true);
			}

			serializeValue(sb, line.Values[i]);
		}
		
		if (line.whitespaces.length >= line.Values.length + 1) {
			String whitespace = line.whitespaces[line.Values.length];
			serializeWhitespace(sb, whitespace, false);
		} else if (line.comment != null && line.Values.length > 0) {
			sb.append(' ');
		}
	}
	
	private static void serializeValuesWithoutWhitespace(StringBuilder sb, 
			WsvLine line) {
		if (line.Values == null) {
			return;
		}
		
		boolean isFollowingValue = false;
		for (String value : line.Values) {
			if (isFollowingValue) {
				sb.append(' ');
			} else {
				isFollowingValue = true;
			}
			serializeValue(sb, value);
		}

		if (line.comment != null && line.Values.length > 0) {
			sb.append(' ');
		}
	}
	
	public static void serializeLine(StringBuilder sb, WsvLine line) {
		if (line.whitespaces != null && line.whitespaces.length > 0) {
			serializeValuesWithWhitespace(sb, line);
		} else {
			serializeValuesWithoutWhitespace(sb, line);
		}
		
		if (line.comment != null) {
			sb.append('#');
			sb.append(line.comment);
		}
	}
	
	public static String serializeLine(WsvLine line) {
		StringBuilder sb = new StringBuilder();
		serializeLine(sb, line);
		return sb.toString();
	}
	
	public static String serializeDocument(WsvDocument document) {
		StringBuilder sb = new StringBuilder();
		boolean isFirstLine = true;
		for (WsvLine line : document.Lines) {
			if (!isFirstLine) {
				sb.append('\n');
			} else {
				isFirstLine = false;
			}
			serializeLine(sb, line);
		}
		return sb.toString();
	}
	
	public static String serializeLineNonPreserving(WsvLine line) {
		StringBuilder sb = new StringBuilder();
		serializeLine(sb, line.Values);
		return sb.toString();
	}
	
	public static String serializeDocumentNonPreserving(WsvDocument document) {
		StringBuilder sb = new StringBuilder();
		boolean isFirstLine = true;
		for (WsvLine line : document.Lines) {
			if (!isFirstLine) {
				sb.append('\n');
			} else {
				isFirstLine = false;
			}
			serializeLine(sb, line.Values);
		}
		return sb.toString();
	}
	
	public static void serializeLine(StringBuilder sb, String[] line) {
		boolean isFirstValue = true;
		for (String value : line) {
			if (!isFirstValue) {
				sb.append(' ');
			} else {
				isFirstValue = false;
			}
			serializeValue(sb, value);
		}
	}
	
	public static String serializeLine(String... line) {
		StringBuilder sb = new StringBuilder();
		serializeLine(sb, line);
		return sb.toString();
	}
	
	public static String serializeDocument(String[][] lines) {
		StringBuilder sb = new StringBuilder();
		boolean isFirstLine = true;
		for (String[] line : lines) {
			if (!isFirstLine) {
				sb.append('\n');
			} else {
				isFirstLine = false;
			}
			serializeLine(sb, line);
		}
		return sb.toString();
	}
}