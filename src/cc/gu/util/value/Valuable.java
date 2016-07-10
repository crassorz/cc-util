package cc.gu.util.value;

import java.util.Map;
import java.util.WeakHashMap;

import cc.gu.util.ListenerHandler;
import cc.gu.util.Util;
import cc.gu.util.gather.Gatherer;
import cc.gu.util.occlude.Occluder;

public class Valuable<V> implements Gatherer<V>{
	
	private V value;
	private long  setTimed = Long.MIN_VALUE;
	
	private ListenerHandler<ListenerProxy<?, V>> handler = new ListenerHandler<ListenerProxy<?, V>>() {
		
		@Override
		protected void onCallback(ListenerProxy<?, V> proxy) {
			proxy.onCallback(get());
		}
	};
	
	public void set(V value) {
		this.value = value;
		setTimed = System.nanoTime();
		handler.callback();
	}
	
	public long getSetTimed() {
		return setTimed;
	}
	
	protected boolean hasChanged(V value, V old) {
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
	private static class ListenerProxy<LV, V extends LV> {
		final OnValueSetListener<LV> listener;
		final Valuable<V> valuable;
		
		ListenerProxy(Valuable<V> valuable, OnValueSetListener<LV> listener) {
			this.valuable = valuable;
			this.listener = listener;
		}
		
		void onCallback(V value) {
			listener.onValueSeted(valuable, value);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ListenerProxy) {
				return listener.equals(((ListenerProxy<?, ?>)obj).listener);
			}
			return false;
		}
	} 
	
	private Map<OnValueSetListener<?>, ListenerProxy<?, V>> callbacks = new WeakHashMap<>();

	private static <LV, V extends LV> ListenerProxy<LV, V> proxy(Valuable<V> valuable, OnValueSetListener<LV> listener) {
		return new ListenerProxy<>(valuable, listener);
	}
	public static <V, VAIUABLE extends V> void addOnValueChangeListener(Valuable<VAIUABLE> valuable, OnValueSetListener<V> listener) {
		valuable.addOnValueChangeListener(proxy(valuable, listener));
	}
	public static <V, VAIUABLE extends V> void addWeakOnValueChangeListener(Valuable<VAIUABLE> valuable, OnValueSetListener<V> listener) {
		valuable.addWeakOnValueChangeListener(proxy(valuable, listener));
	}
	public static <V, VAIUABLE extends V> void removeOnValueChangeListener(Valuable<VAIUABLE> valuable, OnValueSetListener<V> listener) {
		valuable.removeOnValueChangeListener(proxy(valuable, listener));
	}
	
	private void addOnValueChangeListener(ListenerProxy<?, V> proxy) {
		callbacks.put(proxy.listener, proxy);
		handler.addListener(proxy);
	}
	
	private void addWeakOnValueChangeListener(ListenerProxy<?, V> proxy) {
		callbacks.put(proxy.listener, proxy);
		handler.addWeakListener(proxy);
	}
	
	private void removeOnValueChangeListener(ListenerProxy<?, V> proxy) {
		callbacks.remove(proxy.listener, proxy);
		handler.removeListener(proxy);
	}
}
