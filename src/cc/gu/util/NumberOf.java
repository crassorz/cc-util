package cc.gu.util;

import java.util.Objects;

public class NumberOf {
	final private static String FORMAT_INT = "^([0-9]+)$";
	final private static String FORMAT_LONG = "(?i)^([0-9]+)l$";
	final private static String FORMAT_FLOAT = "(?i)^([0-9]+\\.?([0-9])*(e[0-9])?)$";
	final private static String FORMAT_X_0xX = "(?i)^0x([0-9a-f]+)$";
//	final private static String FORMAT_X_0xXp = "(?i)^0x([0-9a-f]+)(\\.[0-9a-f]+)?(p[0-9])$";
	final private static String FORMAT_X_Xh = "(?i)^([0-9][0-9a-f]*)h$";
//	final private static String FORMAT_X_Xph = "(?i)^([0-9][0-9a-f]*)(\\.[0-9a-f]+)?(p[0-9])h$";
	final private static String FORMAT_O_0oO = "^(?i)0O([0-7]+)$";
//	final private static String FORMAT_O_0oOp = "(?i)^0O([0-7]+)(\\.[0-7]+)?(p[0-9])$";

	public static boolean isNumber(Object obj) {
		return toNumber(obj, null) != null;
	}

	public static Number toNumber(Object v, Number defaultNumber) {
		if (v == null)
			return defaultNumber;
		else if (v instanceof Number)
				return (Number) v;
		else {
			String string = Objects.toString(v);
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
				return defaultNumber;
			}
		}
	}

	public static Byte getNumber(Object v, Byte defaultNumber) {
		Number number = toNumber(v, defaultNumber);
		if (number == null) {
			return null;
		} else {
			return number.byteValue();
		}
	}

	public static Short getNumber(Object v, Short defaultNumber) {
		Number number = toNumber(v, defaultNumber);
		if (number == null) {
			return null;
		} else {
			return number.shortValue();
		}
	}

	public static Integer getNumber(Object v, Integer defaultNumber) {
		Number number = toNumber(v, defaultNumber);
		if (number == null) {
			return null;
		} else {
			return number.intValue();
		}
	}

	public static Long getNumber(Object v, Long defaultNumber) {
		Number number = toNumber(v, defaultNumber);
		if (number == null) {
			return null;
		} else {
			return number.longValue();
		}
	}

	public static Float getNumber(Object v, Float defaultNumber) {
		Number number = toNumber(v, defaultNumber);
		if (number == null) {
			return null;
		} else {
			return number.floatValue();
		}
	}

	public static Double getNumber(Object v, Double defaultNumber) {
		Number number = toNumber(v, defaultNumber);
		if (number == null) {
			return null;
		} else {
			return number.doubleValue();
		}
	}
}
