package cc.gu.util.occlude;

import cc.gu.util.obserser.Observable;

public class BasicOccluder implements Occluder {

	final private Occluder occluder;
	final private Observable<OccludedListener, OccludedException> observable = new Observable<OccludedListener, OccludedException>() {
		@Override
		public void onObserver(OccludedListener listener, OccludedException e) {
			listener.onOccluded(occluder, occluded);
		}
	};
	
	@Override
	public void addObserver(OccludedListener observer, boolean weak) {
		observable.addObserver(observer, weak);
		if (isOccluded()) {
			observable.onObserver(observer, occluded);
		}
	}
	
	@Override
	public void removeObserver(OccludedListener observer) {
		observable.removeObserver(observer);
	}

	public BasicOccluder() {
		occluder = this;
	}

	public BasicOccluder(Occluder occluder) {
		this.occluder = occluder;
	}

	private volatile OccludedException occluded;

	@Override
	public void occlude() {
		occlude(new OccludedException());
	}

	@Override
	public void occlude(String message) {
		occlude(new OccludedException(message));
	}

	@Override
	public void occlude(Throwable e) {
		occlude(new OccludedException(e));
	}

	@Override
	public void occlude(String message, Throwable e) {
		occlude(new OccludedException(message, e));
	}

	public void occlude(OccludedException occluded) {
		if (occluded == null) {
			return;
		}
		boolean ed = this.occluded != null;
		this.occluded = occluded;
		if (!ed) {
			onOcclude(occluded);
		}
	}
	
	protected void onOcclude(OccludedException occluded) {
		observable.observer();
	}
	
	@Override
	public OccludedException getOccludedException() {
		return occluded;
	}

	@Override
	public boolean isOccluded() {
		return occluded != null;
	}

	@Override
	public boolean occluded() throws OccludedException {
		if (isOccluded()) {
			throw occluded;
		}
		return false;
	}
}
