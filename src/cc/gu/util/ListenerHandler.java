package cc.gu.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

abstract public class ListenerHandler <LISTENER>{
	final private Set<LISTENER> listeners = new HashSet<>();
	final private Map<LISTENER, Object> weakListeners = new WeakHashMap<>();
	private LinkedList<LISTENER> callback = new LinkedList<>();
	synchronized public void addListener(LISTENER listener) {
		removeListener(listener);
		listeners.add(listener);
	}
	synchronized public void addWeakListener(LISTENER listener) {
		removeListener(listener);
		weakListeners.put(listener, false);
	}
	synchronized public void removeListener(LISTENER listener) {
		listeners.remove(listener);
		weakListeners.remove(listener);
		synchronized (callback) {
			callback.remove(listener);	
		}
	}
	
	synchronized public void callback() {
		resetCallback();
		callbackAll();
	}
	
	protected void resetCallback() {
		synchronized (callback) {
			callback.clear();
			callback.addAll(listeners);
			callback.addAll(weakListeners.keySet());
		}
	}
	
	protected void callbackAll() {
		LISTENER listener;
		while (true) {
			synchronized (callback) {
				if (callback.isEmpty()) {
					return;
				}
				listener = callback.removeFirst();
				if (listener == null) {
					continue;
				}
				onCallback(listener);
			}
		}
	}
	abstract protected void onCallback(LISTENER listener);
}
