package cc.gu.util.structure;

import java.util.Map.Entry;


public class BasicEntry<K, V> implements Entry<K, V> {

	public BasicEntry(K key) {
		this.key = key;
	}
	public BasicEntry(K key, V value) {
		this.key = key;
		this.value = value;
	}

	final private K key;
	private V value;
	
	@Override
	public K getKey() {
		return key;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public V setValue(V value) {
		return this.value = value;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (o instanceof Entry) {
			Object k = getKey();
			if (k == null) {
				Object ok = ((Entry<?,?>) o).getKey();
				if (ok == null) {
					return true;
				} else {
					return false;
				}
			} else {
				return k.equals(((Entry<?,?>) o).getKey());
			}
		} 
		return super.equals(o);
	}
	
	@Override
	public String toString() {
		return String.format("{%s=%s}", key, value);
	}
}
