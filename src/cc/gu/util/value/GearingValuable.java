package cc.gu.util.value;

import cc.gu.util.gather.GearingGather;

public class GearingValuable<T> extends GearingGather<T> implements Valuable<T> {
	
	final private Valuable<T> gearing;
	final private ValuableObservable<T> observable = new ValuableObservable<T>(this);
	@Override
	public void addObserver(ValuableChangedListener<? super T> observer, boolean weak) {
		observable.addObserver(observer, weak);
	}

	@Override
	public void removeObserver(ValuableChangedListener<? super T> observer) {
		observable.removeObserver(observer);
	}
	
	private ValuableChangedListener<T> listener = new ValuableChangedListener<T>() {

		@Override
		public void onValueChanged(Valuable<? extends T> valuable) {
			onGearingValueChanged();
		}
		
	};
	protected void onGearingValueChanged() {
		observable.observer();
	}
	public GearingValuable(Valuable<T> gearing) {
		super(gearing);
		this.gearing = gearing;
		this.gearing.addObserver(listener, true);
	}
	
	public T set(T value) {
		return setToGearing(value);
	}
	protected T setToGearing(T value) {
		return gearing.set(value);
	}
}
