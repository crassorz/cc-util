package cc.gu.util.occlude;

import java.util.HashSet;
import java.util.Set;

public class ParallelOccluder extends BasicOccluder {
	final private OccludedListener listener = new OccludedListener() {

		@Override
		public void onOccluded(Occluder occluder, OccludedException e) {
			occlude(e);
		}
	};

	Set<Occluder> occluders = new HashSet<>();

	public ParallelOccluder(Occluder... occluders) {
		addOccluder(occluders);
	}
	
	public void addOccluder(Occluder...occluders) {
		occluders = OccludHandler.notNull(occluders);
		for (Occluder occluder : occluders) {
			this.occluders.add(occluder);
		}
		for (Occluder occluder : occluders) {
			occluder.addObserver(listener, true);
		}
	}	
	public void addOccluder(Occluder occluder) {
		occluder = OccludHandler.notNull(occluder);
		occluders.add(occluder);
		occluder.addObserver(listener, true);
	}

	protected void onParallelOccluded(Occluder occluder, OccludedException e) {
		if (occluders.remove(occluder) && occluders.isEmpty()) {
			occluder.removeObserver(listener);
			occlude(e);
		}
	}
}
