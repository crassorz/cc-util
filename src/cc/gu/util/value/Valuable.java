package cc.gu.util.value;

import cc.gu.util.Observable;
import cc.gu.util.Util;
import cc.gu.util.gather.Gatherer;
import cc.gu.util.occlude.Occluder;

public class Valuable<V> implements Gatherer<V>{
	
	private V value;
	private long  setTimed = Long.MIN_VALUE;
	
	private Observable<ValuableChangedListener<? super V>> handler = new Observable<ValuableChangedListener<? super V>>() {
		
		@Override
		protected void onObserver(ValuableChangedListener<? super V> listener) {
			listener.onValueChanged(Valuable.this);
		}
	};
	
	public void set(V value) {
		V old = this.value;
		this.value = value;
		setTimed = System.nanoTime();
		if (isChanged(value, old)) {
			handler.observer();
		}
	}
	
	public long getSetTimed() {
		return setTimed;
	}
	
	protected boolean isChanged(V value, V old) {
		return !Util.equal(value, old);
	}
	
	@Override
	public V get() {
		return value;
	}
	@Override
	public V get(Occluder occluder) {
		return get();
	}
	public void addOnValueChangeListener(ValuableChangedListener<? super V> listener) {
		handler.addObserver(listener);
	}
	public void addWeakOnValueChangeListener(ValuableChangedListener<? super V> listener) {
		handler.addWeakObserver(listener);
	}
	public void removeOnValueChangeListener(ValuableChangedListener<? super V> listener) {
		handler.removeObserver(listener);
	}
}
