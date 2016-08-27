package cc.gu.util.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by cc on 2016/7/15.
 */
public class Build {


    public static <MAP extends Map<K, V>, K, V> MapBuilder<MAP, K, V> buildMap(MAP map) {
        return new MapBuilder<>(map);
    }

    public static <C extends Collection<E>, E> CollectionBuilder<C, E> buildCollection(C c) {
        return new CollectionBuilder<>(c);
    }

    public static <K, V> MapBuilder<Map<K, V>, K, V> buildMap(K k, V v) {
        return buildMap((Map<K, V>) new HashMap<K, V>()).put(k, v);
    }

    public static MapBuilder<Map<Object, Object>, Object, Object> buildMap() {
        return buildMap((Map<Object, Object>) new HashMap<Object, Object>());
    }

    public static <K, V> Map<K, V> singleton(K k, V v) {
        return buildMap(k, v).build();
    }

    public static <E> CollectionBuilder<Collection<E>, E> buildCollection(E e) {
        return buildCollection((Collection<E>) new ArrayList<E>()).add(e);
    }

    public static <E> Collection<E> singletonCollection(E e) {
        return buildCollection(e).build();
    }

    public static <E> CollectionBuilder<List<E>, E> buildList(E e) {
        return buildCollection((List<E>) new ArrayList<E>()).add(e);
    }

    public static CollectionBuilder<List<Object>, Object> buildList() {
        return buildCollection((List<Object>) new ArrayList<Object>());
    }

    public static <E> List<E> singletonList(E e) {
        return buildList(e).build();
    }

    public static <E> CollectionBuilder<Set<E>, E> buildSet(E e) {
        return buildCollection((Set<E>) new HashSet<E>()).add(e);
    }

    public static CollectionBuilder<Set<Object>, Object> buildSet() {
        return buildCollection((Set<Object>) new HashSet<Object>());
    }

    public static <E> Set<E> singletonSet(E e) {
        return buildSet(e).build();
    }

    private static class Builder<T> {


        final private T self;

        public T getSelf() {
            return self;
        }

        private Builder(T self) {
            this.self = self;
        }

        public T build() {
            return getSelf();
        }
    }


    public static class MapBuilder<MAP extends Map<K, V>, K, V> extends Builder<MAP> {

        private MapBuilder(MAP self) {
            super(self);
        }

        public MapBuilder<MAP, K, V> put(K k, V v) {
            getSelf().put(k, v);
            return this;
        }

        public MapBuilder<MAP, K, V> putAll(Map<? extends K, ? extends V> map) {
            getSelf().putAll(map);
            return this;
        }
    }

    public static class CollectionBuilder<C extends Collection<E>, E> extends Builder<C> {

        private CollectionBuilder(C self) {
            super(self);
        }

        public CollectionBuilder<C, E> add(E e) {
            getSelf().add(e);
            return this;
        }

        public CollectionBuilder<C, E> addAll(Collection<? extends E> es) {
            getSelf().addAll(es);
            return this;
        }
    }
}
