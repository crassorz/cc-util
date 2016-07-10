package cc.gu.util.debug;

public interface Output {
	void output(StackTraceElement[] elements, int level, Object self, Throwable e, String src);
	void output(StackTraceElement[] elements, int level, Throwable e, String src);
	void output(StackTraceElement[] elements, int level, Object self, String src);
	void output(StackTraceElement[] elements, int level, Object self, Throwable e);
	void output(StackTraceElement[] elements, int level, Object self);
	void output(StackTraceElement[] elements, int level, Throwable e);
	void output(StackTraceElement[] elements, int level, String src);
	void output(StackTraceElement[] elements, int level);

}