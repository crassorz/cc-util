package cc.gu.util.debug;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import cc.gu.util.structure.StructureBuilder;

public abstract class FormatOutput  implements Output {
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
	private Map<Integer, String> levels = StructureBuilder.build(new HashMap<Integer, String>())
			.put(Debug.VERBOSE, "V")
			.put(Debug.DEBUG, "D")
			.put(Debug.INFO, "I")
			.put(Debug.WARN, "W")
			.put(Debug.ERROR, "E")
			.put(Debug.ALERT, "ALERT")
			.build();
	
	
	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}
	
	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public String getNow() {
		return getDateFormat().format(System.currentTimeMillis());
	}
	
	public Map<Integer, String> getLevels() {
		return levels;
	}
	
	public void setLevels(Map<Integer, String> levels) {
		this.levels = levels;
	}
	
	public String getLevel(int level) {
		return getLevels().get(level);
	}
	
	public String getHeader(StackTraceElement[] elements, int level) {
		return String.format("%s %s/%s.%s(%s)", getNow(), getLevel(level), elements[0].getClassName(), elements[0].getMethodName(), elements[0].isNativeMethod() ? "native" : elements[0].getLineNumber());
	}
	
	abstract public void output(String header, String src, Throwable e);
	
	@Override
	public void output(StackTraceElement[] elements, int level, Object self, Throwable e, String src) {
		output(getHeader(elements, level), String.format("%s %s", self, src), e);
	}

	@Override
	public void output(StackTraceElement[] elements, int level, Throwable e, String src) {
		output(getHeader(elements, level), String.format("%s", src), e);
	}

	@Override
	public void output(StackTraceElement[] elements, int level, Object self, String src) {
		output(getHeader(elements, level), String.format("%s %s", self, src), null);
	}

	@Override
	public void output(StackTraceElement[] elements, int level, Object self, Throwable e) {
		output(getHeader(elements, level), String.format("%s", self), e);
	}

	@Override
	public void output(StackTraceElement[] elements, int level, Object self) {
		output(getHeader(elements, level), String.format("%s", self), null);
	}

	@Override
	public void output(StackTraceElement[] elements, int level, Throwable e) {
		output(getHeader(elements, level), null, e);
	}

	@Override
	public void output(StackTraceElement[] elements, int level, String src) {
		output(getHeader(elements, level), src, null);
	}

	@Override
	public void output(StackTraceElement[] elements, int level) {
		output(getHeader(elements, level), null, null);
	}
	
}