package cc.gu.util.gather;

import cc.gu.util.occlude.OccludedException;
import cc.gu.util.occlude.Occluder;

public interface Gatherer <T> {
	public T get() throws Throwable;
	public T get(Occluder occluder) throws OccludedException, Throwable;
}