package cc.gu.util.occlude;

import cc.gu.util.ListenerHandler;

public class Occluder {
	
	private ListenerHandler<OnOccludedListener> listenerHandler = new ListenerHandler<OnOccludedListener>() {
		
		@Override
		protected void onCallback(OnOccludedListener listener) {
			listener.onOccluded(Occluder.this, occluded);
		}
	};
	
	private volatile OccludedException occluded;
	public void occlude() {
		occlude(new OccludedException());
	}
	public void occlude(String message) {
		occlude(new OccludedException(message));
	}
	public void occlude(Throwable e) {
		occlude(new OccludedException(e));
	}
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
	
	public boolean isOccluded() {
		return occluded != null;
	}
	
	public void occluded() throws OccludedException {
		if (isOccluded()) {
			throw occluded;
		}
	}

	public void addOnOccludedListener(OnOccludedListener listener) {
		listenerHandler.addListener(listener);
	}
	public void addWeakOnOccludedListener(OnOccludedListener listener) {
		listenerHandler.addWeakListener(listener);
	}
	public void removeOnOccludedListener(OnOccludedListener listener) {
		listenerHandler.removeListener(listener);
	}
}
