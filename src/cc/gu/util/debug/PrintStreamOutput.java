package cc.gu.util.debug;

import java.io.PrintStream;
import java.util.Map;
import java.util.WeakHashMap;

public class PrintStreamOutput extends FormatOutput {
	
	private static Map<PrintStream, PrintStreamOutput> instances = new WeakHashMap<>();
	
	synchronized public static PrintStreamOutput getInstance(PrintStream printStream) {
		PrintStreamOutput instance = instances.get(printStream);
		if (instance == null) {
			instance = new PrintStreamOutput(printStream);
			instances.put(printStream, instance);
		}
		return instance;
	}
	
	public PrintStreamOutput(PrintStream printStream) {
		out = printStream;
	}
	
	private PrintStream out;

	@Override
	public synchronized void output(String header, String src, Throwable e) {
		out.printf(header);
		if (src != null) {
			out.printf(" ");
			if (e != null) {
				out.printf(src);
				out.printf(" ");
				e.printStackTrace(out);
			} else {
				out.println(src);
			}
		} else if (e != null) {
			out.printf(" ");
			e.printStackTrace(out);
		} else {
			out.println();
		}
	}
	
}