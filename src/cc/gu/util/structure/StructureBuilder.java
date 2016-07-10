package cc.gu.util.structure;

import java.util.Collection;
import java.util.Map;

public class StructureBuilder {
	public static class MapBuilder<MAP extends Map<K, V>, K, V> {
		final private MAP map;
		
		private MapBuilder(MAP map) {
			this.map = map;
		}
		
		public MAP build() {
			return map;
		}

		public MapBuilder<MAP, K, V> put(K key, V value) {
			map.put(key, value);
			return this;
		}
		public MapBuilder<MAP, K, V> putAll(Map<? extends K, ? extends V> m) {
			map.putAll(m);
			return this;
		}
	}
	
	public static <MAP extends Map<K, V>, K, V> MapBuilder<MAP, K, V> build(MAP map) {
		return new MapBuilder<MAP, K, V>(map);
	}
	
	public static class CollectionBuilder<C extends Collection<V>, V> {
		final private C c;
		
		private CollectionBuilder(C c) {
			this.c = c;
		}
		
		public C build() {
			return c;
		}

		public CollectionBuilder<C, V> add(V value) {
			this.c.add(value);
			return this;
		}
		public CollectionBuilder<C, V> add(@SuppressWarnings("unchecked") V...values) {
			for (V value : values) {
				this.c.add(value);
			}
			return this;
		}
		public CollectionBuilder<C, V> addAll(Collection<? extends V> c) {
			this.c.addAll(c);
			return this;
		}
	}
	
	public static <C extends Collection<V>, V> CollectionBuilder<C, V> build(C c) {
		return new CollectionBuilder<C, V>(c);
	}
}
