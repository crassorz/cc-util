package cc.gu.util.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

abstract public class InstanceFactory<T> {
	final private static Map<Object, Map<Object, Object>> instancesMap = new HashMap<>();
	final private static Map<Object, Map<Object, Object>> keepsMap = new HashMap<>();
	final private Map<Object, Object> instances;
	final private Map<Object, Object> keeps;
	
	public InstanceFactory() {
		synchronized (InstanceFactory.class) {
			Map<Object, Object> instances = instancesMap.get(getClass());
			if (instances == null) {
				instances = new WeakHashMap<>(); 
				instancesMap.put(getClass(), instances);
			}
			this.instances = instances;
			Map<Object, Object> keeps = keepsMap.get(getClass());
			if (keeps == null) {
				keeps = new HashMap<>(); 
				keepsMap.put(getClass(), keeps);
			}
			this.keeps = keeps;
		}
	}
	
	private boolean weak = true;
	
	abstract public Object getInstanceKey();
	
	
	public InstanceFactory<T> setWeak(boolean weak) {
		this.weak = weak;
		return this;
	}
	
	public boolean isWeak() {
		return weak;
	}
	
	@SuppressWarnings("unchecked")
	final public T getInstance() {

		T provider;
		synchronized (getClass()) {
			Object key = getInstanceKey();
			if (key != null) {
				provider = (T) instances.get(key);
			} else {
				provider = null;
			}
			if (provider == null) {
				provider = (T) newInstance();
			}
			if (key != null) {
				instances.put(key, provider);
				if (isWeak()) {
					keeps.put(key, provider);
				} else {
					keeps.remove(key);
				}
			}	
		}
		return provider;
	}
	
	abstract protected T newInstance();
}
