package cc.gu.util.value;

import cc.gu.util.obserser.Observable;

public class ValuableObservable<T> extends Observable<ValuableChangedListener<? super T>, T> {
	
	final private Valuable<T> valuable;
	
	public ValuableObservable(Valuable<T> valuable) {
		this.valuable = valuable;
	}
	
	@Override
	public void onObserver(ValuableChangedListener<?super T>listener, T t) {
		listener.onValueChanged(valuable);
	}
}
