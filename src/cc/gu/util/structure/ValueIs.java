package cc.gu.util.structure;

import cc.gu.util.Util;

/**
 * Created by cc on 2016/7/11.
 */
abstract public class ValueIs {

    abstract public boolean is(Object obj);

    private static ValueIs NULL;
    private static ValueIs NOT_NULL;
    private static ValueIs IS_TRUE;
    private static ValueIs IS_FALSE;
    private static ValueIs IS_JSON_RPC;
    private static ValueIs HAS_RESULT;
    private static ValueIs HAS_ERROR;
    private static ValueIs IS_NUMBER;
    private static ValueIs EMPTY;
    private static ValueIs SINGLE;

    public static ValueIs nullable() {
        if (null == NULL) {
            NULL = new Null();
        }
        return NULL;
    }

    public static ValueIs notNull() {
        if (null == NOT_NULL) {
            NOT_NULL = new Not(nullable());
        }
        return NOT_NULL;
    }

    public static ValueIs isTrue() {
        if (null == IS_TRUE) {
        	IS_TRUE = new IsTrue();
        }
        return IS_TRUE;
    }

    public static ValueIs isFalse() {
        if (null == IS_FALSE) {
        	IS_FALSE = new IsFalse();
        }
        return IS_FALSE;
    }

    public static ValueIs isJsonRpc() {

        if (null == IS_JSON_RPC) {
            IS_JSON_RPC = new Or(hasResult(), hasError());
        }
        return IS_JSON_RPC;
    }

    public static ValueIs hasResult() {
        if (null == HAS_RESULT) {
            HAS_RESULT = new HasResult();
        }
        return HAS_RESULT;
    }

    public static ValueIs hasError() {
        if (null == HAS_ERROR) {
            HAS_ERROR = new HasError();
        }
        return HAS_ERROR;
    }

    public static ValueIs isNumber() {
        if (null == IS_NUMBER) {
            IS_NUMBER = new IsNumber();
        }
        return IS_NUMBER;
    }

    public static ValueIs isEmpty() {
        if (null == EMPTY) {
            EMPTY = size(0);
        }
        return EMPTY;
    }

    public static ValueIs isSingle() {
        if (null == SINGLE) {
            SINGLE = size(1);
        }
        return SINGLE;
    }

    public static ValueIs size(int size) {
        return new Size(size);
    }

    public static ValueIs has(Object... keys) {
        return new Has(keys);
    }

    public static ValueIs path(ValueIs is, Object... keys) {
        return new Path(is, keys);
    }

    public static ValueIs and(ValueIs... ises) {
        return new And(ises);
    }

    public static ValueIs or(ValueIs... ises) {
        return new Or(ises);
    }

    public static ValueIs not(ValueIs is) {
        return new Not(is);
    }

    public static ValueIs equal(Object o) {
        return new Equal(o);
    }

    public static <K> ValueIs isInstance(Class<K> klass) {
        return new IsInstance<K>(klass);
    }


    public static class Not extends ValueIs {
        final private ValueIs valueIs;

        public Not(ValueIs valueIs) {
            this.valueIs = valueIs;
        }

        public ValueIs getValueIs() {
            return valueIs;
        }

        @Override
        public boolean is(Object obj) {
            return !getValueIs().is(obj);
        }

        @Override
        public String toString() {
            return String.format("!%s", getValueIs());
        }
    }

    abstract public static class ValueIsList extends ValueIs {
        final private ValueIs[] valueIsList;

        public ValueIsList(ValueIs... valueIsList) {
            this.valueIsList = valueIsList;
        }

        public ValueIs[] getValueIsList() {
            return valueIsList;
        }

        protected String start() {
            return "(";
        }

        protected String space() {
            return ", ";
        }

        protected String end() {
            return ")";
        }

        protected String empty() {
            return "";
        }

        protected String single(ValueIs is) {
            return is.toString();
        }

        @Override
        public String toString() {
            ValueIs[] list = getValueIsList();
            switch (list.length) {
                case 0:
                    return empty();
                case 1:
                    return single(list[0]);
                default:
                    StringBuilder builder = new StringBuilder();
                    builder.append(start()).append(list[0]);
                    for (int i = 1; i < list.length; i++) {
                        builder.append(space()).append(list[i]);
                    }
                    builder.append(end());
                    return builder.toString();
            }
        }
    }

    public static class And extends ValueIsList {
        public And(ValueIs... valueIsList) {
            super(valueIsList);
        }

        @Override
        public boolean is(Object obj) {
            for (ValueIs valueIs : getValueIsList()) {
                if (!valueIs.is(obj)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        protected String empty() {
            return "true";
        }

        @Override
        protected String space() {
            return " && ";
        }
    }

    public static class Or extends ValueIsList {
        public Or(ValueIs... valueIsList) {
            super(valueIsList);
        }

        @Override
        public boolean is(Object obj) {
            for (ValueIs valueIs : getValueIsList()) {
                if (valueIs.is(obj)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        protected String empty() {
            return "false";
        }

        @Override
        protected String space() {
            return " || ";
        }
    }

    public static class Has extends ValueIs {
        final private Object[] keys;

        public Has(Object... keys) {
            this.keys = keys;
        }

        public Object[] getKeys() {
            return keys;
        }

        @Override
        public boolean is(Object obj) {
            return ValueOf.has(obj, getKeys());
        }

        protected String start() {
            return "has(";
        }

        protected String space() {
            return ".";
        }

        protected String end() {
            return ")";
        }

        protected String empty() {
            return "has()";
        }

        @Override
        public String toString() {
            Object[] keys = getKeys();
            if (keys.length == 0) {
                return empty();
            }

            StringBuilder builder = new StringBuilder();
            builder.append(start()).append(keys[0]);
            for (int i = 1; i < keys.length; i++) {
                builder.append(space()).append(keys[i]);
            }
            builder.append(end());
            return builder.toString();
        }
    }

    public static class Path extends Has {
        final private ValueIs valueIs;

        @Override
        protected String start() {
            return "";
        }

        @Override
        protected String end() {
            return String.format(".%s", getValueIs());
        }

        @Override
        protected String empty() {
            return getValueIs().toString();
        }

        public Path(ValueIs valueIs, Object... keys) {
            super(keys);
            this.valueIs = valueIs;
        }

        public ValueIs getValueIs() {
            return valueIs;
        }

        @Override
        public boolean is(Object obj) {
            return super.is(obj) && getValueIs().is(ValueOf.get(obj, getKeys()));
        }
    }

    abstract public static class Cast<T> extends ValueIs {
        final private Class<T> klass;

        public Cast(Class<T> klass) {
            this.klass = klass;
        }

        public Class<T> getKlass() {
            return klass;
        }

        @Override
        public boolean is(Object obj) {
            T t = ValueOf.get(null, getKlass(), obj);
            return instance(t);
        }

        @Override
        public String toString() {
            return String.format("%s.cast()", getKlass());
        }

        abstract public boolean instance(T obj);
    }

    public static class IsInstance<T> extends Cast<T> {
        public IsInstance(Class<T> klass) {
            super(klass);
        }

        @Override
        public boolean instance(T obj) {
            return obj != null;
        }

        @Override
        public String toString() {
            return String.format("%s.isInstance()", getKlass());
        }
    }

    public static class Equal extends Cast<Object> {
        private Object o;

        public Equal(Object o) {
            super(Object.class);
            this.o = o;
        }

        public Object getObj() {
            return o;
        }

        @Override
        public boolean instance(Object obj) {
            return Util.equal(o, obj);
        }

        @Override
        public String toString() {
            return String.format("equal(%s)", getObj());
        }

    }

    public static class Null extends Equal {
        public Null() {
            super(null);
        }
    }

    public static class IsTrue extends ValueIs {
        @Override
        public boolean is(Object obj) {
            return Value.getBool(obj, false);
        }

        @Override
        public String toString() {
            return "isTrue()";
        }
    }

    public static class IsFalse extends ValueIs {
        @Override
        public boolean is(Object obj) {
            return Value.getBool(obj, true);
        }

        @Override
        public String toString() {
            return "isFalse()";
        }
    }

    public static class IsNumber extends ValueIs {
        @Override
        public boolean is(Object obj) {
            return Value.isNumber(obj);
        }

        @Override
        public String toString() {
            return "isNumber()";
        }
    }

    public static class Size extends ValueIs {
        final private int size;

        public Size(int size) {
            this.size = size;
        }

        public int getSize() {
            return size;
        }

        @Override
        public boolean is(Object obj) {
            return sizeOf(ValueOf.size(obj));
        }

        @Override
        public String toString() {
            return "size == " + getSize();
        }

        public boolean sizeOf(int size) {
            return size == getSize();
        }
    }

    public static class HasResult extends And {
        public HasResult() {
            super(new Has("result"), new Path(isNumber(), "id"));
        }
    }

    public static class HasError extends Path {
        public HasError() {
            super(new Or(new Path(isNumber(), "code"), new Has("message")), "error");
        }
    }
}
