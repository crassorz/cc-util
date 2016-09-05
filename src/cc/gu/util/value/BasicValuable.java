package cc.gu.util.value;

import cc.gu.util.Util;
import cc.gu.util.occlude.Occluder;

public class BasicValuable<T> implements Valuable<T> {

	private T value;
	final private ValuableObservable<T> observable = new ValuableObservable<T>(this);

	@Override
	public void addObserver(ValuableChangedListener<? super T> observer, boolean weak) {
		observable.addObserver(observer, weak);
	}

	@Override
	public void removeObserver(ValuableChangedListener<? super T> observer) {
		observable.removeObserver(observer);
	}

	public T set(T value) {
		T old = this.value;
		this.value = value;
		if (isChanged(value, old)) {
			observable.observer();
		}
		return this.value;
	}

	protected boolean isChanged(T value, T old) {
		return !Util.equal(value, old);
	}

	@Override
	public T get() {
		return value;
	}

	@Override
	public T get(Occluder occluder) {
		return get();
	}
}
