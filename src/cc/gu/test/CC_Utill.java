package cc.gu.test;

import cc.gu.util.Util;
import cc.gu.util.debug.ClassLight;
import cc.gu.util.debug.Debug;
import cc.gu.util.debug.PrintStreamOutput;
import cc.gu.util.structure.Value;

public class CC_Utill {
	public static void main(String[] args) throws Throwable {
		Debug.setOutput(PrintStreamOutput.getInstance(System.out));
		Debug.addCurrentLight(new ClassLight(CC_Utill.class, true));
		Debug.addCurrentLight(new ClassLight(Util.class, true));
		Object[] numbers = {null, 
				1, -1, 1.1, -1.1,
				"1", "-1",
				"1l", "-1l",
				"0o7", "-0o7",
				"0xf", "-0xf",
				"1.1", "-1.1",
				"1e1", "-1e1", "1e-1", "-1e-1",
				"1.1e1", "-1.1e1", "1.1e-1", "-1.1e-1",
				"else"
		};
		for (Object number : numbers) {
			Debug.d("numbers: %s = %s", number, Value.getNumber(number, null));
		}
		Object[] bools = {
				true, false,
				"true", "false",
				"t", "f",
				"yes", "no",
				"y", "n",
		};
		for (Object bool : numbers) {
			Debug.d("bool: %s = %s", bool, Value.getBool(bool, null));
		}
		for (Object bool : bools) {
			Debug.d("bool: %s = %s", bool, Value.getBool(bool, null));
		}
	}
}
