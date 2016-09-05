package cc.gu.util.value;

import cc.gu.util.gather.Gatherer;

public interface Valuable<T> extends Gatherer<T>{
	T set(T value);

	void addObserver(ValuableChangedListener<? super T> observer, boolean weak);

	void removeObserver(ValuableChangedListener<? super T> observer);
}
