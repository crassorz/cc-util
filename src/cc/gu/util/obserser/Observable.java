package cc.gu.util.obserser;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;

import cc.gu.util.structure.Build;

abstract public class Observable <OBSERVER, T>{
	final private Set<OBSERVER> keep = new HashSet<>();
	final private Map<OBSERVER, Object> observers = new WeakHashMap<>();
	final private LinkedList<Entry<OBSERVER, T>> observering = new LinkedList<>();
	
	synchronized public void addObserver(OBSERVER observer, boolean weak) {
		if (weak) {
			keep.remove(observer);
		} else {
			keep.add(observer);
		}
		observers.put(observer, false);
	}
	
	synchronized public void removeObserver(OBSERVER observer) {
		synchronized (observering) {
			observers.remove(observer);
			keep.remove(observer);
			for (int i = observering.size() - 1; i >= 0; i--) {
				if (observering.get(i).getKey() == observer) {
					observering.remove(i);
				}
			}
			
		}
	}
	public void stopObserver() {
		synchronized (observering) {
			observering.clear();
		}
	}

	public void observer() {
		observer(null);
	}
	
	public void observer(T t) {
		synchronized (observering) {
			for (OBSERVER observer : observers.keySet()) {
				if (observer != null) {
					observering.add(Build.entry(observer, t));
				}
			}
		}
		Entry<OBSERVER, T> observer;
		while (true) {
			synchronized (observering) {
				if (observering.isEmpty()) {
					return;
				}
				observer = observering.removeFirst();
				onObserver(observer.getKey(), observer.getValue());
			}
		}
	}
	abstract public void onObserver(OBSERVER observer, T t);
}
