package cc.gu.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class Util {

	final private static Collection<Equal> equals;
	final public static int EQUAL_NULLABLE = 0x0000000;
	final public static int EQUAL_EQUAL = 0x4000000;
	final public static int EQUAL_ARRAY = 0x8000000;
	final public static int EQUAL_DEFAULT = 0xc000000;
	static {
		equals = new HashSet<>();
		new Equal() {

			@Override
			protected Boolean equal(Object a, Object b) {
				if (a == b) return true;
				if (a == null || b == null) return false;
				return null;
			}

			@Override
			public int hashCode() {
				return EQUAL_NULLABLE;
			}
		};
		new Equal() {

			@Override
			protected Boolean equal(Object a, Object b) {
				return a.equals(b);
			}

			@Override
			public int hashCode() {
				return EQUAL_EQUAL;
			}
		};
		new Equal() {

			@Override
			protected Boolean equal(Object a, Object b) {
				if (a.getClass().isArray() && b.getClass().isArray()) {
					Object[] as = (Object[]) a;
					Object[] bs = (Object[]) b;
					if (as.length != bs.length) {
						return false;
					}
					for (int i = 0; i < bs.length; i++) {
						if (!Util.equal(as[i], bs[i])) {
							return false;
						}
					}
					return true;
				}
				return null;
			}

			@Override
			public int hashCode() {
				return EQUAL_ARRAY;
			}
		};
	}

	abstract public static class Equal {
		public Equal() {
			equals.add(this);
		}

		abstract protected Boolean equal(Object a, Object b);

		@Override
		public int hashCode() {
			return EQUAL_DEFAULT;
		}
	}

	public static boolean equal(Object a, Object b) {
		for (Equal equal : equals) {
			if (equal.equal(a, b)) {
				return true;
			}
		}
		return false;
	}
}
