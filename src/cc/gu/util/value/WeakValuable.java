package cc.gu.util.value;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import cc.gu.util.occlude.OccludedException;
import cc.gu.util.occlude.Occluder;

abstract public class WeakValuable<T> extends SingleThreadValuable<T> {

	public WeakValuable(Valuable<T> gearing) {
		super(gearing);
	}

	final private Reference<T> CHANGED = new WeakReference<T>(null);
	private volatile Reference<T> value = null;
	
	@Override
	protected T setToGearing(T value) {
		value = super.setToGearing(value);
		this.value = (value == null) ? null : new WeakReference<>(value);
		return value;
	}
	
	@Override
	protected void onGearingValueChanged() {
		value = CHANGED;
		super.onGearingValueChanged();
	}

	protected T getWeak() throws NullPointerException {
		if (value == CHANGED) {
			throw new NullPointerException();
		}
		if (value == null) {
			return null;
		} else {
			T v = value.get();
			if (v == null) {
				throw new NullPointerException();
			}
			return v;
		}
	}
	@Override
	public T get() throws Throwable {
		try {
			return getWeak();
		} catch (NullPointerException e) {
			return super.get();
		}
	}
	
	@Override
	public T get(Occluder occluder) throws OccludedException, Throwable {
		try {
			return getWeak();
		} catch (NullPointerException e) {
			return super.get(occluder);
		}
	}
	
	@Override
	protected T getFromSingleThread(Occluder occluder) throws OccludedException, Throwable {
		T v = super.getFromSingleThread(occluder);
		this.value = (v == null) ? null : new WeakReference<>(v);
		return v;
	}
}
