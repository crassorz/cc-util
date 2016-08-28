package cc.gu.util.occlude;

public interface Occluder {

	public void occlude();

	public void occlude(String message);

	public void occlude(Throwable e);

	public void occlude(String message, Throwable e);

	public boolean isOccluded();

	public void occluded() throws OccludedException;

	public void addOnOccludedListener(OccludedListener listener);

	public void addWeakOnOccludedListener(OccludedListener listener);

	public void removeOnOccludedListener(OccludedListener listener);
}
