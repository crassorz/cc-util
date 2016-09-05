package cc.gu.util.occlude;

public interface Occluder {

	void occlude();

	void occlude(String message);

	void occlude(Throwable e);

	void occlude(String message, Throwable e);

	boolean isOccluded();

	boolean occluded() throws OccludedException;

	OccludedException getOccludedException();
	

	void addObserver(OccludedListener observer, boolean weak);

	void removeObserver(OccludedListener observer);

}
