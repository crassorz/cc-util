package cc.gu.util.gather;

import cc.gu.util.occlude.OccludHandler;
import cc.gu.util.occlude.OccludedException;
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
				T t = null;
				Throwable e = null;
				got = true;
				try {
					t = getFromSingleThread(this);
				} catch (Throwable throwable) {
					e = throwable;
				}
				synchronized (SingleThreadGather.this) {
					this.t = t;
					this.e = e;
					if (sync == this) {
						sync = null;
					}
				}
			}
			if (e != null) {
				throw e;
			}
			return t;
		}

		@Override
		protected void onOcclude(OccludedException occluded) {
			synchronized (SingleThreadGather.this) {
				if (sync == this) {
					sync = null;
				}
			}
			super.onOcclude(occluded);
		}
	}

	@Override
	public T get() throws Throwable {
		return getFromSingleThread(null);
	}

	@Override
	public T get(Occluder occluder) throws OccludedException, Throwable {
		return getFromSingleThread(occluder);
	}

	protected T getFromSingleThread(Occluder occluder) throws OccludedException, Throwable {
		occluder = OccludHandler.notNull(occluder);
		Sync syncing;
		synchronized (this) {
			syncing = sync;
			if (syncing == null) {
				sync = syncing = new Sync();
			}
		}
		T t = syncing.get(occluder);
		occluder.occluded();
		return t;
	}
}
