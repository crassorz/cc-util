package cc.gu.util.gather;

import cc.gu.util.occlude.OccludedException;
import cc.gu.util.occlude.Occluder;

public class GearingGather<T> implements Gatherer<T> {

	final private Gatherer<? extends T> gearing;

	public GearingGather(Gatherer<T> gearing) {
		this.gearing = gearing;
	}

	@Override
	public T get() throws Throwable {
		return getFromGearing();
	}

	@Override
	public T get(Occluder occluder) throws OccludedException, Throwable {
		return getFromGearing(occluder);
	}

	protected T getFromGearing() throws Throwable {
		return gearing.get();
	}
	protected T getFromGearing(Occluder occluder) throws OccludedException, Throwable {
		return gearing.get(occluder);
	}
}
