package cc.gu.util.debug;

import cc.gu.util.debug.Debug.Light;

public class ClassLight implements Light {
	
	final private String klass;
	public ClassLight(Class klass, boolean inner) {
		String name = klass.getName().replaceAll(".", "\\.").replaceAll("$", "\\$");
		if (inner) {
			this.klass = String.format("^(%s.*)$", name);
		} else {
			this.klass = String.format("^(%s)$", name);
		}
	}

	@Override
	public boolean light(StackTraceElement[] elements, int level, Object self, Throwable e, String format,
			Object... args) {
		return elements[0].getClassName().matches(klass);
	}

}
