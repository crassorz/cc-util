package cc.gu.util.debug;

import java.io.PrintStream;
import java.util.Map;

public class PrintStreamOutput extends FormatOutput {
	
	private static Map<PrintStream, PrintStreamOutput> instances;
	
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
	public void output(String header, String src, Throwable e) {
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