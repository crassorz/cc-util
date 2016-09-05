package cc.gu.util.occlude;


public class SerialOccluder extends BasicOccluder{
	final private OccludedListener listener = new OccludedListener() {
		
		@Override
		public void onOccluded(Occluder occluder, OccludedException e) {
			onSerialOccluded(occluder, e);
		}
	};
	public SerialOccluder(Occluder...occluders) {
		addOccluder(occluders);
	}
	
	
	public void addOccluder(Occluder...occluders) {
		occluders = OccludHandler.notNull(occluders);
		for (Occluder occluder : occluders) {
			addOccluder(occluder);
		}
	}	
	public void addOccluder(Occluder occluder) {
		if (occluder != null) {
			occluder.addObserver(listener, true);
		}
	}
	
	protected void onSerialOccluded(Occluder occluder, OccludedException e) {
		occluder.removeObserver(listener);
		occlude(e);
	}
}
