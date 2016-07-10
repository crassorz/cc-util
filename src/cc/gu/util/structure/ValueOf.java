package cc.gu.util.structure;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ValueOf {
	private ValueOf() {}
	
	/**
	 * 
	 * @param missNull miss value is null
	 * @param object your target object
	 * @param keys null will get all;
	 * Map support any(key);
	 * List support int(index);
	 * else classes support String(field name)
	 * @return
	 */
	synchronized public static <T> List<T> valueOf(boolean missNull, Object object, Object...keys) {
		List<T> ret = new ArrayList<T>();
		getAll(missNull, ret, object, keys);
		return ret;
	}
	private static <T> void add(boolean missNull, List<T> list, Object object) {
		if (missNull && object == null) return;
		try {
			@SuppressWarnings("unchecked")
			T t = (T) object;
			list.add(t);
		} catch (Exception e) {
		}
	}
	
	private static <T> void getAll(boolean missNull, List<T> ret, Object object, Object...keys) {
		if (keys.length == 0) {
			add(missNull, ret, object);
			return;
		} else if (object == null) {
			return;
		}
		Object key = keys[0];
		keys = Arrays.copyOfRange(keys, 1, keys.length);
		if (object instanceof Map) {
			findAll(missNull, ret, (Map<?, ?>) object, key, keys);
		} else if (object instanceof List) {
			findAll(missNull, ret, (List<?>) object, key, keys);
		} else {
			findAll(missNull, ret, object, key, keys);
		}
	}

	private static <T> void findAll(boolean missNull, List<T> ret, Map<?, ?> map, Object key, Object...keys) {
		if (key == null) {
			for (Object object : map.values()) {
				getAll(missNull, ret, object, keys);
			}
		} else if (map.containsKey(key)) {
			getAll(missNull, ret, map.get(key), keys);
		}
	}
	private static <T> void findAll(boolean missNull, List<T> ret, List<?> list, Object key, Object...keys) {
		if (key == null) {
			for (Object object : list) {
				getAll(missNull, ret, object, keys);
			}
		} else if (key instanceof Integer) {
			int index = (Integer) key;
			if (list.size() < index) {
				getAll(missNull, ret, list.get(index), keys);
			}
		}
	}
	
	private static <T> void findAll(boolean missNull, List<T> ret, Object object, Object key, Object...keys) {
		if (key == null) {
			for (Field f : object.getClass().getFields()) {
				try {
					if (!Modifier.isStatic(f.getModifiers())) {
						getAll(missNull, ret, f.get(object), keys);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		} else if (key instanceof String) {
			Field f;
			try {
				f = object.getClass().getField((String) key);
				if (!Modifier.isStatic(f.getModifiers())) {
					getAll(missNull, ret, f.get(object), keys);
				}
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
}
