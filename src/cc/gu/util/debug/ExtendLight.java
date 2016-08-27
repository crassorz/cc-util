package cc.gu.util.debug;

import cc.gu.util.debug.Debug.Light;

public class ExtendLight implements Light {
	
	final private Class<?> klass;
	public ExtendLight(Class<?> klass) {
		this.klass = klass;
	}

	@Override
	public boolean light(StackTraceElement[] elements, int level, Object self, Throwable e, String format,
			Object... args) {
		Class<?> elementClass;
		try {
			elementClass = Class.forName(elements[0].getClassName());
		} catch (ClassNotFoundException e1) {
			return false;
		}
		return klass.isAssignableFrom(elementClass);
	}

}
