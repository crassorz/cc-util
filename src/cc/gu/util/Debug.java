package cc.gu.util;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

abstract public class Debug {
	public interface Bool {
		default boolean bool(Object object) {
			return object != null && object != (Integer) 0 && object != (Boolean) false && object != "";
		}
	}
	
	
	public interface Light extends Bool{
		@Override
		default boolean bool(Object object) {
			if (!(object instanceof Object[])) {
				return false;
			}
			Object[] objects = (Object[]) object;
			if (objects.length == 0) { 
				return false;
			}
			if (!(objects[0] instanceof StackTraceElement[])) { 
				return false;
			}
			if (!(objects[1] instanceof Integer)) { 
				return false;
			}
			if (objects[3] != null && !(objects[3] instanceof Throwable)) { 
				return false;
			}
			if (objects[4] != null && !(objects[4] instanceof String)) { 
				return false;
			}
			if (objects[5] != null && !(objects[5] instanceof Object[])) { 
				return false;
			}
			return light((StackTraceElement[])objects[0],
					(Integer) objects[1], 
					objects[2], 
					(Throwable) objects[3], 
					(String) objects[4], 
					(Object[]) objects[5]);
		}
		boolean light(StackTraceElement[] elements, int level, Object self, Throwable e, String format,
				Object...args);
	}

	final private static Set<Light> currentLights = new LinkedHashSet<>();
	final private static Set<Light> banLights = new LinkedHashSet<>();

	synchronized public static void addCurrentLight(Light light) {
		Debug.currentLights.add(light);
	}
	
	synchronized public static void addBanLight(Light light) {
		Debug.banLights.add(light);
	}
	
	final private static String CLASS_NAME = Debug.class.getName();
	
	final public static int VERBOSE = 2;
	final public static int DEBUG = 3;
	final public static int INFO = 4;
	final public static int WARN = 5;
	final public static int ERROR = 6;
	final public static int  ALERT = 7;
	
	public interface Printer {
		void print(StackTraceElement[] elements, int level, Object self, Throwable e, String format, Object...args);
	}
	
	private static Printer printer;
	public static void setPrinter(Printer printer) {
		Debug.printer = printer;
	}
	public static Printer getPrinter() {
		return printer;
	}
	
	public static boolean isDebug() {
		return printer != null;
	}
	
	synchronized private static boolean isDebug(StackTraceElement[] elements, int level, Object self, Throwable e, String format,
			Object...args) {
		if (!isDebug()) {
			return false;
		}

		for (Light light: banLights) {
			if (light.light(elements, level, self, e, format, args)) {
				return false;
			}
		}
		for (Light light: currentLights) {
			if (light.light(elements, level, self, e, format, args)) {
				return true;
			}
		}
		return false;
	}
	
	private static StackTraceElement[] getStackTraceElements() {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		int i = 2;
		while (CLASS_NAME.equals(elements[++i].getClassName())) {
			
		}
		return Arrays.copyOfRange(elements, i, elements.length);
	}
	
	public static void println(int level, Object self, Throwable e, String format, Object...args) {
		if (!isDebug()) {
			return;
		}
		StackTraceElement[] elements = getStackTraceElements();
		if (!isDebug(elements,level, self, e, format, args)) {
			return;
		}
		getPrinter().print(elements, level, self, e, format, args);
	}
	
	public static void println(int level, Object self, Throwable e, String format) {
		println(level, null, e, format, (Object[]) null);
	}
	
	public static void println(int level, Throwable e, String format, Object...args) {	
		println(level, null, e, format, args);
	}
	
	public static void println(int level, String format, Object...args) {
		println(level, null, null, format, args);
	}
	
	public static void println(int level, Throwable e, String format) {	
		println(level, null, e, format);
	}
	
	public static void println(int level, String format) {
		println(level, null, null, format);
	}
	
	public static void println(int level, Throwable e) {
		println(level, null, e, null);
	}
	
	public static void v(Object self, Throwable e, String format, Object...args) {
		println(VERBOSE, self, e, format, args);
	}
	
	public static void v(Object self, Throwable e, String format) {	
		println(VERBOSE, null, e, format, (Object[]) null);
	}
	
	public static void v(Throwable e, String format, Object...args) {	
		println(VERBOSE, null, e, format, args);
	}
	
	public static void v(String format, Object...args) {
		println(VERBOSE, null, null, format, args);
	}
	
	public static void v(Throwable e, String format) {	
		println(VERBOSE, null, e, format);
	}
	
	public static void v(String format) {
		println(VERBOSE, null, null, format);
	}
	
	public static void v(Throwable e) {
		println(VERBOSE, null, e, null);
	}
	
	public static void d(Object self, Throwable e, String format, Object...args) {
		println(DEBUG, self, e, format, args);
	}
	
	public static void d(Object self, Throwable e, String format) {	
		println(DEBUG, null, e, format, (Object[]) null);
	}
	
	public static void d(Throwable e, String format, Object...args) {	
		println(DEBUG, null, e, format, args);
	}
	
	public static void d(String format, Object...args) {
		println(DEBUG, null, null, format, args);
	}
	
	public static void d(Throwable e, String format) {	
		println(DEBUG, null, e, format);
	}
	
	public static void d(String format) {
		println(DEBUG, null, null, format);
	}
	
	public static void d(Throwable e) {
		println(DEBUG, null, e, null);
	}

	
	public static void i(Object self, Throwable e, String format, Object...args) {
		println(INFO, self, e, format, args);
	}
	
	public static void i(Object self, Throwable e, String format) {	
		println(INFO, null, e, format, (Object[]) null);
	}
	
	public static void i(Throwable e, String format, Object...args) {	
		println(INFO, null, e, format, args);
	}
	
	public static void i(String format, Object...args) {
		println(INFO, null, null, format, args);
	}
	
	public static void i(Throwable e, String format) {	
		println(INFO, null, e, format);
	}
	
	public static void i(String format) {
		println(INFO, null, null, format);
	}
	
	public static void i(Throwable e) {
		println(INFO, null, e, null);
	}
	
	public static void w(Object self, Throwable e, String format, Object...args) {
		println(WARN, self, e, format, args);
	}
	
	public static void w(Object self, Throwable e, String format) {	
		println(WARN, null, e, format, (Object[]) null);
	}
	
	public static void w(Throwable e, String format, Object...args) {	
		println(WARN, null, e, format, args);
	}
	
	public static void w(String format, Object...args) {
		println(WARN, null, null, format, args);
	}
	
	public static void w(Throwable e, String format) {	
		println(WARN, null, e, format);
	}
	
	public static void w(String format) {
		println(WARN, null, null, format);
	}
	
	public static void w(Throwable e) {
		println(WARN, null, e, null);
	}

	
	public static void e(Object self, Throwable e, String format, Object...args) {
		println(ERROR, self, e, format, args);
	}
	
	public static void e(Object self, Throwable e, String format) {	
		println(ERROR, null, e, format, (Object[]) null);
	}
	
	public static void e(Throwable e, String format, Object...args) {	
		println(ERROR, null, e, format, args);
	}
	
	public static void e(String format, Object...args) {
		println(ERROR, null, null, format, args);
	}
	
	public static void e(Throwable e, String format) {	
		println(ERROR, null, e, format);
	}
	
	public static void e(String format) {
		println(ERROR, null, null, format);
	}
	
	public static void e(Throwable e) {
		println(ERROR, null, e, null);
	}
}
