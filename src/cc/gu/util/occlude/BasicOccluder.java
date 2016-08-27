package cc.gu.util.occlude;

import cc.gu.util.ListenerHandler;

public class BasicOccluder implements Occluder {

	final private Occluder occluder;

	public BasicOccluder() {
		occluder = this;
	}

	public BasicOccluder(Occluder occluder) {
		this.occluder = occluder;
	}

	private ListenerHandler<OnOccludedListener> listenerHandler = new ListenerHandler<OnOccludedListener>() {

		@Override
		protected void onCallback(OnOccludedListener listener) {
			listener.onOccluded(occluder, occluded);
		}
	};

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

	private void occlude(OccludedException occluded) {
		boolean ed = this.occluded != null;
		this.occluded = occluded;
		if (!ed) {
			listenerHandler.callback();
		}
	}

	@Override
	public boolean isOccluded() {
		return occluded != null;
	}

	@Override
	public void occluded() throws OccludedException {
		if (isOccluded()) {
			throw occluded;
		}
	}

	@Override
	public void addOnOccludedListener(OnOccludedListener listener) {
		listenerHandler.addListener(listener);
	}

	@Override
	public void addWeakOnOccludedListener(OnOccludedListener listener) {
		listenerHandler.addWeakListener(listener);
	}

	@Override
	public void removeOnOccludedListener(OnOccludedListener listener) {
		listenerHandler.removeListener(listener);
	}
}
