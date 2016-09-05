package cc.gu.util.value;

import cc.gu.util.occlude.OccludHandler;
import cc.gu.util.occlude.OccludedException;
import cc.gu.util.occlude.Occluder;
import cc.gu.util.occlude.ParallelOccluder;

public class SingleThreadValuable<T> extends GearingValuable<T> {

	public SingleThreadValuable(Valuable<T> gearing) {
		super(gearing);
	}

	private Sync sync;

	private class Sync extends ParallelOccluder {
		private T t;
		private boolean seted;
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
				synchronized (SingleThreadValuable.this) {
					if (!seted) {
						this.t = t;
						this.e = e;
					}
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

		void set(T t, Throwable e) {
			synchronized (SingleThreadValuable.this) {
				if (sync != this) {
					return;
				}
				seted = true;
				this.t = t;
				this.e = e;
				occlude();
			}
		}

		@Override
		protected void onOcclude(OccludedException occluded) {
			synchronized (SingleThreadValuable.this) {
				if (sync == this) {
					sync = null;
				}
			}
			super.onOcclude(occluded);
		}
	}

	synchronized public void occlude() {
		if (sync != null) {
			Sync syncing = sync;
			sync = null;
			syncing.occlude();
		}
	}

	@Override
	synchronized public T set(T value) {
		value = super.set(value);
		if (sync != null) {
			sync.set(value, null);
		}
		return value;
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
