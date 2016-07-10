package cc.gu.util;

public class Number {
	final private static String FORMAT_INT = "^([0-9]+)$";
	final private static String FORMAT_LONG = "(?i)^([0-9]+)l$";
	final private static String FORMAT_FLOAT = "(?i)^([0-9]+\\.?([0-9])*(e[0-9])?)$";
	final private static String FORMAT_X_0xX = "(?i)^0x([0-9a-f]+)$";
//	final private static String FORMAT_X_0xXp = "(?i)^0x([0-9a-f]+)(\\.[0-9a-f]+)?(p[0-9])$";
	final private static String FORMAT_X_Xh = "(?i)^([0-9][0-9a-f]*)h$";
//	final private static String FORMAT_X_Xph = "(?i)^([0-9][0-9a-f]*)(\\.[0-9a-f]+)?(p[0-9])h$";
	final private static String FORMAT_O_0oO = "^0O([0-7]+)$";
//	final private static String FORMAT_O_0oOp = "(?i)^0O([0-7]+)(\\.[0-7]+)?(p[0-9])$";

	public static boolean isNumber(Object obj) {
		if (obj == null)
			return false;
		else if (obj instanceof java.lang.Number)
			return true;
		else if (obj instanceof String)
			return isNumber(toNumber((String) obj));
		return false;
	}

	private static Object toNumber(String string) {
		if (string == null)
			return null;
		if (string.matches(FORMAT_INT))
			return Long.parseLong(string);
		if (string.matches(FORMAT_LONG))
			return Long.parseLong(string.substring(0, string.length() - 1));
		if (string.matches(FORMAT_FLOAT))
			return Double.parseDouble(string);
		if (string.matches(FORMAT_X_0xX))
			return Long.parseLong(string.substring(2, string.length()), 16);
		if (string.matches(FORMAT_X_Xh))
			return Long.parseLong(string.substring(0, string.length() - 1), 16);
		if (string.matches(FORMAT_O_0oO))
			return Long.parseLong(string.substring(2, string.length()), 8);
		try {
			return Double.parseDouble(string);
		} catch (Exception e) {
			return null;
		}
	}

	public static Byte getNumber(Object v, Byte defaultNumber) {
		if (v == null)
			return defaultNumber;
		else if (v instanceof java.lang.Number)
			return ((java.lang.Number) v).byteValue();
		else if (v instanceof String)
			return getNumber(toNumber((String) v), defaultNumber);
		return defaultNumber;
	}

	public static Short getNumber(Object v, Short defaultNumber) {
		if (v == null)
			return defaultNumber;
		else if (v instanceof java.lang.Number)
			return ((java.lang.Number) v).shortValue();
		else if (v instanceof String)
			return getNumber(toNumber((String) v), defaultNumber);
		return defaultNumber;
	}

	public static Integer getNumber(Object v, Integer defaultNumber) {
		if (v == null)
			return defaultNumber;
		else if (v instanceof java.lang.Number)
			return ((java.lang.Number) v).intValue();
		else if (v instanceof String)
			return getNumber(toNumber((String) v), defaultNumber);
		return defaultNumber;
	}

	public static Long getNumber(Object v, Long defaultNumber) {
		if (v == null)
			return null;
		else if (v instanceof java.lang.Number)
			return ((java.lang.Number) v).longValue();
		else if (v instanceof String)
			return getNumber(toNumber((String) v), defaultNumber);
		return defaultNumber;
	}

	public static Float getNumber(Object v, Float defaultNumber) {
		if (v == null)
			return defaultNumber;
		else if (v instanceof java.lang.Number)
			return ((java.lang.Number) v).floatValue();
		else if (v instanceof String)
			return getNumber(toNumber((String) v), defaultNumber);
		return defaultNumber;
	}

	public static Double getNumber(Object v, Double defaultNumber) {
		if (v == null)
			return defaultNumber;
		else if (v instanceof java.lang.Number)
			return ((java.lang.Number) v).doubleValue();
		else if (v instanceof String)
			return getNumber(toNumber((String) v), defaultNumber);
		return defaultNumber;
	}
}
