package cc.gu.util.structure;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class Value {

	
	final public static Number ZERO = Integer.valueOf(0);
	final private static String FORMAT_BOOLEAN_TRUE = "(?i)^(true|yes|t|y)$";
	final private static String FORMAT_BOOLEAN_FALSE = "(?i)^(|false|no|f|n)$";
	final private static String FORMAT_INT = "^(-?[0-9]+)$";
	final private static String FORMAT_LONG = "(?i)^-?([0-9]+)l$";
	final private static String FORMAT_FLOAT = "(?i)^-?([0-9]+(\\.[0-9]+)?(e-?[0-9])?)$";
	final private static String FORMAT_X_0xX = "(?i)^-?0x([0-9a-f]+)$";
//	final private static String FORMAT_X_0xXp = "(?i)^0x([0-9a-f]+)(\\.[0-9a-f]+)?(p[0-9])$";
	final private static String FORMAT_X_Xh = "(?i)^-?([0-9][0-9a-f]*)h$";
//	final private static String FORMAT_X_Xph = "(?i)^([0-9][0-9a-f]*)(\\.[0-9a-f]+)?(p[0-9])h$";
	final private static String FORMAT_O_0oO = "(?i)^-?0o([0-7]+)$";
//	final private static String FORMAT_O_0oOp = "(?i)^0o([0-7]+)(\\.[0-7]+)?(p[0-9])$";

	public static boolean isNumber(Object obj) {
		return getNumber(obj, null) != null;
	}

	public static Number getNumber(Object v, Number defaultNumber) {
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
				if (string.startsWith("-")) {
					return - Long.parseLong(string.substring(3, string.length()), 16);
				} else {
					return Long.parseLong(string.substring(2, string.length()), 16);
				}
			if (string.matches(FORMAT_X_Xh))
				if (string.startsWith("-")) {
					return - Long.parseLong(string.substring(1, string.length() - 1), 16);
				} else {
					return Long.parseLong(string.substring(0, string.length()), 16);
				}
			if (string.matches(FORMAT_O_0oO))
				if (string.startsWith("-")) {
					return - Long.parseLong(string.substring(3, string.length()), 8);
				} else {
					return Long.parseLong(string.substring(2, string.length()), 8);
				}
			try {
				return Double.parseDouble(string);
			} catch (Exception e) {
				return defaultNumber;
			}
		}
	}


	public static Number getNumber(Object v) {
		return getNumber(v, 0);
	}
	public static Number getNumber(Object v, byte defaultNumber) {
		return getNumber(v, Byte.valueOf(defaultNumber));
	}

	public static Number getNumber(Object v, short defaultNumber) {
		return getNumber(v, Short.valueOf(defaultNumber));
	}

	public static Number getNumber(Object v, int defaultNumber) {
		return getNumber(v, Integer.valueOf(defaultNumber));
	}

	public static Number getNumber(Object v, long defaultNumber) {
		return getNumber(v, Long.valueOf(defaultNumber));
	}

	public static Number getNumber(Object v, float defaultNumber) {
		return getNumber(v, Float.valueOf(defaultNumber));
	}

	public static Number getNumber(Object v, double defaultNumber) {
		return getNumber(v, Double.valueOf(defaultNumber));
	}

	public static byte getByte(Object v) {
		return getByte(v, ZERO.byteValue());
	}
	public static Byte getByte(Object v, Byte defaultNumber) {
		Number number = getNumber(v, null);
		if (number == null) {
			return defaultNumber;
		} else {
			return number.byteValue();
		}
	}

	public static short getShort(Object v) {
		return getShort(v, ZERO.shortValue());
	}
	public static Short getShort(Object v, Short defaultNumber) {
		Number number = getNumber(v, null);
		if (number == null) {
			return defaultNumber;
		} else {
			return number.shortValue();
		}
	}

	public static int getInt(Object v) {
		return getInt(v, ZERO.intValue());
	}

	public static Integer getInt(Object v, Integer defaultNumber) {
		Number number = getNumber(v, null);
		if (number == null) {
			return defaultNumber;
		} else {
			return number.intValue();
		}
	}

	public static long getLong(Object v) {
		return getLong(v, ZERO.longValue());
	}

	public static Long getLong(Object v, Long defaultNumber) {
		Number number = getNumber(v, null);
		if (number == null) {
			return defaultNumber;
		} else {
			return number.longValue();
		}
	}


	public static float getFloat(Object v) {
		return getFloat(v, ZERO.floatValue());
	}

	public static Float getFloat(Object v, Float defaultNumber) {
		Number number = getNumber(v, null);
		if (number == null) {
			return defaultNumber;
		} else {
			return number.floatValue();
		}
	}
	public static double getDouable(Object v) {
		return getDouable(v, ZERO.doubleValue());
	}

	public static Double getDouable(Object v, Double defaultNumber) {
		Number number = getNumber(v, null);
		if (number == null) {
			return defaultNumber;
		} else {
			return number.doubleValue();
		}
	}
	
    public static String getString(Object obj) {
        return getString(obj, "");
    }
    
    public static String getString(Object obj, String opt) {
        String string;
        if (obj != null) {
            string = obj.toString();
        } else {
            string = null;
        }
        if (string == null) {
            string = opt;
        }
        return string;
    }
    
    public static boolean getBool(Object obj) {
    	return getBool(obj, false);
    }
    
    public static Boolean getBool(Object obj, Boolean opt) {
        if (obj == null) {
        	return opt;
        }
        if (obj instanceof Boolean) {
        	return (Boolean) obj;
        }
        if (obj instanceof Collection) {
        	return !((Collection<?>) obj).isEmpty();
        }
        if (obj instanceof Map) {
        	return !((Map<?,?>) obj).isEmpty();
        }
        Double number = Value.getDouable(obj, null);
        if (number != null) {
        	return 0 != number;
        }
        String string = getString(obj, null);
        if (string == null) {
        	return opt;
        }
        if (string.matches(FORMAT_BOOLEAN_TRUE)) {
        	return true;
        }
        if (string.matches(FORMAT_BOOLEAN_FALSE)) {
        	return false;
        }
        return opt;
    }
}
