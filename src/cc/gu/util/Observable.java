package cc.gu.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

abstract public class Observable <OBSERVER> {
	final private Set<OBSERVER> observers = new HashSet<>();
	final private Map<OBSERVER, Object> weakObservers = new WeakHashMap<>();
	private LinkedList<OBSERVER> observering = new LinkedList<>();
	
	synchronized public void addObserver(OBSERVER observer) {
		weakObservers.remove(observer);
		observers.add(observer);
	}
	synchronized public void addWeakObserver(OBSERVER observer) {
		observers.remove(observer);
		weakObservers.put(observer, false);
	}
	synchronized public void removeObserver(OBSERVER observer) {
		observers.remove(observer);
		weakObservers.remove(observer);
		synchronized (observering) {
			observering.remove(observer);	
		}
	}
	synchronized public void observer() {
		resetObserver();
		onObserver();
	}
	
	protected void resetObserver() {
		synchronized (observering) {
			observering.clear();
			observering.addAll(observers);
			observering.addAll(weakObservers.keySet());
		}
	}
	
	private void onObserver() {
		OBSERVER observer;
		while (true) {
			synchronized (observering) {
				if (observering.isEmpty()) {
					return;
				}
				observer = observering.removeFirst();
				if (observer == null) {
					continue;
				}
				onObserver(observer);
			}
		}
	}
	abstract protected void onObserver(OBSERVER observer);
}
