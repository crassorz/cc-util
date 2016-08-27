package cc.gu.test;

import cc.gu.util.Util;
import cc.gu.util.debug.ClassLight;
import cc.gu.util.debug.Debug;
import cc.gu.util.debug.PrintStreamOutput;

public class CC_Utill {
	public static void main(String[] args) throws Throwable {
		Debug.setOutput(PrintStreamOutput.getInstance(System.out));
		Debug.addCurrentLight(new ClassLight(CC_Utill.class, true));
		Debug.addCurrentLight(new ClassLight(Util.class, true));
		Debug.d("%s", Util.getNumber("0o77", 3));
	}
}
