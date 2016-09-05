package cc.gu.util.gather;

import cc.gu.util.occlude.OccludHandler;
import cc.gu.util.occlude.OccludedException;
import cc.gu.util.occlude.OccludedListener;
import cc.gu.util.occlude.Occluder;
import cc.gu.util.occlude.ParallelOccluder;

abstract public class SingleThreadGather<T> extends GearingGather<T> implements Gatherer<T> {

	public SingleThreadGather(Gatherer<T> gearing) {
		super(gearing);
	}

	private Sync sync;
	private class Sync extends ParallelOccluder {
		private T t;
		private boolean got;
		private Throwable e;
		private synchronized T get(Occluder occluder) throws Throwable {
			addOccluder(occluder);
			if (!got) {
				got = true;
				try {
					occluded();
					t = SingleThreadGather.super.get(this);
					occluded();
				} catch (Throwable e) {
					this.e = e;
				}
			}
			synchronized (SingleThreadGather.this) {
				if (sync == this) {
					sync = null;
				}
			}
			if (e != null) {
				throw e;
			}
			return t;
		}
	}
	
	@Override
	public T get() throws Throwable {
		return get(null);
	}
	
	synchronized public void occlude() {
		if (sync != null) {
			Sync syncing = sync;
			sync = null;
			syncing.occlude();
		}
	}

	@Override
	public T get(Occluder occluder) throws OccludedException, Throwable {
		occluder = OccludHandler.notNull(occluder);
		Sync syncing;
		synchronized (this) {
			syncing = sync;
			if (syncing == null) {
				sync = syncing = new Sync();
				sync.addObserver(new OccludedListener() {
					
					@Override
					public void onOccluded(Occluder occluder, OccludedException e) {
						synchronized (SingleThreadGather.this) {
							if (sync == occluder) {
								sync = null;
							}
						}
					}
				}, false);
			}
		}
		T t = syncing.get(occluder);
		occluder.occluded();
		return t;
	}
}
