package cc.gu.util.occlude;

public class OccludHandler {
	final public static Occluder UNOCCLUD = new Occluder() {
		
		@Override
		public void removeObserver(OccludedListener observer) {
			
		}
		
		@Override
		public void addObserver(OccludedListener observer, boolean weak) {
			
		}
		
		@Override
		public boolean occluded() throws OccludedException {
			return false;
		}
		
		@Override
		public void occlude(String message, Throwable e) {
			
		}
		
		@Override
		public void occlude(Throwable e) {
			
		}
		
		@Override
		public void occlude(String message) {
			
		}
		
		@Override
		public void occlude() {
			
		}
		
		@Override
		public boolean isOccluded() {
			return false;
		}
		
		@Override
		public OccludedException getOccludedException() {
			return null;
		}
	}; 
	public static Occluder[] notNull(Occluder[] occluders) {
		if (occluders == null) {
			return new Occluder[0];
		}
		for (int i = 0; i < occluders.length; i++) {
			if (occluders[i] == null) {
				occluders[i] = UNOCCLUD;
			}
		}
		return occluders;
	}
	public static Occluder notNull(Occluder occluder) {
		if (occluder == null) {
			return UNOCCLUD;
		}
		return occluder;
	}
	public static boolean occluded(Occluder...occluders) throws OccludedException {
		occluders = notNull(occluders);
		for (Occluder occluder : occluders) {
			if (occluded(occluder)) {
				return true;
			}
		}
		return false;
	}
	public static boolean occluded(Occluder occluder) throws OccludedException {
		if (occluder != null && occluder.occluded()) {
			return true;
		}
		return false;
	}
}
