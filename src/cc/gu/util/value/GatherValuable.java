package cc.gu.util.value;

import cc.gu.util.gather.Gatherer;
import cc.gu.util.occlude.Occluder;

public class GatherValuable<T> extends SingleThreadValuable<T> {

	final private Gatherer<T> gather;

	public GatherValuable(Valuable<T> gearing, Gatherer<T> gather) {
		super(gearing);
		this.gather = gather;
	}

	@Override
	public T get() throws Throwable {
		try {
			return super.get();
		} catch (Throwable e) {
			T v = getFromGather();
			return set(v);
		}
	}

	@Override
	public T get(Occluder occluder) throws Throwable {
		try {
			return super.get(occluder);
		} catch (Throwable e) {
			T v = getFromGather(occluder);
			return set(v);
		}
	}

	protected T getFromGather() throws Throwable {
		return gather.get();
	}

	protected T getFromGather(Occluder occluder) throws Throwable {
		return gather.get(occluder);
	}

}
