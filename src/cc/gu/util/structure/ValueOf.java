package cc.gu.util.structure;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ValueOf {
    private ValueOf() {
    }

    public static boolean has(Object obj, Object key) {
        if (obj == null) {
            return false;
        }
        else if (obj instanceof Map) {
            Map map = (Map) obj;
            return map.containsKey(key);
        } else if (key instanceof Integer) {
            int index = (Integer) key;
            if (index >= 0) {
                if (obj instanceof List) {
                    List list = (List) obj;
                    return list.size() > index;
                } else if (obj instanceof Object[]) {
                    Object[] array = (Object[]) obj;
                    return array.length > index;
                }
            }
        } else if (key instanceof String) {
            String keyString = (String) key;
            try {
                Field field = obj.getClass().getField(keyString);
                return true;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean has(Object obj, Object... keys) {
        for (Object key : keys) {
            if (!has(obj, key)) {
                return false;
            }
            obj = get(obj, key);
        }
        return true;
    }

    public static int size(Object obj, Object... keys) {
        obj = get(obj, keys);
        if (obj == null) {
            return 0;
        } else if (obj instanceof List) {
            List list = (List) obj;
            return list.size();
        } else if (obj instanceof Object[]) {
            Object[] array = (Object[]) obj;
            return array.length;
        } else {
            return 0;
        }
    }

    public static boolean isEmpty(Object obj, Object... keys) {
        return size(obj, keys) == 0;
    }

    public static Object get(Object obj, Object key) {
        if (obj == null || key == null) {
            return null;
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            if (map.containsKey(key)) {
                return map.get(key);
            } else {
                return null;
            }
        } else if (key instanceof Integer) {
            int index = (Integer) key;
            if (index < 0) {
                return null;
            } else if (obj instanceof List) {
                List list = (List) obj;
                if (list.size() > index) {
                    return list.get(index);
                } else {
                    return null;
                }
            } else if (obj instanceof Object[]) {
                Object[] array = (Object[]) obj;
                if (array.length > index) {
                    return array[index];
                } else {
                    return null;
                }
            } else if (key instanceof String) {
                String keyString = (String) key;
                try {
                    Field field = obj.getClass().getField(keyString);
                    return field.get(obj);
                } catch (NoSuchFieldException e) {
                } catch (IllegalAccessException e) {
                }
                return null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static Object get(Object obj, Object... keys) {
        for (Object key : keys) {
            if (!has(obj, key)) {
                return null;
            }
            obj = get(obj, key);
        }
        return obj;
    }

    private static <T> T opt(T opt, Class<T> klass, Object obj) {
        if (klass.isInstance(obj)) {
            return klass.cast(obj);
        }
        return opt;
    }

    public static <T> T get(T opt, Class<T> klass, Object obj, Object key) {
        if (!has(obj, key)) {
            return opt;
        }
        return opt(opt, klass, get(obj, key));
    }

    public static <T> T get(T opt, Class<T> klass, Object obj, Object... keys) {
        switch (keys.length) {
            case 0:
                return opt(opt, klass, obj);
            case 1:
                return get(opt, klass, obj, keys[0]);
            default:
                obj = get(obj, Arrays.copyOfRange(keys, 0, keys.length - 1));
                Object key = keys[keys.length - 1];
                return get(opt, klass, obj, key);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(T opt, Object obj, Object... keys) {
        return get(opt, (Class<T>) opt.getClass(), obj, keys);
    }

    public static Set getSet(Set opt, Object obj, Object... keys) {
        return get(opt, Set.class, obj, keys);
    }
    public static Set getSet(Object obj, Object... keys) {
        return get(Collections.emptySet(), Set.class, obj, keys);
    }

    public static List getList(List opt, Object obj, Object... keys) {
        return get(opt, List.class, obj, keys);
    }
    public static List getList(Object obj, Object... keys) {
        return get(Collections.emptyList(), List.class, obj, keys);
    }

    public static Map getMap(Map opt, Object obj, Object... keys) {
        return get(opt, Map.class, obj, keys);
    }
    public static Map getMap(Object obj, Object... keys) {
        return get(Collections.emptyMap(), Map.class, obj, keys);
    }
    
    public static String getString(Object obj, Object... keys) {
        return getString("", obj, keys);
    }
    public static String getString(String opt, Object obj, Object... keys) {
        if (!has(obj, keys)) {
            return opt;
        }
        return Value.getString(get(obj, keys), opt);
    }

    public static Byte getByte(Byte opt, Object obj, Object... keys) {
        return Value.getByte(get(obj, keys), opt);
    }
    public static byte getByte(Object obj, Object... keys) {
        return Value.getByte(get(obj, keys));
    }

    public static Short getShort(Short opt, Object obj, Object... keys) {
        return Value.getShort(get(obj, keys), opt);
    }
    public static short getShort(Object obj, Object... keys) {
        return Value.getShort(get(obj, keys));
    }

    public static Integer getInt(Integer opt, Object obj, Object... keys) {
        return Value.getInt(get(obj, keys), opt);
    }
    public static int getInt(Object obj, Object... keys) {
        return Value.getInt(get(obj, keys));
    }

    public static Long getLong(Long opt, Object obj, Object... keys) {
        return Value.getLong(get(obj, keys), opt);
    }
    public static long getLong(Object obj, Object... keys) {
        return Value.getLong(get(obj, keys));
    }

    public static Float getFloat(Float opt, Object obj, Object... keys) {
        return Value.getFloat(get(obj, keys), opt);
    }
    public static float getFloat(Object obj, Object... keys) {
        return Value.getFloat(get(obj, keys));
    }

    public static Double getDouable(Double opt, Object obj, Object... keys) {
        return Value.getDouable(get(obj, keys), opt);
    }
    public static double getDouable(Object obj, Object... keys) {
        return Value.getDouable(get(obj, keys));
    }

    public static boolean getBool(Object obj, Object... keys) {
        return getBool(false, obj, keys);
    }
    public static Boolean getBool(Boolean opt, Object obj, Object... keys) {
        if (!has(obj, keys)) {
            return opt;
        }
        return Value.getBool(get(obj, keys), opt);
    }
}
