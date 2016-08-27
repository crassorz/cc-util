package cc.gu.util.debug;

public class MethodLight extends ExtendLight {
	
	final private String method;
	public MethodLight(Class<?> klass, String method) {
		super(klass);
		this.method = method;
	}
	
	@Override
	public boolean light(StackTraceElement[] elements, int level, Object self, Throwable e, String format,
			Object... args) {
		return super.light(elements, level, self, e, format, args) && method.equals(elements[0].getMethodName());
	}

}
